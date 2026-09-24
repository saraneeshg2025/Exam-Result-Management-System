@echo off
echo ===============================================================
echo   Compiling Exam ^& Result Management System (Java 21)
echo ===============================================================

if not exist "bin" mkdir bin

javac -encoding UTF-8 -d bin src\main\java\com\examflow\model\*.java src\main\java\com\examflow\util\*.java src\main\java\com\examflow\service\*.java src\main\java\com\examflow\repository\*.java src\main\java\com\examflow\server\*.java src\main\java\com\examflow\cli\*.java src\main\java\com\examflow\Main.java

if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Compilation completed without errors.
) else (
    echo [FAILED] Compilation errors encountered.
)
