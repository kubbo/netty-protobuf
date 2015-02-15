#!/bin/bash

base_dir=$(dirname $0)/..
for file in ${base_dir}/lib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

CLASSPATH=$CLASSPATH:${base_dir}/config

#java -jar Main