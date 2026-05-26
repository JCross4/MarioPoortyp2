@echo off
REM Compile MarioPoorty Project
REM This script compiles all Java source files

echo.
echo ======================================
echo   Compiling MarioPoorty Project
echo ======================================
echo.

cd src\main\java

if not exist "..\..\..\target\classes" (
    echo Creating target\classes directory...
    mkdir "..\..\..\target\classes"
)

echo Compiling Java files...
javac Main/*.java -d "..\..\..\target\classes"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ======================================
    echo   Compilation Successful!
    echo ======================================
    echo.
    cd ..\..\..\..
) else (
    echo.
    echo ======================================
    echo   Compilation Failed!
    echo ======================================
    echo.
    cd ..\..\..\..
    pause
    exit /b 1
)
