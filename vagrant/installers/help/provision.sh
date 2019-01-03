#!/usr/bin/env bash

cp bin/* ~vagrant/bin
ln -sf /home/vagrant/bin/pleh /home/vagrant/bin/\?
chmod +x ~vagrant/bin/*
chown -R vagrant: ~vagrant/bin
