#!/bin/bash

# Render.com Startup Script for Khoded Application
# Handles database migrations and application startup

set -euo pipefail

echo "🚀 Starting Khoded application on Render..."
echo "Environment: ${APP_ENVIRONMENT:-unknown}"
echo "Port: ${PORT:-8080}"

# Check if DATABASE_URL is available
if [ -n "${DATABASE_URL:-}" ]; then
    echo "✅ Database URL detected"
    
    # Wait for database to be available
    echo "⏳ Waiting for database connection..."
    
    # Extract connection details from DATABASE_URL for health check
    # DATABASE_URL format: postgresql://user:password@host:port/database
    DB_HOST=$(echo $DATABASE_URL | sed 's/.*@\([^:]*\):.*/\1/')
    DB_PORT=$(echo $DATABASE_URL | sed 's/.*:\([0-9]*\)\/.*/\1/')
    
    # Wait for database to be ready (max 60 seconds)
    for i in {1..12}; do
        if pg_isready -h "$DB_HOST" -p "$DB_PORT" >/dev/null 2>&1; then
            echo "✅ Database is ready"
            break
        fi
        echo "⏳ Waiting for database... (attempt $i/12)"
        sleep 5
    done
    
    # Run database migrations if we're in production and have Flyway
    if [ "${APP_ENVIRONMENT:-}" = "production" ] && [ -d "site/src/jvmMain/resources/KhodedSiteData/migrations" ]; then
        echo "🔄 Running database migrations..."
        
        # Note: In a real production setup, you'd want to run Flyway migrations here
        # For now, we'll rely on the application's schema creation in KhodedDB.kt
        echo "📝 Database migrations will be handled by application startup"
    fi
else
    echo "⚠️  No DATABASE_URL found - running in development mode"
fi

# Check for Gmail service account configuration
if [ -n "${GMAIL_SERVICE_ACCOUNT_JSON:-}" ]; then
    echo "✅ Gmail service account configured"
else
    echo "⚠️  Gmail service account not configured - email functionality may not work"
fi

# Set JVM options for Render's memory constraints
export JAVA_OPTS="${JAVA_OPTS:-} -Xmx512m -XX:+UseG1GC -XX:MaxRAMPercentage=75"

# Start the Kobweb application
echo "🎯 Starting Kobweb server on port ${PORT:-8080}..."
exec .kobweb/server/start.sh