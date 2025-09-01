#-----------------------------------------------------------------------------
# Declare variables shared across multiple stages (they need to be explicitly
# opted into each stage by being declaring there too, but their values need
# only be specified once).
ARG KOBWEB_APP_ROOT="site"
# ^ NOTE: KOBWEB_APP_ROOT is commonly set to "site" in multimodule projects

#-----------------------------------------------------------------------------
# Create an intermediate stage which builds and exports our site. In the
# final stage, we'll only extract what we need from this stage, saving a lot
# of space.

FROM eclipse-temurin:21-jdk AS export

ENV KOBWEB_CLI_VERSION=0.9.21
ARG KOBWEB_APP_ROOT

# Copy the project code to an arbitrary subdir so we can install stuff in the
# Docker container root without worrying about clobbering project files.
COPY . /project

# Set execute permissions on gradlew
RUN chmod +x /project/gradlew

# Update and install required OS packages to continue
# Note: Playwright is a system for running browsers, and here we use it to
# install Chromium.
RUN apt-get update \
    && apt-get install -y curl gnupg unzip wget \
    && curl -SLO https://deb.nodesource.com/nsolid_setup_deb.sh | bash -  \
    && chmod 500 nsolid_setup_deb.sh \
    && ./nsolid_setup_deb.sh 21 \
    && apt-get install nodejs -y \
    && npm install -g npm@10.5.0 \
    && npm init -y \
    && npx playwright install --with-deps chromium

# Fetch the latest version of the Kobweb CLI
RUN wget https://github.com/varabyte/kobweb-cli/releases/download/v${KOBWEB_CLI_VERSION}/kobweb-${KOBWEB_CLI_VERSION}.zip \
    && unzip kobweb-${KOBWEB_CLI_VERSION}.zip \
    && rm kobweb-${KOBWEB_CLI_VERSION}.zip

ENV PATH="/kobweb-${KOBWEB_CLI_VERSION}/bin:${PATH}"

WORKDIR /project/${KOBWEB_APP_ROOT}

# Configure Gradle for Docker builds - increased memory for Kotlin/JS compilation
RUN mkdir ~/.gradle && \
    echo "org.gradle.jvmargs=-Xmx1536m -XX:MaxMetaspaceSize=256m" >> ~/.gradle/gradle.properties && \
    echo "org.gradle.daemon=false" >> ~/.gradle/gradle.properties && \
    echo "kotlin.daemon.jvmargs=-Xmx1024m -XX:MaxMetaspaceSize=256m" >> ~/.gradle/gradle.properties

# Build and export using proper Gradle workflow
WORKDIR /project
RUN echo "Building project..." && \
    ./gradlew build --no-daemon --stacktrace && \
    echo "Build completed, now exporting for production deployment..." && \
    ./gradlew kobwebExport -PkobwebReuseServer=false -PkobwebEnv=PROD -PkobwebRunLayout=FULLSTACK -PkobwebBuildTarget=RELEASE -PkobwebExportLayout=FULLSTACK --no-daemon --stacktrace

WORKDIR /project/${KOBWEB_APP_ROOT}

# List exported content for debugging  
RUN echo "=== Kobweb export completed ===" && \
    echo "Full .kobweb structure:" && \
    find .kobweb -type f 2>/dev/null || echo "No .kobweb directory found" && \
    ls -la .kobweb/ 2>/dev/null || echo "Cannot list .kobweb directory" && \
    if [ -d ".kobweb/site" ]; then echo "Site directory:"; ls -la .kobweb/site/; else echo "No .kobweb/site directory found"; fi && \
    if [ -d ".kobweb/server" ]; then echo "Server directory:"; ls -la .kobweb/server/; else echo "No .kobweb/server directory found"; fi

#-----------------------------------------------------------------------------
# Create the final stage, which contains just enough bits to run the Kobweb
# server.
FROM eclipse-temurin:21-jre AS run

ARG KOBWEB_APP_ROOT

# Install curl for health checks and wget for Kobweb CLI (required by Render)
RUN apt-get update && apt-get install -y curl wget unzip && rm -rf /var/lib/apt/lists/*

# Install Kobweb CLI in runtime image
ENV KOBWEB_CLI_VERSION=0.9.21
RUN wget https://github.com/varabyte/kobweb-cli/releases/download/v${KOBWEB_CLI_VERSION}/kobweb-${KOBWEB_CLI_VERSION}.zip \
    && unzip kobweb-${KOBWEB_CLI_VERSION}.zip \
    && rm kobweb-${KOBWEB_CLI_VERSION}.zip
ENV PATH="/kobweb-${KOBWEB_CLI_VERSION}/bin:${PATH}"

COPY --from=export /project/${KOBWEB_APP_ROOT}/.kobweb .kobweb
COPY --from=export /project/${KOBWEB_APP_ROOT}/build ./build

# Render requires apps to bind to 0.0.0.0 and use PORT environment variable
ENV HOST=0.0.0.0
ENV PORT=8080

EXPOSE $PORT

# Create a startup script for production Kobweb server
RUN echo '#!/bin/bash\n\
# Use PORT environment variable from Render, fallback to 8080\n\
export KOBWEB_SERVER_PORT=${PORT:-8080}\n\
export SERVER_PORT=${PORT:-8080}\n\
echo "Starting Kobweb production server on port $KOBWEB_SERVER_PORT"\n\
\n\
# Update config file with dynamic port\n\
sed -i "s/port: 8080/port: $KOBWEB_SERVER_PORT/" .kobweb/conf.yaml\n\
\n\
echo "Production server configuration:"\n\
cat .kobweb/conf.yaml\n\
\n\
# Check for exported server components and site files\n\
echo "Exported server structure:"\n\
find .kobweb -type f -name "*.jar" -o -name "start.sh" -o -name "*.js"\n\
echo "Site directory check:"\n\
if [ -d ".kobweb/site" ]; then\n\
  echo ".kobweb/site exists with $(find .kobweb/site -type f | wc -l) files"\n\
  ls -la .kobweb/site/ | head -10\n\
else\n\
  echo "ERROR: .kobweb/site directory missing!"\n\
  echo "Available .kobweb contents:"\n\
  ls -la .kobweb/\n\
fi\n\
\n\
# Use the exported server components for production\n\
if [ -f ".kobweb/server/start.sh" ]; then\n\
  echo "Using exported server start script"\n\
  exec .kobweb/server/start.sh\n\
elif [ -f ".kobweb/start.sh" ]; then\n\
  echo "Using exported start script"\n\
  exec .kobweb/start.sh\n\
else\n\
  echo "No exported server found, trying direct server execution"\n\
  # Find the server JAR in the exported structure\n\
  SERVER_JAR=$(find .kobweb -name "*.jar" | grep -i server | head -1)\n\
  if [ -n "$SERVER_JAR" ]; then\n\
    echo "Found server JAR: $SERVER_JAR"\n\
    exec java -Dserver.port=$KOBWEB_SERVER_PORT -jar "$SERVER_JAR"\n\
  else\n\
    echo "No server components found in export. Available files:"\n\
    find .kobweb -type f\n\
    exit 1\n\
  fi\n\
fi' > start.sh && chmod +x start.sh

ENTRYPOINT ["./start.sh"]