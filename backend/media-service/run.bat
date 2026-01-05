@echo off
echo ========================================
echo Starting Media Service on port 8083
echo ========================================

REM Définir JAVA_HOME
set JAVA_HOME=E:\Java
set PATH=%JAVA_HOME%\bin;%PATH%

echo JAVA_HOME: %JAVA_HOME%
echo.

REM Démarrer le service
echo Starting Media Service...
java -jar target/media-service-1.0.0.jar

pause
