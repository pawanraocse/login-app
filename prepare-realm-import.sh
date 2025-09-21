#!/bin/bash
# Script to update Keycloak realm import file with current timestamp for user and client
REALM_FILE="keycloak-realm-import/dev-realm-realm.json"
TIMESTAMP=$(date +%s000)

jq --arg ts "$TIMESTAMP" '
  .users[0].createdTimestamp = ($ts | tonumber) |
  .clients[0].attributes["client.secret.creation.time"] = $ts
' "$REALM_FILE" > "${REALM_FILE}.tmp" && mv "${REALM_FILE}.tmp" "$REALM_FILE"
echo "Updated $REALM_FILE with current timestamp: $TIMESTAMP"

