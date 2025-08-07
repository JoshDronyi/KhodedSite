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

ENV KOBWEB_CLI_VERSION=0.9.20
ARG KOBWEB_APP_ROOT

# Copy the project code to an arbitrary subdir so we can install stuff in the
# Docker container root without worrying about clobbering project files.
COPY . /project


# Install build dependencies including browser for Kobweb export
# Note: Kobweb export explicitly requires a browser for static site generation
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        curl \
        gnupg \
        unzip \
        wget \
        ca-certificates \
    && curl -fsSL https://deb.nodesource.com/setup_20.x | bash - \
    && apt-get install -y --no-install-recommends nodejs \
    && npm install -g npm@10.8.2 \
    && npx playwright@1.46.1 install --with-deps chromium \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Fetch Kobweb CLI with robust retry mechanism for network reliability
RUN set -e && \
    KOBWEB_URL="https://github.com/varabyte/kobweb-cli/releases/download/v${KOBWEB_CLI_VERSION}/kobweb-${KOBWEB_CLI_VERSION}.zip" && \
    echo "Downloading Kobweb CLI from: $KOBWEB_URL" && \
    for i in {1..5}; do \
        echo "Download attempt $i/5..." && \
        if curl -L --connect-timeout 30 --max-time 300 --retry 3 --retry-delay 5 --fail "$KOBWEB_URL" -o "kobweb-${KOBWEB_CLI_VERSION}.zip"; then \
            echo "Download successful!" && \
            break; \
        fi; \
        if [ $i -eq 5 ]; then \
            echo "All download attempts failed" && exit 1; \
        fi; \
        echo "Download failed, retrying in 10 seconds..." && \
        sleep 10; \
    done && \
    unzip "kobweb-${KOBWEB_CLI_VERSION}.zip" && \
    rm "kobweb-${KOBWEB_CLI_VERSION}.zip"

ENV PATH="/kobweb-${KOBWEB_CLI_VERSION}/bin:${PATH}"

WORKDIR /project/${KOBWEB_APP_ROOT}

# Optimize Gradle for Docker builds with container-aware JVM settings (2025 best practices)
RUN mkdir ~/.gradle && \
    echo "org.gradle.jvmargs=-XX:MaxRAMPercentage=70.0 -XX:InitialRAMPercentage=70.0 -XX:+ExitOnOutOfMemoryError -XX:+UseG1GC" >> ~/.gradle/gradle.properties && \
    echo "org.gradle.daemon=false" >> ~/.gradle/gradle.properties && \
    echo "org.gradle.parallel=false" >> ~/.gradle/gradle.properties && \
    echo "org.gradle.configureondemand=false" >> ~/.gradle/gradle.properties

# Create minimal local.properties if it doesn't exist (for Docker builds)
RUN if [ ! -f ../local.properties ]; then \
        echo "# Docker build - minimal properties" > ../local.properties && \
        echo "type=service_account" >> ../local.properties && \
        echo "project_id=docker-build" >> ../local.properties && \
        echo "private_key_id=docker" >> ../local.properties && \
        echo "private_key=docker" >> ../local.properties && \
        echo "client_email=docker@build.com" >> ../local.properties && \
        echo "client_id=docker" >> ../local.properties && \
        echo "auth_uri=https://accounts.google.com/o/oauth2/auth" >> ../local.properties && \
        echo "token_uri=https://oauth2.googleapis.com/token" >> ../local.properties && \
        echo "auth_provider_x509_cert_url=https://www.googleapis.com/oauth2/v1/certs" >> ../local.properties && \
        echo "client_x509_cert_url=https://www.googleapis.com/oauth2/v1/certs" >> ../local.properties && \
        echo "universe_domain=googleapis.com" >> ../local.properties; \
    fi

# Fix Windows line endings and set execute permissions
RUN echo "=== Fixing Line Endings and Permissions ===" && \
    sed -i 's/\r$//' ../gradlew && \
    chmod +x ../gradlew && \
    echo "=== Verifying Build Environment ===" && \
    which kobweb && \
    kobweb --version && \
    java -version && \
    echo "=== Cleaning Previous Build ===" && \
    ../gradlew clean --no-daemon && \
    echo "=== Starting Kobweb Export (includes build) ===" && \
    # kobweb export already includes the build step, so no need for separate gradle build
    kobweb export --notty && \
    echo "=== Export Complete, Cleaning Up ===" && \
    # Clean up build artifacts to reduce image size
    rm -rf ~/.gradle/caches && \
    rm -rf ~/.gradle/daemon && \
    rm -rf build/js/node_modules && \
    rm -rf build/tmp && \
    rm -rf build/kotlin && \
    rm -rf .gradle && \
    # Clean npm cache and pin cleanup tools
    npx npm@10.8.2 cache clean --force

#-----------------------------------------------------------------------------
# Create the final stage, which contains just enough bits to run the Kobweb
# server.
# Use smaller runtime image optimized for production
FROM eclipse-temurin:21-jre AS run

ARG KOBWEB_APP_ROOT

# Copy only the necessary runtime files and verify they exist
COPY --from=export /project/${KOBWEB_APP_ROOT}/.kobweb .kobweb

# Create non-root user for security and validate runtime files
RUN groupadd -r kobweb && useradd -r -g kobweb kobweb && \
    # Verify essential files exist
    test -f .kobweb/server/start.sh || (echo "ERROR: start.sh not found" && exit 1) && \
    # Set permissions
    chown -R kobweb:kobweb .kobweb && \
    chmod +x .kobweb/server/start.sh && \
    # Create required directories if missing
    mkdir -p /tmp && chown kobweb:kobweb /tmp

USER kobweb

EXPOSE 8080

# Use exec form for better signal handling
ENTRYPOINT ["./.kobweb/server/start.sh"]