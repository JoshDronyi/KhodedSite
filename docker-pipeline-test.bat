@echo off
REM Docker Pipeline Testing - Exact Ubuntu + Java 21 Environment Match
REM This runs your code in the exact same environment as GitHub Actions

echo.
echo ==========================================
echo 🐳 Docker Pipeline Testing Environment
echo ==========================================
echo   Replicating: Ubuntu Latest + Java 21 Temurin
echo   Matching: GitHub Actions CI/CD Pipeline
echo.

set IMAGE_NAME=khoded:pipeline-test
set CONTAINER_NAME=khoded-pipeline-test-%RANDOM%

echo 🏗️ Building pipeline test image...
docker build -f Dockerfile.local-test -t %IMAGE_NAME% . --no-cache
if %ERRORLEVEL% neq 0 (
    echo ❌ Docker build failed!
    exit /b 1
)
echo ✅ Pipeline test image built successfully!

echo.
echo 🧪 Running pipeline tests in Docker...
echo ----------------------------------------

REM Run the container with the same tests as CI/CD pipeline
docker run --rm --name %CONTAINER_NAME% %IMAGE_NAME%
set TEST_RESULT=%ERRORLEVEL%

echo.
echo ==========================================
echo 📊 Docker Pipeline Test Results
echo ==========================================

if %TEST_RESULT% equ 0 (
    echo ✅ All pipeline tests PASSED in Docker environment!
    echo.
    echo 🎉 Your changes should work in GitHub Actions
    echo    • Environment: Ubuntu + Java 21 Temurin ✅
    echo    • JS Tests: PASSED ✅
    echo    • Build: PASSED ✅
    echo    • Docker compatibility: PASSED ✅
    echo.
    echo 🚀 Ready to commit and push!
) else (
    echo ❌ Pipeline tests FAILED in Docker environment!
    echo.
    echo 💡 Issues found that would cause CI/CD failure:
    echo    Fix these before committing to avoid pipeline failures
    echo.
    echo 🔧 Debug the container by running:
    echo    docker run -it --rm %IMAGE_NAME% /bin/bash
)

echo.
echo 🧹 Cleaning up...
docker rmi %IMAGE_NAME% 2>nul

exit /b %TEST_RESULT%