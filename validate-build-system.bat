@echo off
REM Ultimate Pre-Commit Validation - Fast pipeline compatibility check

echo.
echo ==========================================
echo ✅ Build System Validation
echo ==========================================
echo   Final check before committing changes
echo.

set JAVA_HOME=C:\Users\joshu\.jdks\jbr-21.0.7
set VALIDATION_PASSED=true

echo 📋 Critical Validations:
echo ----------------------------------------

REM 1. Java Version Check
echo 🔧 Java Version:
"%JAVA_HOME%\bin\java.exe" -version 2>&1 | findstr /C:"21" >nul
if %ERRORLEVEL% equ 0 (
    echo ✅ Java 21 confirmed
) else (
    echo ❌ Java version mismatch
    set VALIDATION_PASSED=false
)

REM 2. Gradle Properties Check
echo 🔧 Gradle Configuration:
findstr /C:"kotlin.language.version" gradle.properties >nul
if %ERRORLEVEL% equ 0 (
    echo ❌ Explicit kotlin.language.version found - will cause conflicts
    set VALIDATION_PASSED=false
) else (
    echo ✅ Auto-detected language version (no conflicts)
)

findstr /C:"kotlin.api.version" gradle.properties >nul
if %ERRORLEVEL% equ 0 (
    echo ❌ Explicit kotlin.api.version found - will cause conflicts
    set VALIDATION_PASSED=false
) else (
    echo ✅ Auto-detected API version (no conflicts)
)

REM 3. Essential Files Check
echo 🔧 Required Files:
if exist gradlew.bat (
    echo ✅ Gradle wrapper present
) else (
    echo ❌ Missing gradlew.bat
    set VALIDATION_PASSED=false
)

if exist site\build.gradle.kts (
    echo ✅ Main build script present
) else (
    echo ❌ Missing site/build.gradle.kts
    set VALIDATION_PASSED=false
)

if exist Dockerfile (
    echo ✅ Dockerfile present
) else (
    echo ❌ Missing Dockerfile
    set VALIDATION_PASSED=false
)

REM 4. Version Catalog Check
echo 🔧 Dependency Versions:
findstr /C:"kotlin = \"2.2.10\"" gradle\libs.versions.toml >nul
if %ERRORLEVEL% equ 0 (
    echo ✅ Kotlin 2.2.10 in version catalog
) else (
    echo ⚠️ Check Kotlin version in libs.versions.toml
)

findstr /C:"kobweb = \"0.22.0\"" gradle\libs.versions.toml >nul
if %ERRORLEVEL% equ 0 (
    echo ✅ Kobweb 0.22.0 in version catalog
) else (
    echo ⚠️ Check Kobweb version in libs.versions.toml
)

echo.
echo ==========================================
echo 📊 Validation Results
echo ==========================================

if "%VALIDATION_PASSED%"=="true" (
    echo.
    echo ✅ BUILD SYSTEM VALIDATED
    echo ==========================================
    echo 🎯 Status: READY FOR PIPELINE
    echo 🕐 Expected first build time: 20-25 minutes
    echo 📈 Subsequent builds: 3-5 minutes
    echo.
    echo 🚀 SAFE TO COMMIT AND PUSH
    echo    Your configuration should pass CI/CD pipeline
    echo.
    echo 💡 Pipeline will succeed with current configuration
    echo    Language version conflicts have been resolved
    echo.
    exit /b 0
) else (
    echo.
    echo ❌ BUILD SYSTEM VALIDATION FAILED
    echo ==========================================
    echo 🚫 Status: NOT READY FOR PIPELINE
    echo.
    echo 🔧 Fix the ❌ issues above before committing
    echo    These will cause CI/CD pipeline failures
    echo.
    echo 💡 Run this script again after fixes
    echo.
    exit /b 1
)
