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

# add the PDF417 plugin
cordova plugin add ../Pdf417

# add ios and android support to the project
cordova platform add android
cordova platform add ios

# copy index.html, index.js and usdl_keys.js
cp  -f ../index.html www/index.html
cp  -f ../index.js www/js/index.js
cp  -f ../usdl_keys.js www/js/usdl_keys.js

# add logo
cp  -f ../logo.png www/img/logo.png

# build app
cordova build

# how to run
echo "To run iOS demo application open Xcode project Pdf417Demo.xcodeproj"
echo "To run Android demo application type cordova run android"
