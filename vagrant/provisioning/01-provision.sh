#!/usr/bin/env bash

set -e
export DEBIAN_FRONTEND=noninteractive
export CONFIG_FILE=vagrant-box.conf

function warn {
  echo "$@" 1>&2
}

function no_error_on {
  # grey "  (ignore errors on '${@}')"
  "$@" 2>/dev/null || true
}

# Update the apt mirror to something in the Netherlands for speed
echo "Setting APT mirror to Netherlands"
sed -i -e "s#http://archive.ubuntu.com/ubuntu/#http://nl.archive.ubuntu.com/ubuntu/#g" /etc/apt/sources.list

# Checking for apt-cacher proxy
DEFAULT_GW=`netstat -rn | grep '^0.0.0.0' | awk '{print $2}'`

if [[ ${DEFAULT_GW} =~ ^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+$ ]]
then
  echo "Found default gateway: ${DEFAULT_GW}"

	if nc -z "${DEFAULT_GW}" 3142 -w 5; then
		echo "Configuring APT to use apt-cacher proxy"
		echo "APT proxy configuration stored in file /etc/apt/apt.conf.d/01proxy"
		echo "Acquire::http::proxy \"http://${DEFAULT_GW}:3142\";" > /etc/apt/apt.conf.d/01proxy
	else
	  echo "#Acquire::http::proxy \"http://${DEFAULT_GW}:3142\";" > /etc/apt/apt.conf.d/01proxy
		echo "Could not find a local apt-get proxy."
	fi
fi
echo ""


# Start of the actual provisioning script
sed -i 's/^mesg n/tty -s \&\& mesg n/g' /root/.profile
source /root/.profile

echo "Installing 'dos2unix'."
apt-get -y update
apt-get -y install dos2unix toilet

TMPDIR=$(mktemp -d)
cp /vagrant/${CONFIG_FILE}.defaults "${TMPDIR}"
dos2unix -q ${TMPDIR}/${CONFIG_FILE}.defaults
source ${TMPDIR}/${CONFIG_FILE}.defaults

if [[ -f /vagrant/${CONFIG_FILE} ]]
then
    cp "/vagrant/${CONFIG_FILE}" "${TMPDIR}"
    dos2unix -q ${TMPDIR}/${CONFIG_FILE}
    source ${TMPDIR}/${CONFIG_FILE}
fi

echo "Installing installer script for 'vagrant'."
if [[ -d /home/vagrant/bin ]]
then
  rm -rf /home/vagrant/bin
fi
mkdir -p /home/vagrant/bin
cp /installers/do-install/bin/* /home/vagrant/bin
ln -sf /home/vagrant/bin/do-install /home/vagrant/bin/do-update
ln -sf /home/vagrant/bin/do-install /home/vagrant/bin/vg-update
dos2unix -q /home/vagrant/bin/*
chmod +x /home/vagrant/bin/*

echo "Installing installer script for 'root'."
mkdir -p /root/bin
ln -sf /home/vagrant/bin/* /root/bin/
echo "PATH=/root/bin:\${PATH}" >> /root/.bashrc
export PATH=/root/bin:${PATH}
