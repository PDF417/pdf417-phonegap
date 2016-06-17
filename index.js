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
         * Scan these barcode types
         * Available: "PDF417", "USDL", "QR Code", "Code 128", "Code 39", "EAN 13", "EAN 8", "ITF", "UPCA", "UPCE", "Aztec", "Data Matrix"
         */
        var types = ["PDF417", "QR Code"];

        /**
         * Initiate scan with options
         * NOTE: Some features are unavailable without a license
         * Obtain your key at http://pdf417.mobi
         */
        var options = {
            beep : true,  // Beep on
            noDialog : true, // Skip confirm dialog after scan
            uncertain : false, //Recommended
            quietZone : false, //Recommended
            highRes : false, //Recommended
            inverseScanning: false,
            frontFace : false
        };

        // Note that each platform requires its own license key

        // This license key allows setting overlay views for this application ID: mobi.pdf417.demo
        var licenseiOs = "JLP3VGMS-IYX25ZWG-6L4YCTRQ-FS3BOFFA-XDAFDYEF-S33EMK4Q-2L7RLW27-LE5OGEYN";

        // This license is only valid for package name "mobi.pdf417.demo"
        var licenseAndroid = "Y5AR6RJ4-PPA6ZDJ6-ABLKN4DE-XZEVSOLL-HLBOUBAE-AQCAIBAE-AQCAIBAE-AQCFKMFM";

        // This license is only valid for Product ID "e2994220-6b3d-11e5-a1d6-4be717ee9e23"
        var licenseWP8 = "5JKGDHZK-5WN4KMQO-6TZU3KDQ-I4YN67V5-XSN4FFS3-OZFAXHK7-EMETU6XD-EY74TM4T";

        scanButton.addEventListener('click', function() {    
            cordova.plugins.pdf417Scanner.scan(
            
           		// Register the callback handler
                function callback(scanningResult) {
                    
                    // handle cancelled scanning
                    if (scanningResult.cancelled == true) {
						resultDiv.innerHTML = "Cancelled!";
						return;
					}
					
					// Obtain list of recognizer results
					var resultList = scanningResult.resultList;
					
					// Iterate through all results
					for (var i = 0; i < resultList.length; i++) {

                        // Get individual resilt
						var recognizerResult = resultList[i];
                        if (recognizerResult.resultType == "Barcode result") {
                            // handle Barcode scanning result

                            var raw = "";
                            if (typeof(recognizerResult.raw) != "undefined" && recognizerResult.raw != null) {
                                raw = " (raw: " + hex2a(recognizerResult.raw) + ")";
                            }
                            resultDiv.innerHTML = "Data: " + recognizerResult.data +
                                               raw +
                                               " (Type: " + recognizerResult.type + ")";

                        } else if (recognizerResult.resultType == "USDL result") {
                            // handle USDL parsing result

                            var fields = recognizerResult.fields;

                            resultDiv.innerHTML = /** Personal information */
                                               "USDL version: " + fields[kPPStandardVersionNumber] + "; " +
                                                "Family name: " + fields[kPPCustomerFamilyName] + "; " +
                                                "First name: " + fields[kPPCustomerFirstName] + "; " +
                                                "Date of birth: " + fields[kPPDateOfBirth] + "; " +
                                                "Sex: " + fields[kPPSex] + "; " +
                                                "Eye color: " + fields[kPPEyeColor] + "; " +
                                                "Height: " + fields[kPPHeight] + "; " +
                                                "Street: " + fields[kPPAddressStreet] + "; " +
                                                "City: " + fields[kPPAddressCity] + "; " +
                                                "Jurisdiction: " + fields[kPPAddressJurisdictionCode] + "; " +
                                                "Postal code: " + fields[kPPAddressPostalCode] + "; " +

                                                /** License information */
                                                "Issue date: " + fields[kPPDocumentIssueDate] + "; " +
                                                "Expiration date: " + fields[kPPDocumentExpirationDate] + "; " +
                                                "Issuer ID: " + fields[kPPIssuerIdentificationNumber] + "; " +
                                                "Jurisdiction version: " + fields[kPPJurisdictionVersionNumber] + "; " +
                                                "Vehicle class: " + fields[kPPJurisdictionVehicleClass] + "; " +
                                                "Restrictions: " + fields[kPPJurisdictionRestrictionCodes] + "; " +
                                                "Endorsments: " + fields[kPPJurisdictionEndorsementCodes] + "; " +
                                                "Customer ID: " + fields[kPPCustomerIdNumber] + "; ";
                        }
					}
                },
                
                // Register the error callback
                function errorHandler(err) {
                    alert('Error: ' + err);
                },

                types, options, licenseiOs, licenseAndroid, licenseWP8
            );
        });

    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        console.log('Received Event: ' + id);
    }
};
