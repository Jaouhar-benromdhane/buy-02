@echo off
echo ========================================
echo Building Order Service
echo ========================================

REM Définir JAVA_HOME et MAVEN_HOME
set JAVA_HOME=E:\Java
set MAVEN_HOME=E:\DevTools\maven-mvnd-1.0.3-windows-amd64\maven-mvnd-1.0.3-windows-amd64\mvn
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo JAVA_HOME: %JAVA_HOME%
echo MAVEN_HOME: %MAVEN_HOME%
echo.

REM Vérifier Java
echo Verifying Java installation...
java -version
echo.

REM Compiler
echo Compiling project...
"%MAVEN_HOME%\bin\mvn.cmd" clean package -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo ✅ BUILD SUCCESSFUL!
    echo ========================================
    echo JAR file: target/order-service-1.0.0.jar
    echo.
) else (
    echo.
    echo ========================================
    echo ❌ BUILD FAILED!
    echo ========================================
    echo Please check the error messages above.
    echo.
)

pause
