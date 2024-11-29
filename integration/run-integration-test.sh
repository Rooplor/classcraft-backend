#!/bin/bash

if ! command -v pnpm &> /dev/null; then
  echo "pnpm not found. Installing pnpm..."
  npm install -g pnpm
fi

if [ ! -d "node_modules" ]; then
  echo "node_modules directory not found. Running pnpm install..."
  pnpm install
fi

if ! ls ../target/*.jar 1> /dev/null 2>&1; then
  echo "JAR file not found. Please build the project first."
  exit 1
fi

cp ../target/*jar ./app.jar
docker compose -f compose.yaml up -d

echo "Waiting for the application to start...."
sleep 10

pnpm test integration/src/devAuthControllerIntegration.test.js
pnpm test

docker compose down