#!/bin/bash

echo "Setting system locale"

cp locale.sh /etc/default/locale
locale-gen en_US.UTF-8
