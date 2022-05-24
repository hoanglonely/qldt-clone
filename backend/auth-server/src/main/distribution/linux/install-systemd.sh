#!/bin/bash

FS_USER=${dist.linux.user}
SERVICE_ID=${dist.service.id}

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
    BASE_PATH="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
    SOURCE="$(readlink "$SOURCE")"
    [[ $SOURCE != /* ]] && SOURCE="$BASE_PATH/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
BASE_PATH="$( cd -P "$( dirname "$SOURCE" )" && cd -P ../ && pwd )"

JAVA_OPTS="-server"
JAVA_OPTS="$JAVA_OPTS -Xms256m -Xmx1024m -XX:MaxPermSize=256m -Xss1024k"
JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC -XX:MaxGCPauseMillis=1500 -XX:GCTimeRatio=9 -XX:+DisableExplicitGC"
JAVA_OPTS="$JAVA_OPTS -Djava.io.tmpdir=$BASE_PATH/temp"
JAVA_OPTS="$JAVA_OPTS -Dlogging.path=$BASE_PATH/logs/"
JAVA_OPTS="$JAVA_OPTS -Dspring.config.additional-location=$BASE_PATH/conf/application.yml"
JAVA_OPTS="$JAVA_OPTS -Dspring.pid.file=$BASE_PATH/run/application.pid"

echo "Base path: $BASE_PATH"

echo "Checking user exist..."
# Check if user exists
if ! id -u $FS_USER > /dev/null 2>&1; then
    echo "    The user does not exist; execute below commands to create and try again:"
    echo "        mkdir -p /opt/$FS_USER"
    echo "        groupadd $FS_USER"
    echo "        useradd -M -s /bin/nologin -g $FS_USER -d /opt/$FS_USER $FS_USER"
    echo "        chgrp -R $FS_USER /opt/$FS_USER"
    echo "        chown -R $FS_USER /opt/$FS_USER"
    echo "        chmod g+rwx /opt/$FS_USER"
    echo "        mkdir -p /opt/$FS_USER/$SERVICE_ID"
    echo "        mv $BASE_PATH/* /opt/$FS_USER/$SERVICE_ID/"
    echo "        chgrp -R $FS_USER /opt/$FS_USER/$SERVICE_ID"
    echo "        chown -R $FS_USER /opt/$FS_USER/$SERVICE_ID"
    echo "        chmod g+rwx /opt/$FS_USER/$SERVICE_ID"
    exit 1
fi

FS_USER_HOME=$(eval echo "~$FS_USER")

echo "Service will run as User=$FS_USER"
echo "User home:$FS_USER_HOME"

if ! [[ "$BASE_PATH" == "$FS_USER_HOME"* ]]; then
    echo ""
    echo "WARNING: TO AVOID PERMISSION ERROR, PLEASE COPY APPLICATION TO HOME DIRECTORY OF USER '$FS_USER'"
    echo "        mkdir -p /opt/$FS_USER/$SERVICE_ID"
    echo "        mv $BASE_PATH/* /opt/$FS_USER/$SERVICE_ID/"
    echo "        chgrp -R $FS_USER /opt/$FS_USER/$SERVICE_ID"
    echo "        chown -R $FS_USER /opt/$FS_USER/$SERVICE_ID"
    echo "        chmod g+rwx /opt/$FS_USER/$SERVICE_ID"
    echo ""
fi

echo "Change owner to user $FS_USER"
chgrp -R $FS_USER "$BASE_PATH"
chmod g+rwx "$BASE_PATH"
chown -R $FS_USER "$BASE_PATH"

chgrp -R $FS_USER "$BASE_PATH/"*
chmod g+rwx "$BASE_PATH/"*
chown -R $FS_USER "$BASE_PATH/"*

SERVICE_FILE="/etc/systemd/system/$SERVICE_ID.service"
echo "Creating service file at: $SERVICE_FILE"

JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:/bin/java::")
echo "Using 'java' at '$JAVA_HOME/bin/java'"

CONTENT="[Unit]"
CONTENT="$CONTENT\nDescription=${dist.service.description}"
CONTENT="$CONTENT\nAfter=syslog.target"
CONTENT="$CONTENT\n"

CONTENT="$CONTENT\n[Service]"
CONTENT="$CONTENT\nUser=$FS_USER"
CONTENT="$CONTENT\nGroup=$FS_USER"
CONTENT="$CONTENT\nExecStart=$JAVA_HOME/bin/java $JAVA_OPTS -jar \"$BASE_PATH/bin/${finalBuildName}.jar\""
CONTENT="$CONTENT\nSuccessExitStatus=143"

CONTENT="$CONTENT\n"
CONTENT="$CONTENT\n[Install]"
CONTENT="$CONTENT\nWantedBy=multi-user.target"
echo -e "$CONTENT" > $SERVICE_FILE

echo "Reloading deamon"
systemctl daemon-reload

echo "Enable $SERVICE_ID service on reboot"
systemctl enable $SERVICE_ID

echo -e "Service installed at $SERVICE_FILE"
echo -e "JAVA_OPS can be configure at \"$SERVICE_FILE\""
echo -e "After update service file: Please run: 'systemctl daemon-reload'"
echo -e "Usage:service $SERVICE_ID start | stop | restart | status"
