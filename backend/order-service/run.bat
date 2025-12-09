@echo off
echo ========================================
echo Starting Order Service on port 8084
echo ========================================

REM Définir JAVA_HOME
set JAVA_HOME=E:\Java
set PATH=%JAVA_HOME%\bin;%PATH%

echo JAVA_HOME: %JAVA_HOME%
echo.

REM Démarrer le service
echo Starting Order Service...
java -jar target/order-service-1.0.0.jar

pause
