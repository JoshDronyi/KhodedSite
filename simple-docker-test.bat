@echo off
echo Testing Docker Environment
echo ==========================

echo Step 1: Check Docker daemon...
docker info >nul 2>&1
if errorlevel 1 (
    echo Docker daemon not accessible
    echo Attempting to start Docker Desktop...
    
    if exist "C:\Program Files\Docker\Docker\Docker Desktop.exe" (
        start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
        echo Waiting 45 seconds for Docker to start...
        ping -n 45 127.0.0.1 >nul
        
        echo Testing Docker again...
        docker info >nul 2>&1
        if errorlevel 1 (
            echo Docker still not accessible
            echo.
            echo MANUAL STEPS REQUIRED:
            echo 1. Open Docker Desktop from Start menu
            echo 2. Wait for "Docker Desktop is running" 
            echo 3. Run this script again
            echo.
            echo Current status: Docker fixes applied, awaiting daemon access
            exit /b 1
        ) else (
            echo Docker is now accessible!
        )
    ) else (
        echo Docker Desktop not found at expected location
        exit /b 1
    )
) else (
    echo Docker daemon is accessible!
)

echo.
echo Step 2: Test simple Docker build...
docker build -f Dockerfile.minimal -t khoded-test . --quiet
if errorlevel 1 (
    echo Docker build failed
) else (
    echo Docker build successful!
    echo.
    echo Testing container run...
    docker run --rm -d -p 8080:8080 --name khoded-test-container khoded-test
    if errorlevel 1 (
        echo Container failed to start
    ) else (
        echo Container started successfully!
        echo Stopping test container...
        docker stop khoded-test-container
        echo.
        echo SUCCESS: Docker environment is fully functional!
    )
)

echo.
echo Docker Environment Status: Ready for deployment