#!/bin/bash

# position to a relative path
HERE="$(dirname "$(test -L "$0" && readlink "$0" || echo "$0")")"
pushd $HERE >> /dev/null

# remove any existing code
rm -rf Pdf417Demo

# create a sample application
cordova create Pdf417Demo mobi.pdf417.demo Pdf417Demo

# enter into demo project folder
cd Pdf417Demo

# add the PhotoPay plugin
cordova plugin add ../Pdf417

# add ios and android support to the project
cordova platform add ios
cordova platform add android

# copy index.html and index.js
cp  -f ../index.html www/index.html
cp  -f ../index.js www/js/index.js

# build app
cordova build

# how to run
echo "To run iOS demo application open Xcode project Pdf417Demo.xcodeproj"
echo "To run Android demo application type cordova run android"
