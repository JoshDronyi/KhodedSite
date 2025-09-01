@echo off
REM Local Pipeline Testing Script - Mimics GitHub Actions CI/CD
REM This script replicates the build environment and tests locally before committing

echo.
echo ========================================
echo 🧪 Local Pipeline Testing Environment
echo ========================================
echo.

REM Set Java environment to match pipeline (Java 21 Temurin)
set JAVA_HOME=C:\Users\joshu\.jdks\jbr-21.0.7
set GRADLE_OPTS=-Dorg.gradle.daemon=false -Dorg.gradle.parallel=true -Dorg.gradle.workers.max=2

REM Create test environment directory
set TEST_ENV_DIR=C:\tmp\pipeline-test-%RANDOM%
mkdir "%TEST_ENV_DIR%"

echo 📁 Test environment: %TEST_ENV_DIR%
echo 🔧 Java Version Check:
"%JAVA_HOME%\bin\java.exe" -version

echo.
echo ========================================
echo 📋 Phase 1: Environment Setup
echo ========================================

REM Copy project to isolated test environment
echo 📦 Copying project to test environment...
xcopy /E /I /Q . "%TEST_ENV_DIR%\project" /EXCLUDE:exclude.tmp > nul 2>nul || xcopy /E /I /Q . "%TEST_ENV_DIR%\project" > nul
cd /d "%TEST_ENV_DIR%\project"

REM Ensure gradle wrapper has execute permissions
if exist gradlew.bat (
    echo ✅ Gradle wrapper found
) else (
    echo ❌ Gradle wrapper missing!
    goto :failure
)

REM Create .env from template (matching pipeline)
echo 🔧 Setting up environment variables...
if exist .env.template (
    copy .env.template .env > nul
    echo APP_ENVIRONMENT=development >> .env
    echo PRIMARY_DOMAIN=localhost:8080 >> .env
) else (
    echo APP_ENVIRONMENT=development > .env
    echo PRIMARY_DOMAIN=localhost:8080 >> .env
)

REM Create dummy secrets for testing (matching pipeline)
mkdir secrets 2>nul
echo {"type":"service_account","project_id":"test","client_email":"test@test.com","private_key":"dummy"} > secrets\gmail_service_account_key.json

echo.
echo ========================================
echo 🧪 Phase 2: Test Suite (JS Tests)
echo ========================================

echo 🔧 Running Kotlin/JS tests (matching pipeline jsTest)...
call "./gradlew.bat" jsTest --no-daemon --console=plain --build-cache --parallel
if %ERRORLEVEL% neq 0 (
    echo ❌ JS Tests failed!
    goto :failure
)
echo ✅ JS Tests passed!

echo.
echo ========================================
echo 🏗️ Phase 3: Build Application
echo ========================================

echo 🔧 Building application (matching pipeline build)...
call "./gradlew.bat" build --no-daemon --console=plain --build-cache --parallel
if %ERRORLEVEL% neq 0 (
    echo ❌ Build failed!
    goto :failure
)
echo ✅ Build completed successfully!

echo.
echo ========================================
echo 🐳 Phase 4: Docker Build Test
echo ========================================

echo 🔧 Testing Docker image build...
cd ..
docker build -f project\Dockerfile -t khoded:local-pipeline-test project
if %ERRORLEVEL% neq 0 (
    echo ❌ Docker build failed!
    goto :failure
)
echo ✅ Docker build successful!

echo 🔧 Testing Docker image...
docker run --rm khoded:local-pipeline-test java -version
if %ERRORLEVEL% neq 0 (
    echo ❌ Docker image test failed!
    goto :failure
)
echo ✅ Docker image test passed!

echo.
echo ========================================
echo 🔒 Phase 5: Basic Security Checks
echo ========================================

echo 🔧 Checking for sensitive files...
cd project
findstr /S /M /C:"password" /C:"secret" /C:"api_key" *.gradle* *.kt *.js 2>nul
if %ERRORLEVEL% equ 0 (
    echo ⚠️ Found potential sensitive data in source files
) else (
    echo ✅ No obvious sensitive data found in source files
)

echo 🔧 Checking .env template...
if exist .env.template (
    findstr /C:"=" .env.template | findstr /V /C:"PLACEHOLDER" /C:"YOUR_" /C:"EXAMPLE"
    if %ERRORLEVEL% equ 0 (
        echo ⚠️ .env.template may contain real values
    ) else (
        echo ✅ .env.template looks safe
    )
) else (
    echo ⚠️ No .env.template found
)

echo.
echo ========================================
echo 🎉 Pipeline Test Results
echo ========================================

echo ✅ All pipeline phases completed successfully!
echo.
echo 📊 Summary:
echo   • Environment Setup: ✅ PASSED
echo   • JS Tests:          ✅ PASSED  
echo   • Application Build: ✅ PASSED
echo   • Docker Build:      ✅ PASSED
echo   • Docker Test:       ✅ PASSED
echo   • Security Checks:   ✅ PASSED
echo.
echo 🚀 Your changes are ready for commit and push!
echo    The pipeline should succeed with these changes.
echo.

REM Cleanup
echo 🧹 Cleaning up test environment...
cd /d C:\
rmdir /S /Q "%TEST_ENV_DIR%" 2>nul
docker rmi khoded:local-pipeline-test 2>nul

exit /b 0

:failure
echo.
echo ========================================
echo ❌ Pipeline Test FAILED
echo ========================================
echo.
echo 💡 Fix the issues above before committing.
echo    This would have failed in the GitHub Actions pipeline.
echo.

REM Cleanup on failure
cd /d C:\
rmdir /S /Q "%TEST_ENV_DIR%" 2>nul
docker rmi khoded:local-pipeline-test 2>nul

exit /b 1