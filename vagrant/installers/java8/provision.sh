#!/bin/bash

echo "Installing Java 8"
add-apt-repository -y ppa:webupd8team/java 2> /dev/null
apt-get update
echo debconf shared/accepted-oracle-license-v1-1 select true | sudo debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | sudo debconf-set-selections

mkdir -p /var/cache/oracle-jdk8-installer
cp wgetrc /var/cache/oracle-jdk8-installer/

apt-get -y install oracle-java8-installer oracle-java8-set-default

disable-apt-proxy
apt-get -y install oracle-java8-unlimited-jce-policy
enable-apt-proxy
