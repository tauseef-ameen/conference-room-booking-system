#!/bin/bash

# Check if all required arguments are passed
if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <BASE_URL> <START_TIME> <END_TIME> <ROOM_ID>"
    echo "Example: $0 http://localhost:8081 2025-03-11T12:00 2025-03-11T13:00 22"
    exit 1
fi

# Assign arguments to variables
BASE_URL=$1
START_TIME=$2
END_TIME=$3
ROOM_ID=$4

# Function to make GET request
make_get_request() {
    RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL/available/$START_TIME/$END_TIME")
    BODY=$(echo "$RESPONSE" | sed '$d')      # Extract response body
    STATUS=$(echo "$RESPONSE" | tail -n1)    # Extract HTTP status

    echo "GET Request: $BASE_URL/available/$START_TIME/$END_TIME"
    echo "HTTP Status: $STATUS"
    echo "Response Body: $BODY"
    echo "---------------------------"
}

# Function to make POST request
make_post_request() {
    RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/reserve" \
      -H "accept: */*" \
      -H "Content-Type: application/json" \
      -d "{
      \"roomId\": $ROOM_ID,
      \"startTime\": \"$START_TIME\",
      \"endTime\": \"$END_TIME\"
    }")

    BODY=$(echo "$RESPONSE" | sed '$d')      # Extract response body
    STATUS=$(echo "$RESPONSE" | tail -n1)    # Extract HTTP status

    echo "POST Request: $BASE_URL/reserve"
    echo "HTTP Status: $STATUS"
    echo "Response Body: $BODY"
    echo "---------------------------"
}

# Execute requests
make_get_request  # First GET request
make_post_request # POST request
make_get_request  # Second GET request
