#!/usr/bin/env bash

println "Installing bash completion"
apt-get -y install bash-completion

cp bash_completion.d/* /etc/bash_completion.d
