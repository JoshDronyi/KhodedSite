@echo off
setlocal EnableDelayedExpansion

echo ===============================================================================
echo                    KHODED PROJECT - BUILD SYSTEM VALIDATION
echo                        Comprehensive Build Health Check
echo ===============================================================================
echo.

set JAVA_HOME=C:\Users\joshu\.jdks\jbr-21.0.7
set PATH=%JAVA_HOME%\bin;%PATH%
set BUILD_LOG=%~dp0build-validation.log
set ERROR_COUNT=0

echo [INFO] Starting build validation at %DATE% %TIME% > "%BUILD_LOG%"
echo [INFO] Java Home: %JAVA_HOME% >> "%BUILD_LOG%"

echo === 1. Environment Validation ===
echo [TEST] Validating Java installation...
"%JAVA_HOME%\bin\java" -version 2>&1 | findstr "21.0.7" >nul
if %ERRORLEVEL% EQU 0 (
    echo [PASS] Java 21.0.7 detected
    echo [PASS] Java version validation successful >> "%BUILD_LOG%"
) else (
    echo [FAIL] Java 21.0.7 not found
    echo [FAIL] Java version validation failed >> "%BUILD_LOG%"
    set /a ERROR_COUNT+=1
)

echo [TEST] Validating Gradle wrapper...
if exist gradlew.bat (
    echo [PASS] Gradle wrapper found
    echo [PASS] Gradle wrapper validation successful >> "%BUILD_LOG%"
) else (
    echo [FAIL] Gradle wrapper not found
    echo [FAIL] Gradle wrapper validation failed >> "%BUILD_LOG%"
    set /a ERROR_COUNT+=1
)

echo.
echo === 2. Configuration Validation ===
echo [TEST] Checking libs.versions.toml...
if exist "gradle\libs.versions.toml" (
    findstr "kotlin = \"2.1.21\"" "gradle\libs.versions.toml" >nul
    if %ERRORLEVEL% EQU 0 (
        echo [PASS] Kotlin version 2.1.21 configured
        echo [PASS] Kotlin version configuration correct >> "%BUILD_LOG%"
    ) else (
        echo [FAIL] Kotlin version not set to 2.1.21
        echo [FAIL] Kotlin version configuration incorrect >> "%BUILD_LOG%"
        set /a ERROR_COUNT+=1
    )
    
    findstr "kobweb = \"0.22.0\"" "gradle\libs.versions.toml" >nul
    if %ERRORLEVEL% EQU 0 (
        echo [PASS] Kobweb version 0.22.0 configured
        echo [PASS] Kobweb version configuration correct >> "%BUILD_LOG%"
    ) else (
        echo [FAIL] Kobweb version not set to 0.22.0
        echo [FAIL] Kobweb version configuration incorrect >> "%BUILD_LOG%"
        set /a ERROR_COUNT+=1
    )
) else (
    echo [FAIL] libs.versions.toml not found
    echo [FAIL] Version catalog not found >> "%BUILD_LOG%"
    set /a ERROR_COUNT+=1
)

echo.
echo === 3. Clean Build Test ===
echo [TEST] Testing Gradle version check...
set GRADLE_TEMP_HOME=C:\tmp\.gradle_validation_%RANDOM%
mkdir "%GRADLE_TEMP_HOME%" 2>nul

gradlew.bat --gradle-user-home "%GRADLE_TEMP_HOME%" --version --no-daemon --console=plain > temp_gradle_output.txt 2>&1
if %ERRORLEVEL% EQU 0 (
    echo [PASS] Gradle execution successful
    echo [PASS] Gradle execution test successful >> "%BUILD_LOG%"
) else (
    echo [FAIL] Gradle execution failed
    echo [FAIL] Gradle execution test failed >> "%BUILD_LOG%"
    type temp_gradle_output.txt >> "%BUILD_LOG%"
    set /a ERROR_COUNT+=1
)

del temp_gradle_output.txt 2>nul
rmdir /s /q "%GRADLE_TEMP_HOME%" 2>nul

echo.
echo === 4. Docker Configuration Validation ===
echo [TEST] Checking Dockerfile configurations...
if exist Dockerfile (
    findstr "eclipse-temurin:21" Dockerfile >nul
    if %ERRORLEVEL% EQU 0 (
        echo [PASS] Main Dockerfile uses correct Java 21 base image
        echo [PASS] Main Dockerfile Java version correct >> "%BUILD_LOG%"
    ) else (
        echo [FAIL] Main Dockerfile not using Java 21 base image
        echo [FAIL] Main Dockerfile Java version incorrect >> "%BUILD_LOG%"
        set /a ERROR_COUNT+=1
    )
) else (
    echo [WARN] Main Dockerfile not found
    echo [WARN] Main Dockerfile not found >> "%BUILD_LOG%"
)

if exist Dockerfile.production (
    findstr "eclipse-temurin:21" Dockerfile.production >nul
    if %ERRORLEVEL% EQU 0 (
        echo [PASS] Production Dockerfile uses correct Java 21 base image  
        echo [PASS] Production Dockerfile Java version correct >> "%BUILD_LOG%"
    ) else (
        echo [FAIL] Production Dockerfile not using Java 21 base image
        echo [FAIL] Production Dockerfile Java version incorrect >> "%BUILD_LOG%"
        set /a ERROR_COUNT+=1
    )
) else (
    echo [WARN] Production Dockerfile not found
    echo [WARN] Production Dockerfile not found >> "%BUILD_LOG%"
)

echo.
echo === 5. Source Code Structure Validation ===
echo [TEST] Checking critical source directories...
set SOURCE_DIRS=site\src\commonMain site\src\jsMain site\src\jvmMain

for %%D in (%SOURCE_DIRS%) do (
    if exist "%%D" (
        echo [PASS] Directory %%D exists
        echo [PASS] Source directory %%D found >> "%BUILD_LOG%"
    ) else (
        echo [FAIL] Directory %%D missing
        echo [FAIL] Source directory %%D missing >> "%BUILD_LOG%"
        set /a ERROR_COUNT+=1
    )
)

echo.
echo ===============================================================================
echo                           VALIDATION SUMMARY
echo ===============================================================================
echo [INFO] Validation completed at %DATE% %TIME% >> "%BUILD_LOG%"

if %ERROR_COUNT% EQU 0 (
    echo [SUCCESS] All validation checks passed!
    echo [SUCCESS] Build system is properly configured and ready for production
    echo [SUCCESS] All validation checks passed >> "%BUILD_LOG%"
    echo.
    echo RECOMMENDATIONS:
    echo - Run 'gradlew build' to perform a full build test
    echo - Use Docker builds for consistent deployment environments  
    echo - Monitor build times and optimize as needed
) else (
    echo [FAILURE] %ERROR_COUNT% validation checks failed!
    echo [FAILURE] Please review the issues above and consult build-validation.log
    echo [FAILURE] %ERROR_COUNT% validation checks failed >> "%BUILD_LOG%"
    echo.
    echo NEXT STEPS:
    echo - Review the failed checks above
    echo - Check build-validation.log for detailed information
    echo - Fix configuration issues before proceeding with builds
)

echo.
echo Log file: %BUILD_LOG%
echo ===============================================================================
pause