#!/bin/bash

NUMRECEIVERS=$1;

killall java;

## step1 start broker
echo "Starting broker.."
activemq console &


## step 2 start receivers
echo; echo;
echo "Starting receivers..";
echo "Will run "$NUMRECEIVERS" JMS receivers"
mvn clean package -e -X;
##cd  target;

RUNCMD="java -Dr=$NUMRECEIVERS -jar receiver-1.0.0.jar";
echo; echo "Run command: "$RUNCMD;
echo; echo;
java -Dr=$NUMRECEIVERS -jar target/receiver-1.0.0.jar;

