/**
 * cordova is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) Matt Kane 2010
 * Copyright (c) 2011, IBM Corporation
 */


	var exec = require("cordova/exec");

	/**
	 * Constructor.
	 *
	 * @returns {Pdf417Scanner}
	 */
	function Pdf417Scanner() {

	};

/**
 * Types of barcodes supported (pass as array of desired barcode strings):
 *
 *	"PDF417"
 *	"QR Code"
 *	"Code 128"
 *	"Code 39"
 *	"EAN 13"
 *	"EAN 8"
 *	"ITF"
 *	"UPCA"
 *	"UPCE"
 *
 * Options properties:
 * 	- beep - determine whether to play the beep sound upon barcode recoginition (default is true)
 * 	- noDialog - determine if dialog is presented after successfull scan (default is false)
 * 	- removeOverlay - if license permits this, remove Pdf417.mobi logo overlay on scan activity (default is false)
 * 	- uncertain - Set this to true to scan even barcode not compliant with standards, For example, malformed PDF417 barcodes which were incorrectly encoded. Use only if necessary because it slows down the recognition process
 * 	- quietZone - Set this to true to scan barcodes which don't have quiet zone (white area) around it. Use only if necessary because it drastically slows down the recognition process.
 *	- highRes - Set to true if you want to always use highest possible camera resolution (enabled by default for all devices that support at least 720p camera preview frame size)
 *	- frontFace - Set to true to use front facing camera. Note that front facing cameras do not have autofocus support, so it will not be possible to scan denser and smaller codes.
 *  - customUI - Use custom UI activity
 *
 * License iOS - license key to enable all features (not required)
 * License Android - license key to enable all features (not required)
 */

	Pdf417Scanner.prototype.scan = function (successCallback, errorCallback, types, options, licenseiOs, licenseAndroid) {
		if (errorCallback == null) {
			errorCallback = function () {
			};
		}

		if (typeof errorCallback != "function") {
			console.log("Pdf417Scanner.scan failure: failure parameter not a function");
			return;
		}

		if (typeof successCallback != "function") {
			console.log("Pdf417Scanner.scan failure: success callback parameter must be a function");
			return;
		}

		exec(successCallback, errorCallback, 'Pdf417Scanner', 'scan', [types, options, licenseiOs, licenseAndroid]);
	};

	var pdf417Scanner = new Pdf417Scanner();
	module.exports = pdf417Scanner;

