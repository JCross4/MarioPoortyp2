@echo off
REM Run MarioPoorty Demo (Local Mode)

echo.
echo ======================================
echo   MarioPoorty Demo
echo ======================================
echo.
echo Running local demo without server...
echo.

cd /d "%~dp0target\classes"
java Main.MarioPoorty demo

pause
