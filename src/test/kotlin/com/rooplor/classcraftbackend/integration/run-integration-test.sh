#!/bin/bash

docker compose -f compose.yaml up -d

# Run integration tests
echo "Running integration tests..."
curl -X GET http://127.0.0.1:8080/api/user

docker compose down