#!/bin/bash

PORT=8201

type nohup >/dev/null 2>&1 || { echo >&2 "ERROR: nohup command is required but it's not installed.  Aborting."; exit 1; }

PORT_OPENED=$(netstat -an | fgrep tcp | fgrep LISTEN | fgrep :$PORT | wc -l)
if [[ $PORT_OPENED -ne 0 ]];then
  echo "ERROR: Port $PORT already in use"
  exit 1
fi

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do
  BASE_PATH="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$BASE_PATH/$SOURCE"
done
BASE_PATH="$( cd -P "$( dirname "$SOURCE" )" && cd -P ../ && pwd )"

EXECUTABLE="$BASE_PATH/bin/${finalBuildName}.jar"
PROCESS_STARTED=$(ps -aux | grep java | grep $EXECUTABLE | wc -l)
if [[ $PROCESS_STARTED -ne 0 ]];then
  echo "ERROR: Process $EXECUTABLE is already running"
  exit 1
fi

nohup $BASE_PATH/scripts/run.sh </dev/null >/dev/null 2>&1 &