#!/bin/bash

if ! ls ../target/*.jar 1> /dev/null 2>&1; then
  echo "JAR file not found. Please build the project first."
  exit 1
fi

cp ../target/*jar ./app.jar
docker compose -f compose.yaml up -d

echo "Waiting for the application to start...."
sleep 5

pnpm test

docker compose down