#!/bin/sh

JAVA_HOME=/usr/lib/jvm/Adoptjdk-11/jdk-11.0.2+9
MAVEN_OPTS=-Xmx512m
MAVEN_HOME=/usr/share/maven
export MAVEN_HOME

PATH_ADD=$MAVEN_HOME
PATH_ADD="$JAVA_HOME/bin:$PATH_ADD"

PATH="$PATH_ADD:$PATH"

echo "\nMaven build..."
mvn clean install -Dfile.encoding=UTF-8 -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dmaven.source.skip=true

echo "\nStart jar..."
WEB_MODULE=/home/vetesii/github/mq-error
JAVA_OPTS="-Xms128m -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -XX:MaxMetaspaceSize=128m"
JAVA_OPTS="$JAVA_OPTS -Dspring.config.location=file:$WEB_MODULE/src/main/env/local/mq-error.properties"

java $JAVA_OPTS -jar "$WEB_MODULE/target/mq-error.jar"
