#!/usr/bin/env bash

println "Installing 'do-install' and 'do-update'."

mkdir -p /home/vagrant/bin
cp bin/* /home/vagrant/bin
dos2unix -q /home/vagrant/bin/*
chmod +x /home/vagrant/bin/*

install_bin
