#!/bin/bash
echo "========================================"
echo "Starting Media Service on port 8083"
echo "========================================"

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "JAVA_HOME: $JAVA_HOME"
echo ""

echo "Starting Media Service..."
java -jar target/media-service-1.0.0.jar
