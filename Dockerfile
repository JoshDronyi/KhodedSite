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

FROM openjdk:19-jdk-slim AS export

ENV KOBWEB_CLI_VERSION=0.9.15
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

# Build from project root first, then export from site directory  
WORKDIR /project
RUN ./gradlew build --no-daemon --stacktrace

WORKDIR /project/${KOBWEB_APP_ROOT}
RUN kobweb export --notty

#-----------------------------------------------------------------------------
# Create the final stage, which contains just enough bits to run the Kobweb
# server.
FROM openjdk:19-jdk-slim AS run

ARG KOBWEB_APP_ROOT

# Install curl for health checks (required by Render)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=export /project/${KOBWEB_APP_ROOT}/.kobweb .kobweb

# Render requires apps to bind to 0.0.0.0 and use PORT environment variable
ENV HOST=0.0.0.0
ENV PORT=8080

EXPOSE $PORT

# Create a startup script that handles Render's PORT environment variable
RUN echo '#!/bin/bash\n\
# Use PORT environment variable from Render, fallback to 8080\n\
export KOBWEB_SERVER_PORT=${PORT:-8080}\n\
echo "Starting Kobweb server on port $KOBWEB_SERVER_PORT"\n\
exec .kobweb/server/start.sh' > start.sh && chmod +x start.sh

ENTRYPOINT ["./start.sh"]