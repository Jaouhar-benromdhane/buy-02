#!/bin/bash
echo "========================================"
echo "Starting Product Service on port 8082"
echo "========================================"

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "JAVA_HOME: $JAVA_HOME"
echo ""

echo "Starting Product Service..."
java -jar target/product-service-1.0.0.jar
