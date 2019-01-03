#!/usr/bin/env bash
TXT_DIR="/vagrant/provisioning/next-steps"
OUT="/tmp/next-steps.tmp"

GREY="\\\\033[90m"
RED="\\\\033[91m"
GREEN="\\\\033[92m"
YELLOW="\\\\033[93m"
BLUE="\\\\033[94m"
PURPLE="\\\\033[95m"
CYAN="\\\\033[96m"
WHITE="\\\\033[97m"

function cat_file() {
  while IFS='' read -r line || [[ -n "$line" ]]; do
    line=$(echo "$line" | sed -e "s/\${YELLOW}/${YELLOW}/g")
    line=$(echo "$line" | sed -e "s/\${RED}/${RED}/g")
    line=$(echo "$line" | sed -e "s/\${WHITE}/${WHITE}/g")
    echo -e "${2}$line"
  done < "${1}"
}

function wide() {
  echo > "${OUT}"
  cat ${TXT_DIR}/${1}.header | figlet --font wideterm >> "${OUT}"
  cat_file "${OUT}" ${2:-"\033[91m"}
  rm "${OUT}"
}

function show_step() {
  wide "step-${1}"
  white  "${TXT_DIR}/step-${1}.txt"
}

function red() {
  cat_file ${1} "\033[91m"
}

function white() {
  cat_file ${1} "\033[97m"
}

echo > "${OUT}"
figlet --font mono9 "ALL DONE" >> "${OUT}"
red "${OUT}"
rm "${OUT}"
