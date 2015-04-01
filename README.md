## Installation

First generate a empty project if needed:

    phonegap create <path> <package> <name>
    
> The shown instructions are for **PhoneGap**, the instructions for **Cordova** are practically the same, except for some slight command line argument differences.

Add the **pdf417** plugin to your project:

	cd <path_to_your_project>
	
    phonegap local plugin add <pdf417_plugin_path>

### Android

Add Android platform support to the project:

    phonegap local build android
    
### iOS

Add iOS plaform support to the project:

    phonegap local install ios
    
Open the generated Xcode project found in `platforms/ios/`.

Finally, add the iOS embedded framework to your project by draging and dropping the `Pdf417/src/ios/pdf417.emboeddedframework` folder onto the `Frameworks` group in Xcode.

## Sample

Here's a complete example of how to create and build a project for **Android** and **iOS** using **cordova** (you can substitute equivalent commands for **phonegap**):

```shell
# pull the plugin and sample application from Github
git clone git@github.com:PDF417/pdf417-phonegap.git

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
	
# open the project in xcode and add the framework reference described above (this step cannot be done via command line)

# build the project
cordova build ios
```

In **phonegap** CLI instead of "platform add" just request a build for the platform using "build android" or "build ios". You will have to do the manual steps (execute the commands starting with *android* for Android or the adding of the embedded framework in Xcode for iOS) described above to be able to do a successfull build.

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
	noDialog : true,
	removeOverlay :true,
	uncertain : false, //Recommended
	quietZone : false, //Recommended
	highRes : false, //Recommended
	frontFace : false
};

// Note that each platform requires its own license key

// This license key allows setting overlay views for this application ID: mobi.pdf417.demo
var licenseiOs = "YUY3-MHTT-COH4-SOQF-4M77-R6MN-Y73H-GIPF";

// This license is only valid for package name "mobi.pdf417.demo"
var licenseAndroid = "BTH7-L4JO-UI5T-JAFP-YSKX-BXZT-SDKE-LKIZ";    
    
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


+ Both license parameters must be provided (for **iOS** and **Android**) even if you do not plan to run the application on both platforms. The licenses that you do not have/use must be set to `null`.

+ For obtaining US Driver's license parsing result, see the sample code above, and usdl_keys.js javascript file which contains information about values which you can obtain from scanned USDL. 
```
