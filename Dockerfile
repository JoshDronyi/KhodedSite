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

# Build project then export using CLI
WORKDIR /project
RUN echo "Building project..." && \
    ./gradlew build --no-daemon --stacktrace

WORKDIR /project/${KOBWEB_APP_ROOT}
RUN echo "Exporting fullstack Kobweb application..." && \
    kobweb export --layout fullstack --notty && \
    echo "Creating missing site directory if needed..." && \
    if [ ! -d ".kobweb/site" ] && [ -d "build/processedResources/js/main/public" ]; then \
        mkdir -p .kobweb/site && \
        cp -r build/processedResources/js/main/public/* .kobweb/site/ && \
        echo "Created .kobweb/site from build resources"; \
    fi && \
    echo "Export validation:" && \
    echo "Server: $(ls -la .kobweb/server/ 2>/dev/null | wc -l) files" && \
    echo "Site: $(find .kobweb/site -type f 2>/dev/null | wc -l) files" && \
    if [ ! -f ".kobweb/server/start.sh" ]; then \
        echo "❌ Missing server start script"; \
        exit 1; \
    fi

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

# Simple startup script - just run the exported Kobweb server
RUN echo '#!/bin/bash\n\
export PORT=${PORT:-8080}\n\
echo "Starting Kobweb server on port $PORT"\n\
\n\
# Update port in config\n\
sed -i "s/port: 8080/port: $PORT/" .kobweb/conf.yaml\n\
\n\
# Run the exported server directly\n\
exec .kobweb/server/start.sh' > start.sh && chmod +x start.sh

ENTRYPOINT ["./start.sh"]