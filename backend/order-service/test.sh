#!/bin/bash
echo "========================================"
echo "Running Order Service Tests"
echo "========================================"

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo ""
echo "Running tests..."
mvn test -Dtest=OrderServiceTest

echo ""
echo "========================================"
echo "Tests completed"
echo "========================================"
