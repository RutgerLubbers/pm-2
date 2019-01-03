#!/usr/bin/env bash

path="/opt/p1-reader-hack"
mkdir -p "${path}"

format="%Y%m%d%H%M"
now=$(date +"${format}")

while true
do
  last_now=${now}

  file="${path}/data/p1-${now}.txt"
  if [[ ! -f "${file}" ]]
  then
    touch "${file}"
  fi

  while [[ "${now}" == "${last_now}" ]]
  do
    ${path}/p1-reader.py >> "${file}"
    sleep 2
    now=$(date +"${format}")
  done
  gzip "${file}"
done

