#!/usr/bin/env bash


for year in {2019..2020}
do
  for month in {1..12}
  do
    for day in {1..31}
    do
      printf -v date "%d%02d%02d" ${year} ${month} ${day}

      echo "${date}"
      zip -q -u -m zip/p1-${date}.zip data/p1-${date}*

    done
  done
done
