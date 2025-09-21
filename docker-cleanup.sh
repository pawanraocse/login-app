#!/bin/bash
# docker-cleanup.sh: Dynamically clean up containers, volumes, and networks defined in docker-compose.yml
# Usage: ./docker-cleanup.sh

set -e

COMPOSE_FILE="docker-compose.yml"

if [ ! -f "$COMPOSE_FILE" ]; then
  echo "docker-compose.yml not found in current directory."
  exit 1
fi

# Get project name (default: folder name, can be overridden by COMPOSE_PROJECT_NAME)
# Compose default: lowercased folder name, dashes/underscores preserved
PROJECT_NAME=$(basename "$PWD" | tr '[:upper:]' '[:lower:]')
if [ -n "$COMPOSE_PROJECT_NAME" ]; then
  PROJECT_NAME="$COMPOSE_PROJECT_NAME"
fi

echo "Project name: $PROJECT_NAME"

echo "Stopping and removing containers and orphans..."
docker compose down --remove-orphans

# Remove volumes defined in compose
VOLUMES=$(docker compose config --volumes 2>/dev/null || true)
if [ -n "$VOLUMES" ]; then
  for VOLUME in $VOLUMES; do
    VOLUME_NAME="${PROJECT_NAME}_${VOLUME}"
    if docker volume ls --format '{{.Name}}' | grep -q "^$VOLUME_NAME$"; then
      echo "Removing volume $VOLUME_NAME..."
      docker volume rm "$VOLUME_NAME"
    else
      echo "Volume $VOLUME_NAME does not exist or already removed."
    fi
  done
else
  echo "No volumes defined in compose file."
fi

# Remove networks defined in compose
NETWORKS=$(docker compose config --networks 2>/dev/null || true)
if [ -n "$NETWORKS" ]; then
  for NETWORK in $NETWORKS; do
    NETWORK_NAME="${PROJECT_NAME}_${NETWORK}"
    if docker network ls --format '{{.Name}}' | grep -q "^$NETWORK_NAME$"; then
      echo "Removing network $NETWORK_NAME..."
      docker network rm "$NETWORK_NAME"
    else
      echo "Network $NETWORK_NAME does not exist or already removed."
    fi
  done
else
  echo "No networks defined in compose file."
fi

echo "Cleanup complete."
