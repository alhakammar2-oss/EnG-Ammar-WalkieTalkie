#!/usr/bin/env sh

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

DEFAULT_JVM_OPTS=""

GRADLE_OPTS=""

DIRNAME=`dirname "$0"`
DIRNAME=`cd "$DIRNAME"; pwd -P`

GRADLE_HOME="$DIRNAME/gradle"

CLASSPATH="$GRADLE_HOME/wrapper/gradle-wrapper.jar"

exec java $DEFAULT_JVM_OPTS $GRADLE_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
