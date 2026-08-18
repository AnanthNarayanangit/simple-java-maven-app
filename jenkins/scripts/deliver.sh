#!/usr/bin/env bash
set -e

echo '====================================================='
echo '           DELIVER STAGE EXECUTION                   '
echo '====================================================='

MVN_CMD="mvn"
if [ -f "./mvnw" ]; then
    MVN_CMD="./mvnw"
fi

echo "Using Maven command: ${MVN_CMD}"

NAME=$(${MVN_CMD} -q -DforceStdout help:evaluate -Dexpression=project.artifactId)
VERSION=$(${MVN_CMD} -q -DforceStdout help:evaluate -Dexpression=project.version)
JAR_FILE="target/${NAME}-${VERSION}.jar"

echo "Target JAR: ${JAR_FILE}"

if [ ! -f "${JAR_FILE}" ]; then
    echo "JAR file not found, building package..."
    ${MVN_CMD} package -DskipTests
fi

echo ""
echo "--- 1. Testing Default Run ---"
java -jar "${JAR_FILE}"

echo ""
echo "--- 2. Testing Help Output ---"
java -jar "${JAR_FILE}" --help

echo ""
echo "--- 3. Testing Personalized Greeting ---"
java -jar "${JAR_FILE}" --name "Jenkins CI/CD Pipeline"

echo ""
echo "--- 4. Testing Calculator ---"
java -jar "${JAR_FILE}" --calc "128 * 4"

echo ""
echo "--- 5. Testing Statistics ---"
java -jar "${JAR_FILE}" --stats "10,25,30,45,50,90"

echo ""
echo "--- 6. Testing System Info ---"
java -jar "${JAR_FILE}" --sysinfo

echo ""
echo "====================================================="
echo " Deliver stage verification completed successfully!  "
echo "====================================================="
