#!/bin/bash
echo "========================================"
echo "Building User Service"
echo "========================================"

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "JAVA_HOME: $JAVA_HOME"
echo ""

echo "Compiling project..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "✅ BUILD SUCCESSFUL!"
    echo "========================================"
    echo "JAR file: target/user-service-1.0.0.jar"
    echo ""
else
    echo ""
    echo "========================================"
    echo "❌ BUILD FAILED!"
    echo "========================================"
    echo "Please check the error messages above."
    echo ""
fi
