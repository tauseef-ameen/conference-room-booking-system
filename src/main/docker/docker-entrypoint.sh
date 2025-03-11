#!/bin/sh
set -e

echo 'Starting conference room booking Spring Boot Application'
exec java $JAVA_OPTS -jar ${APP_HOME}/$ARTIFACT_NAME
