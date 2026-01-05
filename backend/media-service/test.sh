#!/bin/bash
echo "========================================"
echo "Running Media Service Tests"
echo "========================================"

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo ""
echo "Running tests..."
echo "Note: No tests implemented yet for Media Service"
mvn test

echo ""
echo "========================================"
echo "Tests completed"
echo "========================================"
