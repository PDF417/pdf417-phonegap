## Submodules
After cloning repository, make sure you clone also its submodules:

	git submodule update --init --recursive
	
Without this, Android version of plugin will not work (you will get error that `LibPdf417Mobi.aar` is missing.

## Installation

First generate a empty project if needed:

    cordova create <path> <package> <name>
    
> The shown instructions are for **Cordova**, the instructions for **PhoneGap** are practically the same, except for some slight command line argument differences.

Add the **pdf417** plugin to your project:

	cd <path_to_your_project>
	
    cordova plugin add <pdf417_plugin_path>

### Android

Add Android platform support to the project:

    cordova platform add android
    
### iOS

Add iOS plaform support to the project:

    cordova platform add ios
    
### Windows Phone 8.0 (Deprecated)

Add Windows Phone 8.0 support to the project:

	cordova platform add wp8

Copy `Pdf417/src/wp8/lib/Microblink.dll` file to `Plugins/mobi.Pdf417.Pdf417Scanner/` folder in your new project.

Support for Windows Phone 8.0 is no longer maintained.

## Sample

Here's a complete example of how to create and build a project for **Android**, **iOS** and **Windows Phone 8.0** using **cordova** (you can substitute equivalent commands for **phonegap**):

```shell
# pull the plugin and sample application from Github
git clone https://github.com/PDF417/pdf417-phonegap.git

# initialize and update submodules
git submodule init
git submodule update

# create a empty application
cordova create testcordova

cd testcordova

# add the pdf417 plugin
cordova plugin add ../pdf417-phonegap/Pdf417

# add android support to the project
cordova platform add android

# build the project, the binary will appear in the bin/ folder
cordova build android

# add ios support to the project
cordova platform add ios

# build the project
cordova build ios

# add windows phone 8.0 support to the project
cordova platform add wp8

# build the project
cordova build wp8

# copy the Microblink.dll file to location in project as described above
```

In **phonegap** CLI instead of "platform add" just request a build for the platform using "build android" or "build ios" or "build wp8". You will have to do the manual steps described above to be able to do a successfull build.

You can also use provided `initDemoApp.sh` script that will generate a demo app that uses the plugin:

```shell
./initDemoApp.sh
```

To run the script, you'll need BASH environment on Windows (Linux and MacOS use BASH by default).


## Usage

To use the plugin you call it in your Javascript code like the demo application:

```javascript

/**
 * Scan these barcode types
 * Available: "PDF417", "USDL", "QR Code", "Code 128", "Code 39", "EAN 13", "EAN 8", "ITF", "UPCA", "UPCE"
 */
var types = ["USDL", "QR Code"];

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
var licenseiOs = "RZNIT6NY-YUY2L44B-JY4C3TC7-LE5LFU2B-JOAF4FO3-L5MTVMWT-IFFYAXQV-3NPQQA4G";

// This license is only valid for package name "mobi.pdf417.demo"
var licenseAndroid = "UDPICR2T-RA2LGTSD-YTEONPSJ-LE4WWOWC-5ICAIBAE-AQCAIBAE-AQCAIBAE-AQCFKMFM";

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

					// Get individual result
					var recognizerResult = resultList[i];

					if (recognizerResult.resultType == "Barcode result") {
						// handle Barcode scanning result

						resultDiv.innerHTML = "Data: " + recognizerResult.data +
										   " (raw: " + hex2a(recognizerResult.raw) + ")" +
										   " (Type: " + recognizerResult.type + ")";

					} else if (recognizerResult.resultType == "USDL result") {
						// handle USDL parsing result

						var fields = recognizerResult.fields;

						resultDiv.innerHTML = /** Personal information */
										   "USDL version: " + fields[kPPAamvaVersionNumber] + "; " +
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
				alert('Error');
			},

			types, options, licenseiOs, licenseAndroid
		);
	});

```
+ Available barcode types for the scanner are:
    + PDF417
    + QR Code
    + Code 128
    + Code 39
    + EAN 13
    + EAN 8
    + ITF
    + UPCA
    + UPCE
    
+ Additionally, USDL parsing is available when types array contains "USDL" string.

+ The following options are available:
    + **beep** - *Boolean* - set to true to play beep sound after successful scan
    + **noDialog** - *Boolean* - set to true to show confirm dialog after successful scan (license required)
    + **removeOverlay** - *Boolean* - set to true to remove Pdf417.mobi logo overlay on scan (license required)
    + **uncertain** - *Boolean* - set to true to scan even barcode not compliant with standards. For example, malformed PDF417 barcodes which were incorrectly encoded. Use only if necessary because it slows down the recognition process
    + **quietZone** - *Boolean* - set to true to scan barcodes which don't have quiet zone (white area) around it. Use only if necessary because it drastically slows down the recognition process 
    + **highRes** - *Boolean* - Set to true if you want to always use highest possible camera resolution (enabled by default for all devices that support at least 720p camera preview frame size)
    + **frontFace** - *Boolean* - to use front facing camera. Note that front facing cameras do not have autofocus support, so it will not be possible to scan denser and smaller codes.


+ All license parameters must be provided (for **iOS** and **Android** and **WP8**) even if you do not plan to run the application on both platforms. The licenses that you do not have/use must be set to `null`.

+ For obtaining US Driver's license parsing result, see the sample code above, and usdl_keys.js javascript file which contains information about values which you can obtain from scanned USDL. 

## How to get started

- [Download](https://github.com/PDF417/pdf417-phonegap/archive/master.zip) PDF417.mobi PhoneGap SDK, and try the sample app for iOS, Android or Windows Phone 8.0.

Sample app is generated with a script

```shell
./initDemoApp.sh
```

To run iOS demo application open Xcode project Pdf417Demo.xcodeproj

To run Android demo application type

```shell
cordova run android
```
To run Windows Phone demo application open Visual Studio solution Pdf417Demo.sln

- [Generate](https://microblink.com/login?url=/customer/generatedemolicence) a **free demo license key** to start using the SDK in your app (registration required)

- Get information about pricing and licensing od [pdf417.mobi](http://pdf417.mobi/#pricing)
