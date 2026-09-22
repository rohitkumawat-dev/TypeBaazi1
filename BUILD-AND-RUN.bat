@echo off
setlocal
cd /d "%~dp0"
if not exist "backend\mvnw.cmd" (
 echo Copy this file and the backend and frontend folders into D:\TypeBaazi first.
 pause
 exit /b 1
)
if exist "%LOCALAPPDATA%\Programs\Eclipse Adoptium\jdk-17.0.20.101-hotspot\bin\java.exe" (
 set "JAVA_HOME=%LOCALAPPDATA%\Programs\Eclipse Adoptium\jdk-17.0.20.101-hotspot"
)
if defined JAVA_HOME set "PATH=%JAVA_HOME%\bin;%PATH%"
echo Building the React website...
cd frontend
call npm run build
if errorlevel 1 goto failure
cd ..
if not exist "backend\src\main\resources\static" mkdir "backend\src\main\resources\static"
xcopy "frontend\dist\*" "backend\src\main\resources\static\" /E /I /Y >nul
if errorlevel 1 goto failure
cd backend
echo Starting TypeBaazi. Wait for Started BackendApplication, then open http://localhost:8080
call mvnw.cmd spring-boot:run
if errorlevel 1 goto failure
pause
exit /b 0
:failure
echo.
echo The build or server stopped with an error. Send the error shown above.
pause
exit /b 1
