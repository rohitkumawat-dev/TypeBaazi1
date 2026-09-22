@echo off
set "JAVA_HOME=C:\Users\Rohit\AppData\Local\Programs\Eclipse Adoptium\jdk-17.0.20.101-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"
call mvnw.cmd spring-boot:run
pause