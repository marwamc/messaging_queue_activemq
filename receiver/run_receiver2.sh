#!/bin/bash

NUMRECEIVERS=$1;

## step 2 start receivers
echo; echo;
echo "Starting receivers..";
echo "Will run "$NUMRECEIVERS" JMS receivers"
#mvn clean package -e -X;
##cd  target;

RUNCMD="java -Dr=$NUMRECEIVERS -jar receiver-1.0.1.jar";
echo; echo "Run command: "$RUNCMD;
echo; echo;
cp target/receiver-1.0.0.jar target/receiver-1.0.1.jar
java -Dr=$NUMRECEIVERS -jar target/receiver-1.0.1.jar;

