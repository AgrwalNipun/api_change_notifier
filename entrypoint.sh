#!/bin/sh
set -e

echo "API Change Notifier Action starting..."
echo "Workspace: $(pwd)"

echo " Git info:"
git --version
git rev-parse HEAD

echo "Running Spring Boot app"
java -jar /app/app.jar "$@"
