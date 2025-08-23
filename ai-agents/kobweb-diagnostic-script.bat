@echo off
setlocal enabledelayedexpansion

REM Kobweb Project Diagnostic Script for Windows
REM Usage: kobweb-diagnostic-script.bat > diagnostic-report.txt

echo === KOBWEB PROJECT DIAGNOSTIC REPORT ===
echo Generated on: %date% %time%
echo Project Directory: %cd%
echo.

echo === VERSION INFORMATION ===
echo Current Kobweb version:
findstr /C:"kobweb" gradle\libs.versions.toml 2>nul || echo ❌ libs.versions.toml not found

echo.
echo Current Kotlin version:
findstr /C:"kotlin" gradle\libs.versions.toml 2>nul || echo ❌ Kotlin version not found

echo.
echo Compose version:
findstr /C:"jetbrains-compose" gradle\libs.versions.toml 2>nul || echo ❌ Compose version not found

echo.
echo === KOBWEB CLI STATUS ===
where kobweb >nul 2>&1
if !errorlevel! == 0 (
    echo ✅ Kobweb CLI found
    echo Version: 
    kobweb --version 2>nul || echo Version not available
    echo.
    echo Running servers:
    kobweb list 2>nul || echo ❌ Cannot check running servers
) else (
    echo ❌ Kobweb CLI not found in PATH
)

echo.
echo === PROJECT STRUCTURE VALIDATION ===
if exist "site\" (
    echo ✅ site\ directory exists
) else (
    echo ❌ site\ directory missing
)

if exist "site\build.gradle.kts" (
    echo ✅ site\build.gradle.kts exists
) else (
    echo ❌ site\build.gradle.kts missing
)

if exist ".kobweb\" (
    echo ✅ .kobweb\ directory exists
    if exist ".kobweb\conf.yaml" (
        echo ✅ .kobweb\conf.yaml exists
    ) else (
        echo ⚠️  .kobweb\conf.yaml missing ^(will be created on first run^)
    )
) else (
    echo ⚠️  .kobweb\ directory missing ^(will be created on first run^)
)

echo.
echo === SOURCE STRUCTURE CHECK ===
if exist "site\src\jsMain\kotlin\" (
    echo ✅ JS main source directory exists
    for /f %%i in ('dir /s /b "site\src\jsMain\kotlin\*pages*.kt" 2^>nul ^| find /c /v ""') do set js_pages=%%i
    echo    📄 Found !js_pages! page files
    for /f %%i in ('dir /s /b "site\src\jsMain\kotlin\*components*.kt" 2^>nul ^| find /c /v ""') do set js_components=%%i
    echo    🧩 Found !js_components! component files
) else (
    echo ❌ JS main source directory missing
)

if exist "site\src\jvmMain\kotlin\" (
    echo ✅ JVM main source directory exists
    for /f %%i in ('dir /s /b "site\src\jvmMain\kotlin\*api*.kt" 2^>nul ^| find /c /v ""') do set jvm_apis=%%i
    echo    🔌 Found !jvm_apis! API files
) else (
    echo ❌ JVM main source directory missing
)

if exist "site\src\commonMain\kotlin\" (
    echo ✅ Common main source directory exists
    for /f %%i in ('dir /s /b "site\src\commonMain\kotlin\*.kt" 2^>nul ^| find /c /v ""') do set common_files=%%i
    echo    📋 Found !common_files! shared files
) else (
    echo ⚠️  Common main source directory missing
)

echo.
echo === BUILD STATUS CHECK ===
echo Last Gradle build status:
call gradlew.bat build --dry-run >nul 2>&1
if !errorlevel! == 0 (
    echo ✅ Gradle build configuration valid
) else (
    echo ❌ Gradle build configuration has issues
)

echo.
echo Port availability check:
netstat -an 2>nul | findstr ":8080" >nul
if !errorlevel! == 0 (
    echo ⚠️  Port 8080 is occupied
) else (
    echo ✅ Port 8080 is available
)

netstat -an 2>nul | findstr ":8081" >nul
if !errorlevel! == 0 (
    echo ⚠️  Port 8081 is occupied
) else (
    echo ✅ Port 8081 is available
)

echo.
echo === DEPENDENCY ANALYSIS ===
echo Critical dependencies check:
findstr /C:"kobweb-core" site\build.gradle.kts >nul 2>&1
if !errorlevel! == 0 (
    echo ✅ kobweb-core dependency found
) else (
    echo ❌ kobweb-core dependency missing
)

findstr /C:"kobweb-silk" site\build.gradle.kts >nul 2>&1
if !errorlevel! == 0 (
    echo ✅ kobweb-silk dependency found
) else (
    echo ⚠️  kobweb-silk dependency missing ^(optional but recommended^)
)

findstr /C:"compose-html-ext" site\build.gradle.kts >nul 2>&1
if !errorlevel! == 0 (
    echo ✅ compose-html-ext dependency found
) else (
    echo ❌ compose-html-ext dependency missing
)

echo.
echo === RECENT ERROR LOGS ===
echo Checking for recent error logs...

if exist "current_errors.log" (
    for %%F in (current_errors.log) do if %%~zF gtr 0 (
        echo ❌ Found errors in current_errors.log:
        echo Last 5 lines:
        powershell "Get-Content current_errors.log | Select-Object -Last 5"
        echo.
    ) else (
        echo ✅ current_errors.log exists but is empty
    )
) else (
    echo ✅ No current_errors.log found
)

if exist "kobweb_errors.log" (
    for %%F in (kobweb_errors.log) do if %%~zF gtr 0 (
        echo ❌ Found errors in kobweb_errors.log:
        echo Last 5 lines:
        powershell "Get-Content kobweb_errors.log | Select-Object -Last 5"
        echo.
    ) else (
        echo ✅ kobweb_errors.log exists but is empty
    )
) else (
    echo ✅ No kobweb_errors.log found
)

echo.
echo === DEVELOPMENT ENVIRONMENT ===
echo Node.js version:
where node >nul 2>&1
if !errorlevel! == 0 (
    node --version 2>nul && echo ✅ Node.js found || echo ❌ Node.js found but version check failed
) else (
    echo ❌ Node.js not found
)

echo.
echo Yarn version:
where yarn >nul 2>&1
if !errorlevel! == 0 (
    yarn --version 2>nul && echo ✅ Yarn found || echo ❌ Yarn found but version check failed
) else (
    echo ⚠️  Yarn not found ^(npm can be used as alternative^)
)

echo.
echo Java version:
where java >nul 2>&1
if !errorlevel! == 0 (
    java -version 2>&1 | findstr /C:"version" && echo ✅ Java found || echo ❌ Java found but version check failed
) else (
    echo ❌ Java not found
)

echo.
echo === RECOMMENDATIONS ===
echo Based on the analysis above:

REM Check if Kobweb version is outdated
for /f "tokens=2 delims=^" " %%a in ('findstr /C:"kobweb.*=" gradle\libs.versions.toml 2^>nul') do (
    set current_version=%%a
    set current_version=!current_version:"=!
)

if not "!current_version!" == "0.23.1" (
    echo 🔄 RECOMMENDED: Update Kobweb from !current_version! to 0.23.1
)

REM Check for common issues
if not exist ".kobweb\conf.yaml" (
    echo 🛠️  RECOMMENDED: Run 'kobweb run' once to initialize configuration
)

where kobweb >nul 2>&1
if !errorlevel! neq 0 (
    echo 📦 REQUIRED: Install Kobweb CLI
)

echo.
echo === END OF DIAGNOSTIC REPORT ===
echo For assistance, provide this report to the Kobweb specialist agent.

endlocal