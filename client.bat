@echo off
REM Start MarioPoorty Client
REM Usage: client.bat [host] [port] [name]

if "%1"=="" (
    set HOST=localhost
) else (
    set HOST=%1
)

if "%2"=="" (
    set PORT=5000
) else (
    set PORT=%2
)

if "%3"=="" (
    set NAME=Jugador
) else (
    set NAME=%3
)

echo.
echo ======================================
echo   MarioPoorty Client
echo ======================================
echo.
echo Connecting to server...
echo Host: %HOST%
echo Port: %PORT%
echo Player: %NAME%
echo.

cd /d "%~dp0target\classes"
java Main.MarioPoorty client %HOST% %PORT% %NAME%

pause
