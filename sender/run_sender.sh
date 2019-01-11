#!/bin/bash

NUM2SEND=$1; NUMSENDERS=$2;

echo; echo; 
echo "Starting sender..";
mvn clean package -e -X;
cd  target;

SENDCMD="java -Dn="$NUMSENDERS" -Ds="$NUM2SEND" -jar sender-1.0.0.jar";
echo; echo;
echo "Sender CMD: "$SENDCMD;
eval $SENDCMD;

