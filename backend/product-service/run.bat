@echo off
echo ========================================
echo Starting Product Service on port 8082
echo ========================================

REM Définir JAVA_HOME
set JAVA_HOME=E:\Java
set PATH=%JAVA_HOME%\bin;%PATH%

echo JAVA_HOME: %JAVA_HOME%
echo.

REM Démarrer le service
echo Starting Product Service...
java -jar target/product-service-1.0.0.jar

pause
