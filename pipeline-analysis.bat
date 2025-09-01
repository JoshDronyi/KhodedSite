@echo off
REM Comprehensive Pipeline Analysis - Identify all potential CI/CD issues

echo.
echo ==========================================
echo 🔍 Pipeline Compatibility Analysis
echo ==========================================
echo   Analyzing current build against CI/CD requirements
echo.

set JAVA_HOME=C:\Users\joshu\.jdks\jbr-21.0.7

echo 📋 Environment Analysis:
echo ----------------------------------------
echo 🔧 Java Version:
"%JAVA_HOME%\bin\java.exe" -version 2>&1 | findstr /C:"21"
if %ERRORLEVEL% equ 0 (
    echo ✅ Java 21 detected - matches pipeline
) else (
    echo ❌ Java version mismatch - pipeline expects Java 21
)

echo.
echo 📋 Gradle Configuration Analysis:
echo ----------------------------------------
echo 🔧 Checking gradle.properties...
if exist gradle.properties (
    echo ✅ gradle.properties found
    findstr /C:"kotlin.language.version" gradle.properties
    if %ERRORLEVEL% equ 0 (
        echo ⚠️ Explicit kotlin.language.version found - may cause version conflicts
    ) else (
        echo ✅ Using auto-detected language version
    )
    
    findstr /C:"kotlin.api.version" gradle.properties
    if %ERRORLEVEL% equ 0 (
        echo ⚠️ Explicit kotlin.api.version found - may cause version conflicts
    ) else (
        echo ✅ Using auto-detected API version
    )
) else (
    echo ❌ Missing gradle.properties
)

echo.
echo 📋 Build Script Analysis:
echo ----------------------------------------
echo 🔧 Checking site/build.gradle.kts...
if exist site\build.gradle.kts (
    echo ✅ Main build script found
    
    REM Check for explicit language version in build script
    findstr /C:"languageVersion.set" site\build.gradle.kts | findstr /C:"JavaLanguageVersion"
    if %ERRORLEVEL% equ 0 (
        echo ✅ JVM toolchain configured properly
    ) else (
        echo ⚠️ JVM toolchain may not be configured
    )
    
    REM Check for problematic compiler options
    findstr /C:"kotlin.RequiresOptIn" site\build.gradle.kts
    if %ERRORLEVEL% equ 0 (
        echo ✅ RequiresOptIn configured
    ) else (
        echo ℹ️ RequiresOptIn not found (optional)
    )
) else (
    echo ❌ Missing site/build.gradle.kts
)

echo.
echo 📋 Dependency Analysis:
echo ----------------------------------------
echo 🔧 Checking gradle/libs.versions.toml...
if exist gradle\libs.versions.toml (
    echo ✅ Version catalog found
    
    findstr /C:"kotlin =" gradle\libs.versions.toml
    if %ERRORLEVEL% equ 0 (
        echo ✅ Kotlin version specified in catalog
    ) else (
        echo ⚠️ Kotlin version not found in catalog
    )
    
    findstr /C:"kobweb =" gradle\libs.versions.toml
    if %ERRORLEVEL% equ 0 (
        echo ✅ Kobweb version specified in catalog
    ) else (
        echo ⚠️ Kobweb version not found in catalog
    )
) else (
    echo ❌ Missing version catalog
)

echo.
echo 📋 Docker Configuration Analysis:
echo ----------------------------------------
echo 🔧 Checking Dockerfile...
if exist Dockerfile (
    echo ✅ Dockerfile found
    
    findstr /C:"eclipse-temurin:21" Dockerfile
    if %ERRORLEVEL% equ 0 (
        echo ✅ Using eclipse-temurin:21 - matches pipeline
    ) else (
        echo ⚠️ Different JDK version in Docker
    )
    
    findstr /C:"gradlew build" Dockerfile
    if %ERRORLEVEL% equ 0 (
        echo ✅ Gradle build configured in Docker
    ) else (
        echo ⚠️ Build command may be missing in Docker
    )
) else (
    echo ❌ Missing Dockerfile
)

echo.
echo 📋 CI/CD Workflow Analysis:
echo ----------------------------------------
echo 🔧 Checking .github/workflows/...
if exist .github\workflows\ci-cd.yml (
    echo ✅ Main CI/CD workflow found
    
    findstr /C:"java-version: '21'" .github\workflows\ci-cd.yml
    if %ERRORLEVEL% equ 0 (
        echo ✅ Java 21 specified in workflow
    ) else (
        echo ⚠️ Java version mismatch in workflow
    )
    
    findstr /C:"jsTest" .github\workflows\ci-cd.yml
    if %ERRORLEVEL% equ 0 (
        echo ✅ JS tests configured in pipeline
    ) else (
        echo ⚠️ JS tests may not be configured
    )
) else (
    echo ❌ Missing main CI/CD workflow
)

echo.
echo 📋 Environment Configuration Analysis:
echo ----------------------------------------
echo 🔧 Checking environment setup...
if exist .env.template (
    echo ✅ Environment template found
) else (
    echo ⚠️ Missing .env.template - pipeline creates this
)

if exist secrets\gmail_service_account_key.json.template (
    echo ✅ Service account template found
) else (
    echo ⚠️ Missing service account template
)

echo.
echo 📋 Test Configuration Analysis:
echo ----------------------------------------
echo 🔧 Checking test setup...
if exist site\src\jsTest\kotlin (
    echo ✅ JS test directory structure found
) else (
    echo ⚠️ JS test directory may be missing
)

if exist site\src\jvmTest\kotlin (
    echo ✅ JVM test directory structure found
) else (
    echo ⚠️ JVM test directory may be missing
)

echo.
echo ==========================================
echo 📊 Analysis Summary
echo ==========================================
echo.
echo 🔧 Critical Issues (will cause pipeline failure):
echo    - Check for ❌ markers above
echo.
echo ⚠️ Warning Issues (may cause pipeline failure):
echo    - Check for ⚠️ markers above
echo.
echo 💡 Recommendations:
echo    1. Run 'quick-pipeline-check.bat' for compilation test
echo    2. Fix any critical (❌) issues found above
echo    3. Address warning (⚠️) issues before pushing
echo    4. Use Docker Desktop for full pipeline simulation
echo.
echo 🎯 Next Steps:
echo    - Fix critical issues
echo    - Test with clean Gradle cache
echo    - Verify Docker build works
echo    - Push to feature branch for pipeline validation