#!/usr/bin/env bash
set -euo pipefail

# Build the React app and put it inside Spring Boot's static folder.
cd "$(dirname "$0")"

cd frontend
if [ -f package-lock.json ]; then
  npm ci
else
  npm install
fi
npm run build

cd ../backend
rm -rf src/main/resources/static
mkdir -p src/main/resources/static
cp -R ../frontend/dist/. src/main/resources/static/

if [ -f mvnw ]; then
  chmod +x mvnw
  ./mvnw clean package -DskipTests
else
  mvn clean package -DskipTests
fi
