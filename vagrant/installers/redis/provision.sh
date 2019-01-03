#!/bin/bash

#
# Add servers into redis-server.
#   The first server will get the port number configured, the second one will have the port number + 100 (ie 6379 -> 6479).
# Add sentinels to redis-sentinel.
#
# Do not forget to forward the redis-server ports to the outside world.
#

echo "Installing Redis Cluster"
apt-get -y install redis-server redis-tools redis-sentinel
service redis-server stop 2> /dev/null
service redis-sentinel stop 2> /dev/null
for svc in ` ls /lib/systemd/system/ | grep redis`; do
  service ${svc} stop 2> /dev/null
done
echo "  Setting up redis environment."
echo "vm.overcommit_memory = 1" > /etc/sysctl.d/61-redis.conf
sysctl -p /etc/sysctl.d/61-redis.conf > /dev/null
sed -i -e 's/exit 0//g' /etc/rc.local
echo "echo never > /sys/kernel/mm/transparent_hugepage/enabled" >> /etc/rc.local
echo "exit 0" >> /etc/rc.local
chmod +x /etc/rc.local
## Perform setup
echo never > /sys/kernel/mm/transparent_hugepage/enabled


rm -rf /lib/systemd/system/redis-server.service /lib/systemd/system/redis-sentinel.service /etc/init.d/redis-server /etc/init.d/redis-sentinel
rm -rf /etc/redis/redis.conf /etc/redis/sentinel.conf

echo "  Creating redis services."
function create_service {
  if [ $# -ne 2 ]; then
    echo create_service name template
    exit -1;
  fi
  echo "    Create service 'redis-${1}'."
  SERVICE_FILE=/lib/systemd/system/redis-${1}.service
  cp ${2} ${SERVICE_FILE}
  sed -i -e "s/SERVICE_NAME/${1}/g" ${SERVICE_FILE}

}

function enable_service {
  if [ $# -ne 1 ]; then
    echo enable_service name
    exit -1;
  fi
  echo "  Enable service 'redis-${1}'"
  systemctl enable redis-${1} 2> /dev/null
  systemctl start redis-${1} 2> /dev/null
}

function add_server_to_sentinel {
  if [ $# -lt 2 ]; then
    echo add_server_to_sentinel name port port
    exit -1;
  fi

  while IFS=" " read -r SENTINEL_NAME SENTINEL_PORT
  do
    echo "    Add cluster 'redis-cluster-${1}' to sentinel '${SENTINEL_NAME}'."
    FILE_NAME=${REDIS_CONF}/${SENTINEL_NAME}.conf
    echo "# $1" >> ${FILE_NAME}
    echo "sentinel monitor redis-cluster-${1} 127.0.0.1 $2 2" >> ${FILE_NAME}
    echo "sentinel down-after-milliseconds redis-cluster-${1} 5000" >> ${FILE_NAME}
    echo "sentinel failover-timeout redis-cluster-${1} 10000" >> ${FILE_NAME}
    echo "sentinel config-epoch redis-cluster-${1} 9" >> ${FILE_NAME}
    echo "" >> ${FILE_NAME}
  done <redis-sentinel
}

function create_server_config {
  if [ $# -lt 2 ]; then
    echo create_server_config name port [master_name master_port]
    exit -1;
  fi
  echo "  Create redis server '${1}' at '${2}'."
  FILE_NAME=${REDIS_CONF}/${1}.conf
  cp ${SERVER_CONFIG_TEMPLATE} ${FILE_NAME}
  sed -i -e "s/SERVER_NAME/${1}/g" -e "s/SERVER_PORT/${2}/g" ${FILE_NAME}
  mkdir -p /var/lib/redis/${1}

   if [ ! -z "${3}" ]; then
    echo "    '${1}' will be a slave of '${3}'."
    echo "slaveof 127.0.0.1 ${4}" >> ${FILE_NAME}
  fi

  create_service ${1} ${SERVER_SERVICE_TEMPLATE}
  echo "alias redis-cli-${1}='redis-cli -p ${2}'" >> /etc/profile.d/redis-alias.sh
}

function create_sentinel_config {
  if [ $# -lt 2 ]; then
    echo create_sentinel_config name port [master_port]
    exit -1;
  fi

  echo "  Create redis sentinel ${1} at ${2}."
  FILE_NAME=${REDIS_CONF}/${1}.conf
  cp ${SENTINEL_CONFIG_TEMPLATE} ${FILE_NAME}
  sed -i -e "s/SENTINEL_NAME/${1}/g" -e "s/SENTINEL_PORT/${2}/g" ${FILE_NAME}
  mkdir -p /var/lib/redis/${1}

  create_service ${1} ${SENTINEL_SERVICE_TEMPLATE}
}

if [ -e /etc/profile.d/redis-alias.sh ]; then
  rm /etc/profile.d/redis-alias.sh
fi

REDIS_CONF=/etc/redis

SERVER_CONFIG_TEMPLATE=/tmp/redis-server.conf.template
cp redis-server.conf.template ${SERVER_CONFIG_TEMPLATE}

SERVER_SERVICE_TEMPLATE=/tmp/redis-server.service.template
cp redis-server.service.template ${SERVER_SERVICE_TEMPLATE}

SENTINEL_CONFIG_TEMPLATE=/tmp/redis-sentinel.conf.template
cp redis-sentinel.conf.template ${SENTINEL_CONFIG_TEMPLATE}

SENTINEL_SERVICE_TEMPLATE=/tmp/redis-sentinel.service.template
cp redis-sentinel.service.template ${SENTINEL_SERVICE_TEMPLATE}

# CREATE SENTINELS
while IFS=" " read -r SENTINEL_NAME SENTINEL_PORT
do
  create_sentinel_config ${SENTINEL_NAME} ${SENTINEL_PORT}
done <redis-sentinel

# CREATE SERVERS + SLAVES
while IFS=" " read -r SERVER_NAME SERVER_PORT
do
  create_server_config ${SERVER_NAME}1 ${SERVER_PORT}
  add_server_to_sentinel ${SERVER_NAME} ${SERVER_PORT}

  SLAVE_PORT=$((SERVER_PORT + 100))
  create_server_config ${SERVER_NAME}2 ${SLAVE_PORT} ${SERVER_NAME}1 ${SERVER_PORT}
done <redis-server

chown -R redis:redis /etc/redis/*.conf
chown -R redis:redis /var/lib/redis

systemctl daemon-reload


while IFS=" " read -r NAME PORT
do
  enable_service ${NAME}1
  enable_service ${NAME}2
done <redis-server

while IFS=" " read -r  NAME PORT
do
  enable_service ${NAME}
done <redis-sentinel

