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
 *	"License"
 *	"Code 128"
 *	"Code 39"
 *	"EAN 13"
 *	"EAN 8"
 *	"ITF"
 *	"UPCA"
 *	"UPCE"
 *	"Barcode"
 *
 * Beep is a boolean which tells whether to play the beep sound upon barcode recoginition (default is true)
 */

	Pdf417Scanner.prototype.scan = function (successCallback, errorCallback, types, beep) {
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

		exec(successCallback, errorCallback, 'Pdf417Scanner', 'scan', [types, beep]);
	};

	var pdf417Scanner = new Pdf417Scanner();
	module.exports = pdf417Scanner;

