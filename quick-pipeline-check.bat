@echo off
REM Quick Pipeline Compatibility Check - Fast pre-commit validation

echo.
echo ⚡ Quick Pipeline Check
echo ======================

set JAVA_HOME=C:\Users\joshu\.jdks\jbr-21.0.7

echo 🔧 Java Version (Pipeline uses Java 21):
"%JAVA_HOME%\bin\java.exe" -version 2>&1 | findstr "21"
if %ERRORLEVEL% neq 0 (
    echo ❌ Java version mismatch! Pipeline uses Java 21
    exit /b 1
)

echo ✅ Java 21 confirmed

echo.
echo 🧪 Quick compilation test...
call "./gradlew.bat" :site:compileKotlinJvm :site:compileKotlinJs --no-daemon --console=plain
if %ERRORLEVEL% neq 0 (
    echo ❌ Quick compilation failed!
    echo 💡 Run full pipeline test: local-pipeline-test.bat
    exit /b 1
)

echo ✅ Quick compilation passed!

echo.
echo 📋 Basic file checks...
if not exist "Dockerfile" (
    echo ❌ Missing Dockerfile - pipeline will fail
    exit /b 1
)

if not exist ".env.template" (
    echo ⚠️ Missing .env.template - may cause pipeline issues
)

echo.
echo ⚡ Quick check PASSED!
echo 💡 For full pipeline simulation: local-pipeline-test.bat
echo 💡 For Docker environment test: docker-pipeline-test.bat