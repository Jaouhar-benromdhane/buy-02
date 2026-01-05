#!/bin/bash
echo "========================================"
echo "Running Product Service Tests"
echo "========================================"

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo ""
echo "Running tests..."
mvn test -Dtest=ProductServiceTest

echo ""
echo "========================================"
echo "Tests completed"
echo "========================================"
