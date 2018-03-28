#!/bin/bash

# enter into ios project folder
HERE="$(dirname "$(test -L "$0" && readlink "$0" || echo "$0")")"
pushd ${HERE}/../src/ios/ > /dev/null

LINK='https://github.com/PDF417/pdf417-ios/releases/download/v7.0.0/pdf417-ios_v7.0.0.zip'
FILENAME='pdf417-ios.zip'

# check if Microblink framework and bundle already exist
wget --version > /dev/null 2>&1 || { echo "ERROR: couldn't download Microblink framework, install wget" &&  exit 1; }
wget -O "${FILENAME}" "${LINK}" -nv --show-progress || ( echo "ERROR: couldn't download Microblink framework, Something went while downloading framework from ${LINK}" && exit 1 )

echo "Unzipping ${FILENAME}"
unzip -v > /dev/null 2>&1 || { echo "ERROR: couldn't unzip Microblink framework, install unzip" && exit 1; }
unzip "${FILENAME}" >/dev/null 2>&1 && echo "Unzipped ${FILENAME}"

if [ -d 'Microblink.bundle' ] ; then
    rm -rf Microblink.bundle && echo "Removing Microblink.bundle"
fi

if [ -d 'Microblink.framework' ] ; then
    rm -rf Microblink.framework && echo "Removing Microblink.framework"
fi 

cd pdf417-ios || exit 1

mv -f Microblink.framework ../Microblink.framework
mv -f Microblink.bundle ../Microblink.bundle

cd ..

echo "Removing unnecessary files"

rm -rfv pdf417-ios >/dev/null 2>&1
rm "${FILENAME}" >/dev/null 2>&1

popd
