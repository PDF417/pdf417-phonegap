/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

// implement your decoding as you need it, this just does ASCII decoding
function hex2a(hex) {
    var str = '';
    for (var i = 0; i < hex.length; i += 2) {
        str += String.fromCharCode(parseInt(hex.substr(i, 2), 16));
    }
    return str;
}

var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicity call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.receivedEvent('deviceready');
        
        var resultDiv = document.getElementById('resultDiv');

        /**
        * Simple scan example
        **/
        scanButton.addEventListener('click', function() {    
            cordova.plugins.pdf417Scanner.scan(
                // Register the callback handler
                function callback(data) {
                    //alert("got result " + data.data + " type " + data.type);
                    if (data.cancelled == true) {
						resultDiv.innerHTML = "Cancelled!";
					} else if (data.resultList && data.resultList.length > 1) {
                        // More than 1 element in results
                        resultDiv.innerHTML = data.resultList.length + " results";
                    } else {
						resultDiv.innerHTML = "Data: " + data.data + " (raw: " + hex2a(data.raw) + ") (Type: " + data.type + ")";
					}
                },
                // Register the errorHandler
                function errorHandler(err) {
                    alert('Error');
                },
                [ ["PDF417", "QR Code"], false ] //We want qr codes and pdf417 scanned with the beep sound off
            );
        });
        
        /**
        * Scan these barcode types
        * Available: "PDF417", "QR Code", "Code 128", "Code 39", "EAN 13", "EAN 8", "ITF", "UPCA", "UPCE"
        **/
        var types = ["PDF417", "QR Code"];

        /**
        * Initiate scan with options
        * NOTE: Some features are unavailable without a license
        * Obtain your key at http://pdf417.mobi
        **/
        var options = {
            beep : true,
            noDialog : true,
            removeOverlay :true,
            uncertain : false, //Recommended
            quietZone : false, //Recommended
            highRes : false, //Recommended
            frontFace : false
        };

        // Note that each platform requires its own license key

        // This license key allows setting overlay views for this application ID: net.photopay.barcode.pdf417-sample
        var licenseiOs = "Y2QM-2NW3-RYBA-LJ64-UCNU-LIWQ-WBHS-ABQJ";

        // This license is only valid for package name "mobi.pdf417"
        var licenseAndroid = "F2XY-Y5PC-GCLU-3HCJ-EPKQ-QNPI-X5PG-HEQ5";

        scanWithOptionsButton.addEventListener('click', function() {    
            cordova.plugins.pdf417Scanner.scanWithOptions(
                // Register the callback handler
                function callback(data) {
                    //alert("got result " + data.data + " type " + data.type);
                    if (data.cancelled == true) {
                        resultDiv.innerHTML = "Cancelled!";
                    } else {
                        resultDiv.innerHTML = "Data: " + data.data + " (raw: " + hex2a(data.raw) + ") (Type: " + data.type + ")";
                    }
                },
                // Register the errorHandler
                function errorHandler(err) {
                    alert('Error');
                },
                types, options, licenseiOs, licenseAndroid
            );
        });

        /**
        * Initiate scan with custom UI
        * NOTE: Some features are unavailable without a license
        * Obtain your key at http://pdf417.mobi
        **/
        var optionsCustomUI = {
            beep : true,
            noDialog : true,
            removeOverlay :true,
            uncertain : false, //Recommended
            quietZone : false, //Recommended
            highRes : false, //Recommended
            frontFace : false,
            customUI : true
        };

        scanWithCustomUIButton.addEventListener('click', function() {    
            cordova.plugins.pdf417Scanner.scanWithOptions(
                // Register the callback handler
                function callback(data) {
                    //alert("got result " + data.data + " type " + data.type);
                    if (data.cancelled == true) {
                        resultDiv.innerHTML = "Cancelled!";
                    } else {
                        resultDiv.innerHTML = "Data: " + data.data + " (raw: " + hex2a(data.raw) + ") (Type: " + data.type + ")";
                    }
                },
                // Register the errorHandler
                function errorHandler(err) {
                    alert('Error');
                },
                types, optionsCustomUI, licenseiOs, licenseAndroid
            );
        });
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        console.log('Received Event: ' + id);
    }
};
