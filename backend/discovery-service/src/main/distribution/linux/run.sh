#!/bin/bash

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  BASE_PATH="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$BASE_PATH/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
BASE_PATH="$( cd -P "$( dirname "$SOURCE" )" && cd -P ../ && pwd )"

echo "Base path: $BASE_PATH"

JAVA_OPTS="-server"
JAVA_OPTS="$JAVA_OPTS -Xms256m -Xmx1024m"
JAVA_OPTS="$JAVA_OPTS -XX:MaxPermSize=256m -Xss1024k"
JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC -XX:MaxGCPauseMillis=1500 -XX:GCTimeRatio=9 -XX:+DisableExplicitGC"
JAVA_OPTS="$JAVA_OPTS -Djava.io.tmpdir=$BASE_PATH/temp"
JAVA_OPTS="$JAVA_OPTS -Dlogging.config=$BASE_PATH/conf/logback.xml"
JAVA_OPTS="$JAVA_OPTS -Dlogging.path=$BASE_PATH/logs/"
JAVA_OPTS="$JAVA_OPTS -Dspring.config.additional-location=$BASE_PATH/conf/application.yml"
JAVA_OPTS="$JAVA_OPTS -Dspring.pid.file=$BASE_PATH/run/application.pid"

java $JAVA_OPTS -jar "$BASE_PATH/bin/${finalBuildName}.jar"