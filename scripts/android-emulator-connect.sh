#!/usr/bin/env bash
set -e -E -o pipefail

if ! [ -x "$(command -v adb)" ]; then
  echo 'Error: adb is not installed.' >&2
  exit 1
fi

deviceCount=$(echo $( adb devices -l | grep "device " | wc -l ))
if [[ $deviceCount == 0 ]]; then
    echo "You must have a device connected";
    exit -1
fi

if [[ $deviceCount == 1 ]]; then
    deviceNum=1
else
    count=0
    adb devices -l | grep "device " | while read line ; do
        count=$((count+1))
        echo "($count) $line"
    done
    echo -n "Device to connect to: "
    read deviceNum
fi

device=$(adb devices -l | grep "device " | head "-$deviceNum" | tail -1 | cut -d' ' -f1)

if [[ $device =~ ^emulator ]]; then
    adb -s $device forward tcp:60000 tcp:8080
    IP="127.0.0.1:60000"
else
    IP="$(adb -s $device shell ip route | awk '{print $9}' | tail -1):8080"
fi

# Check if IP is not blank
if [[ $IP = *[!\ ]* ]]; then
    open "https://mockzilla-cloud.firebaseapp.com/?deviceIp=$IP"
else
    echo "Sorry something didn't work"
fi