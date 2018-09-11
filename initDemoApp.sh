#!/bin/bash

APP_NAME="Pdf417Demo"

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

# copy www folder
cp  -f -r ../www .

# build app
cordova prepare

# how to run
echo "To run iOS demo application open Xcode project $APP_NAME.xcodeproj"
echo "To run Android demo application, position to $APP_NAME folder and type: cordova run android"
