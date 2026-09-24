@echo off
echo ===============================================================
echo   Apex ExamFlow - Exam ^& Result Management System (Java 21)
echo ===============================================================

call build.bat
if %ERRORLEVEL% NEQ 0 exit /b %ERRORLEVEL%

echo.
echo Starting ExamFlow Server on port 8085...
start http://localhost:8085/
java -cp bin com.examflow.Main 8085
