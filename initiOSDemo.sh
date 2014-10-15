#!/bin/bash

# position to a relative path
HERE="$(dirname "$(test -L "$0" && readlink "$0" || echo "$0")")"
pushd $HERE >> /dev/null

# remove any existing code
rm -rf Pdf417iOSDemo

# create a sample application
cordova create Pdf417iOSDemo net.photopay.demo Pdf417iOSDemo

# enter into demo project folder
cd Pdf417iOSDemo

# add the PhotoPay plugin
cordova plugin add ../Pdf417

# add ios support to the project
cordova platform add ios

# copy index.html and index.js
cp  -f ../index.html www/index.html
cp  -f ../index.js www/js/index.js

# build app
cordova build >> /dev/null

# open Xcode project
open platforms/ios/Pdf417iOSDemo.xcodeproj 

# return to original path
popd >> /dev/null