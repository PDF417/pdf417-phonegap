#!/bin/bash

APP_NAME="PDF417Demo"

# position to a relative path
HERE="$(dirname "$(test -L "$0" && readlink "$0" || echo "$0")")"
pushd $HERE > /dev/null

# remove any existing code
rm -rf $APP_NAME

# create a sample application
cordova create $APP_NAME mobi.pdf417.demo $APP_NAME

# enter into demo project folder
cd $APP_NAME

# add the PDF417 plugin
cordova plugin add ../Pdf417 --variable CAMERA_USAGE_DESCRIPTION="Camera permission is required for automated scanning"

# add ios, android support to the project
cordova platform add android@7
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
echo "To run iOS demo application open Xcode project $APP_NAME.xcodeproj"
echo "To run Android demo application, position to $APP_NAME folder and type: cordova run android"
