#!/usr/bin/env bash
# install all software using the provisioning script
do-install locale
do-install swap
do-install apt-software
do-install cachefilesd
do-install etc-hosts

do-install bash-completion
do-install bashrc.d

do-install java11
do-install kafka

do-install help

do-install version

source /home/vagrant/bin/update-vagrant-version-file
