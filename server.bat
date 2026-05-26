@echo off
REM Start MarioPoorty Server
REM Usage: server.bat [port]

if "%1"=="" (
    set PORT=5000
) else (
    set PORT=%1
)

echo.
echo ======================================
echo   MarioPoorty Server
echo ======================================
echo.
echo Starting server on port %PORT%...
echo.
echo Clients can connect with:
echo   java -cp target\classes Main.MarioPoorty client localhost %PORT% PlayerName
echo.

cd /d "%~dp0target\classes"
java Main.MarioPoorty server %PORT%

pause
