@echo off
echo ======================================
echo PRODUCTION DOCKER BUILD TEST
echo ======================================

echo.
echo 1. Checking Docker status...
docker --version
if %errorlevel% neq 0 (
    echo ERROR: Docker is not running!
    exit /b 1
)

echo.
echo 2. Building Production Dockerfile with all functionality...
docker build -f Dockerfile.production -t khoded-production . --progress=plain
if %errorlevel% neq 0 (
    echo ERROR: Production build failed!
    exit /b 1
)

echo.
echo 3. Building with docker-compose...
docker-compose build --no-cache
if %errorlevel% neq 0 (
    echo ERROR: Compose build failed!
    exit /b 1
)

echo.
echo 4. Testing container startup...
docker-compose up -d
if %errorlevel% neq 0 (
    echo ERROR: Container startup failed!
    exit /b 1
)

echo.
echo 5. Checking container status...
docker ps | findstr khoded

echo.
echo 6. Checking image size...
docker images | findstr khoded

echo.
echo 7. Cleanup...
docker-compose down

echo.
echo ======================================
echo PRODUCTION BUILD FEATURES:
echo - Node.js ^& Playwright for browser support
echo - Kobweb CLI with export functionality  
echo - Windows line ending fixes
echo - Optimized Gradle configuration
echo - Proper runtime security setup
echo - All original functionality preserved
echo ======================================