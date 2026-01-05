@echo off
echo ========================================
echo Running Product Service Tests
echo ========================================

REM Définir environnement
set JAVA_HOME=E:\Java
set MAVEN_HOME=E:\DevTools\maven-mvnd-1.0.3-windows-amd64\maven-mvnd-1.0.3-windows-amd64\mvn
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo.
echo Running tests...
"%MAVEN_HOME%\bin\mvn.cmd" test -Dtest=ProductServiceTest

echo.
echo ========================================
echo Tests completed
echo ========================================
pause
