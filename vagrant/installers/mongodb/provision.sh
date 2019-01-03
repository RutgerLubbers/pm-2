#!/usr/bin/env bash

echo "Installing MongoDB"
cat mongodb-public.key | apt-key add -
echo "deb [ arch=amd64,arm64 ] http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.6 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-3.6.list > /dev/null
apt-get update

apt-get install -y --allow-unauthenticated mongodb-org

echo "    Installing MongoDB service"
cp disable-transparent-hugetables /etc/init.d
chmod +x /etc/init.d/disable-transparent-hugetables
update-rc.d disable-transparent-hugetables defaults

systemctl enable mongod

cp mongod.conf /etc/mongod.conf
service mongod start
sleep 3
mongo create-root.js
mongo admin -u root -p root create-user.js
