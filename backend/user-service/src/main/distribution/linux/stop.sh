#!/bin/bash

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do
  BASE_PATH="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$BASE_PATH/$SOURCE"
done
BASE_PATH="$( cd -P "$( dirname "$SOURCE" )" && cd -P ../ && pwd )"

EXECUTABLE="$BASE_PATH/bin/${finalBuildName}.jar"
PID=$(ps -aux | grep java | grep $EXECUTABLE | awk '{print $2}')
if [[ -z $PID ]];then
  echo "ERROR: Process $EXECUTABLE is not running"
  exit 1
fi

echo "Killing process with PID=$PID"
kill -9 $PID