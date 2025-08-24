@echo off
REM Simple Windows batch script for environment management
REM For Windows development environment

setlocal enabledelayedexpansion

if "%~1"=="" (
    echo Usage: env-setup.bat [command] [environment]
    echo.
    echo Commands:
    echo   setup [env]     - Setup environment
    echo   validate [env]  - Validate environment  
    echo   status          - Show status
    echo   clean           - Clean temp files
    echo.
    echo Environments: development, staging, production
    exit /b 1
)

set "COMMAND=%~1"
set "ENV=%~2"
if "%ENV%"=="" set "ENV=development"

REM Get script directory
set "SCRIPT_DIR=%~dp0"
set "PROJECT_ROOT=%SCRIPT_DIR%.."
set "ENV_FILES_DIR=%PROJECT_ROOT%\env-files"

if "%COMMAND%"=="setup" goto :setup
if "%COMMAND%"=="validate" goto :validate
if "%COMMAND%"=="status" goto :status
if "%COMMAND%"=="clean" goto :clean

echo Error: Unknown command "%COMMAND%"
exit /b 1

:setup
echo [INFO] Setting up %ENV% environment...

REM Check if template exists
if not exist "%ENV_FILES_DIR%\.env.%ENV%" (
    echo [ERROR] Template not found: %ENV_FILES_DIR%\.env.%ENV%
    exit /b 1
)

REM Copy template to .env
copy "%ENV_FILES_DIR%\.env.%ENV%" "%PROJECT_ROOT%\.env" >nul
echo [SUCCESS] Copied %ENV% configuration to .env

REM Create secrets directory
if not exist "%PROJECT_ROOT%\secrets" mkdir "%PROJECT_ROOT%\secrets"

REM Create dummy secrets for development
if "%ENV%"=="development" (
    echo {"type":"service_account","project_id":"development-project","client_email":"dev@development.iam.gserviceaccount.com","private_key":"-----BEGIN PRIVATE KEY-----\nDUMMY_KEY_FOR_DEVELOPMENT\n-----END PRIVATE KEY-----"} > "%PROJECT_ROOT%\secrets\gmail_service_account_key.json"
    echo [SUCCESS] Created development secrets
) else (
    echo [WARNING] For %ENV% environment, configure real secrets manually
)

echo [SUCCESS] Environment setup completed
goto :end

:validate
echo [INFO] Validating %ENV% environment...

set "ERROR_COUNT=0"

REM Check .env file
if not exist "%PROJECT_ROOT%\.env" (
    echo [ERROR] .env file not found
    set /a ERROR_COUNT+=1
) else (
    echo [SUCCESS] .env file exists
)

REM Check secrets directory
if not exist "%PROJECT_ROOT%\secrets" (
    echo [ERROR] secrets/ directory not found
    set /a ERROR_COUNT+=1
) else (
    echo [SUCCESS] secrets/ directory exists
)

REM Check Gradle wrapper
if exist "%PROJECT_ROOT%\gradlew.bat" (
    echo [SUCCESS] Gradle wrapper exists
) else (
    echo [ERROR] Gradle wrapper not found
    set /a ERROR_COUNT+=1
)

if %ERROR_COUNT%==0 (
    echo [SUCCESS] Validation passed
    exit /b 0
) else (
    echo [ERROR] Validation failed with %ERROR_COUNT% errors
    exit /b 1
)

:status
echo.
echo Environment Status
echo ===================

REM Show current environment
if exist "%PROJECT_ROOT%\.env" (
    for /f "tokens=2 delims==" %%a in ('findstr "APP_ENVIRONMENT=" "%PROJECT_ROOT%\.env" 2^>nul') do set "CURRENT_ENV=%%a"
    echo Current Environment: !CURRENT_ENV!
    echo [SUCCESS] .env file exists
) else (
    echo Current Environment: none
    echo [ERROR] .env file missing
)

REM Show secrets status
if exist "%PROJECT_ROOT%\secrets" (
    echo [SUCCESS] secrets/ directory exists
) else (
    echo [ERROR] secrets/ directory missing
)

REM Show available environments
echo.
echo Available Environments:
for %%e in (development staging production) do (
    if exist "%ENV_FILES_DIR%\.env.%%e" (
        echo   * %%e
    ) else (
        echo   ! %%e (template missing)
    )
)

goto :end

:clean
echo [INFO] Cleaning temporary files...

set "CLEANED=0"

if exist "%PROJECT_ROOT%\site\build" (
    rmdir /s /q "%PROJECT_ROOT%\site\build"
    echo [SUCCESS] Removed build artifacts
    set /a CLEANED+=1
)

if exist "%PROJECT_ROOT%\.gradle" (
    rmdir /s /q "%PROJECT_ROOT%\.gradle"
    echo [SUCCESS] Removed local Gradle cache
    set /a CLEANED+=1
)

if exist "%PROJECT_ROOT%\site\.kobweb" (
    rmdir /s /q "%PROJECT_ROOT%\site\.kobweb"
    echo [SUCCESS] Removed Kobweb cache
    set /a CLEANED+=1
)

if exist "%PROJECT_ROOT%\site\kotlin-js-store" (
    rmdir /s /q "%PROJECT_ROOT%\site\kotlin-js-store"
    echo [SUCCESS] Removed Kotlin JS store
    set /a CLEANED+=1
)

if %CLEANED%==0 (
    echo [INFO] No temporary files to clean
) else (
    echo [SUCCESS] Cleaned %CLEANED% directories
)

:end
echo.
echo Environment management completed.