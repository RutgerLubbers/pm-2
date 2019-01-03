#!/usr/bin/env bash

# Setup a fixed number of Kafka brokers.
# If necessary, the specified Kafka version is downloaded and installed under /opt/kafka.
# For each broker instance, a properties file is created along with a
# systemd unit file, a logging location under /var/log/kafka and a storage
# location under /var/lib/kafka.

# Exported variables so they can be used in envsubst
export KAFKA_DIR=/opt/kafka/
export KAFKA_VERSION=2.0.0
NUM_BROKERS=3

KAFKA_CONFIG_BASE=/etc/kafka
KAFKA_LOGS_BASE=/var/lib/kafka
KAFKA_LOGGING_BASE=/var/log/kafka

function cleanup {
    rm -rf ${KAFKA_DIR}
    rm -rf ${KAFKA_LOGS_BASE}
    rm -rf ${KAFKA_LOGGING_BASE}
    find /lib/systemd/system -name "*kafka-*.service" -exec rm {} \;
    systemctl daemon-reload
}

function createConfig {
    BROKER_ID=$1
    CONFIG_FILE=${KAFKA_CONFIG_BASE}/server-${BROKER_ID}.properties
    cp ${KAFKA_CONFIG_BASE}/server.properties ${CONFIG_FILE}
    sed -i -r -e 's/(broker.id=).*/\1'${BROKER_ID}'/g' ${CONFIG_FILE}
    sed -i -r -e 's/(log.dirs=).*/\1\/var\/lib\/kafka\/server-'${BROKER_ID}'/g' ${CONFIG_FILE}
    sed -i -r -e 's/(num.partitions=).*/\12/g' ${CONFIG_FILE}
    echo >> ${CONFIG_FILE}
    echo "# Added by install script" >> ${CONFIG_FILE}
    echo listeners=PLAINTEXT://:${BROKER_ID}9092 >> ${CONFIG_FILE}
}

function configureSchemaRegistry {
    CONFIG_FILE=/etc/schema-registry/schema-registry.properties
    # Comment out the zookeeper connection and enable the broker connection, on the right port
    sed -i -r -e 's/^(kafkastore.connection.url)/#\1/g' \
     -e 's/^#(kafkastore.bootstrap.servers=PLAINTEXT:\/\/localhost:)/\11/g' \
     -e 's/^(listeners=http:\/\/0.0.0.0:)[0-9]*/\119091/' ${CONFIG_FILE}
}

function setupBrokers {
    for ((BROKER_ID = 1; BROKER_ID <= ${NUM_BROKERS}; BROKER_ID++))
    do
        # Export for envsubst
        yellow Setting up broker ${BROKER_ID}
        export BROKER_ID
        # Broker configuration
        echo -n configuration.......
        createConfig ${BROKER_ID}
        echo ok

        # Broker logs
        echo -n broker logs.........
        BROKER_LOGS_DIR=${KAFKA_LOGS_BASE}/server-${BROKER_ID}
        mkdir -p ${BROKER_LOGS_DIR}
        chown cp-kafka ${BROKER_LOGS_DIR}
        echo ok

        # Logging
        echo -n broker logging......
        export LOG_DIR=/var/log/kafka/server-${BROKER_ID}
        mkdir -p /var/log/kafka/server-${BROKER_ID}
        chown -R cp-kafka /var/log/kafka/server-${BROKER_ID}
        systemctl daemon-reload
        echo ok

        # systemd unit
        echo -n systemd unit file...
        UNIT_FILE=/lib/systemd/system/confluent-kafka-${BROKER_ID}.service
        if [ -f ${UNIT_FILE} ]; then
            systemctl stop confluent-kafka-${BROKER_ID} 2>&1 >> /dev/null
        fi
        envsubst < systemd/kafka.service.template > ${UNIT_FILE}
        echo ok

        echo -n starting broker.....
        systemctl daemon-reload
        systemctl enable confluent-kafka-${BROKER_ID} 2>&1 >> /dev/null
        systemctl start confluent-kafka-${BROKER_ID}
        echo ok
    done
}

# Remove zookeeper and cleanup if the kafka user still exists
# Used to migrate to confluent setup
if [ -n "$(id kafka 2>/dev/null)" ]; then
    systemctl stop zookeeper.service 2>&1 > /dev/null | true
    apt-get remove -y --purge zookeeper
    apt-get --purge -y autoremove
    userdel kafka
fi

cleanup

wget -qO - https://packages.confluent.io/deb/5.0/archive.key | sudo apt-key add -

sudo add-apt-repository "deb [arch=amd64] https://packages.confluent.io/deb/5.0 stable main"

disable-apt-proxy
apt-get update && sudo apt-get -y install confluent-platform-oss-2.11
enable-apt-proxy

# Make Zookeeper start automatically
systemctl enable confluent-zookeeper.service 2>&1 > /dev/null
# Start zookeeper
systemctl start confluent-zookeeper.service


configureSchemaRegistry

setupBrokers

# Make schema registry start automatically
systemctl enable confluent-schema-registry.service 2>&1 > /dev/null
# Start schema registry
systemctl start confluent-schema-registry

install_bin
