#!/bin/bash

echo "Installing MariaDB"

export DEBIAN_FRONTEND=noninteractive

sudo apt-get install software-properties-common
sudo apt-key adv --recv-keys --keyserver hkp://keyserver.ubuntu.com:80 0xF1656F24C74CD1D8
sudo add-apt-repository 'deb [arch=amd64,i386,ppc64el] http://mirror.i3d.net/pub/mariadb/repo/10.2/ubuntu xenial main'

# Install the apt packages
DATABASE_PASSWORD=loadgen
# Install MySQL Server in a Non-Interactive mode. Default root password will be "root"
echo "mysql-server-5.6 mysql-server/root_password password ${DATABASE_PASSWORD}" | sudo debconf-set-selections
echo "mysql-server-5.6 mysql-server/root_password_again password ${DATABASE_PASSWORD}" | sudo debconf-set-selections

apt-get update
apt-get -y remove --purge -y mariadb-client mariadb-server
apt-get -y autoremove
apt-get -y install mariadb-client mariadb-server

mkdir -p /etc/mysql/mariadb.conf.d
cp my.cnf /etc/mysql
cp mariadb.conf.d/* /etc/mysql/mariadb.conf.d

# Set the MariaDB listen address to all network interfaces
# sed -i -e 's/^bind-address.*/bind-address            = 0.0.0.0/g' /etc/mysql/my.cnf
systemctl restart mariadb


# Remove the test database, change the root password and set the user to mysql_native_password-type login
cat << EOF | mysql -u root -h localhost --password="${DATABASE_PASSWORD}"
DELETE FROM mysql.user WHERE User <> 'root';
UPDATE mysql.user SET Host = '%' WHERE User = 'root' AND Host = 'localhost';
UPDATE mysql.user SET plugin = 'mysql_native_password' WHERE User = 'root';

DROP DATABASE IF EXISTS test;
DELETE FROM mysql.db WHERE Db = 'test' OR Db = 'test\\_%';
FLUSH PRIVILEGES;
EOF

# From now on, we must log in with this command: mysql -u root -h localhost --password=loadgen

# Create the loadgen database and owner/connect users
cat << EOF | mysql -u root -h localhost --password="${DATABASE_PASSWORD}"
DROP DATABASE IF EXISTS loadgen;
CREATE DATABASE loadgen;
DROP USER IF EXISTS loadgenown;
DROP USER IF EXISTS loadgencon;
CREATE USER 'loadgenown'@'%' IDENTIFIED BY 'loadgenown';
GRANT ALL ON loadgen.* TO 'loadgenown'@'%';
CREATE USER 'loadgencon'@'%' IDENTIFIED BY 'loadgencon';
GRANT SELECT ON loadgen.* TO 'loadgencon'@'%';
GRANT INSERT ON loadgen.* TO 'loadgencon'@'%';
GRANT UPDATE ON loadgen.* TO 'loadgencon'@'%';
GRANT DELETE ON loadgen.* TO 'loadgencon'@'%';
EOF
