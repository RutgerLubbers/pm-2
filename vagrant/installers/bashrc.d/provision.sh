#!/usr/bin/env bash

mkdir ~vagrant/.bashrc.d

cat >> ~vagrant/.bashrc << EOF

# Source all files in .bashrc.d

shopt -s nullglob
shopt -s dotglob

files=(~/.bashrc.d/*)
if [ \${#files[@]} -gt 0 ]
then
  for config_file in "\${files[@]}"
  do
    PATH=~vagrant/bin:\${PATH} source \${config_file}
  done
fi

shopt -u nullglob
shopt -u dotglob

EOF
