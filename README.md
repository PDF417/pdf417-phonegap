## Installation

First generate an empty project if needed:

    cordova create <path> <package> <name>
    
> The shown instructions are for **Cordova**, the instructions for **PhoneGap** are practically the same, except for some slight command line argument differences.

Add the **pdf417** plugin to your project:

	cd <path_to_your_project>
	
    cordova plugin add <pdf417_plugin_path>

### Android

Add Android platform support to the project:

    cordova platform add android
    
### iOS

If you want to add iOS as a platform for your application, you will need to install **unzip** and **wget**.
Currently cordova plugin uses a hook script, that runs before adding ios platform, to download pdf417 framework and bundle from github.

Add iOS plaform support to the project:

    cordova platform add ios
    
## Sample

Here's a complete example of how to create and build a project for **Android** and **iOS** using **cordova** (you can substitute equivalent commands for **phonegap**):

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
cordova platform add android@7

# build the project, the binary will appear in the bin/ folder
cordova build android

# add ios support to the project
cordova platform add ios

# build the project
cordova build ios

```

In **phonegap** CLI instead of "platform add" just request a build for the platform using "build android" or "build ios". You will have to do the manual steps described above to be able to do a successfull build.

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
// Valid until 2018-06-04
var licenseiOs = "sRwAAAEQbW9iaS5wZGY0MTcuZGVtbz/roBZ34ygXMQRMupTjSPXnoj0Mz1jPfk1iRX7f78Ux6a+pfXVyW0HCjPTxl5ocxgXWF66PTrtFUbJFCDUpyznreSWY4akvhvqVFfcTYgVEKjB+UqO6vPD5iIaUCaEYhF4dVmM=";

// This license is only valid for package name "mobi.pdf417.demo"
var licenseAndroid = "sRwAAAAQbW9iaS5wZGY0MTcuZGVtb2uCzTSwE5Pixw1pJL5UEN7nyXbOdXB61Ysy/sgAYt4SaB0T/g6JvisLn6HtB8LzLDmpFjULMxmB8iLsy3tFdHtMhLWOM6pr0tQmSLGyhrXfe6rVoHAxJtPrFEoCNTk4RjLltQ==";

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

            var resToShow = "";

            // Iterate through all results
            for (var i = 0; i < resultList.length; i++) {
                // Get individual resilt
                var recognizerResult = resultList[i];
                resToShow += "(Result type: " + recognizerResult.resultType + ") <br>"
                if (recognizerResult.resultType == "Barcode result") {
                    // handle Barcode scanning result
                    var raw = "";
                    if (typeof(recognizerResult.raw) != "undefined" && recognizerResult.raw != null) {
                        raw = " (raw: " + hex2a(recognizerResult.raw) + ")";
                    }
                    resToShow += "(Barcode type: " + recognizerResult.type + ")<br>"
                                 + "Data: " + recognizerResult.data + "<br>"
                                 + raw;
                } else if (recognizerResult.resultType == "USDL result") {
                    // handle USDL parsing result

                    var fields = recognizerResult.fields;

                    resToShow += /** Personal information */
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
                resToShow += "<br><br>";
            }
            resultDiv.innerHTML = resToShow;
        },

        // Register the error callback
        function errorHandler(err) {
            alert('Error: ' + err);
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
    + Aztec
    + Data Matrix
    
+ Additionally, USDL parsing is available when types array contains "USDL" string.

+ The following options are available:
    + **beep** - *Boolean* - set to true to play beep sound after successful scan
    + **noDialog** - *Boolean* - set to true to show confirm dialog after successful scan (license required)
    + **uncertain** - *Boolean* - set to true to scan even barcode not compliant with standards. For example, malformed PDF417 barcodes which were incorrectly encoded. Use only if necessary because it slows down the recognition process
    + **quietZone** - *Boolean* - set to true to scan barcodes which don't have quiet zone (white area) around it. Use only if necessary because it drastically slows down the recognition process 
    + **highRes** - *Boolean* - set to true if you want to always use highest possible camera resolution (enabled by default for all devices that support at least 720p camera preview frame size)
    + **inverseScanning** - *Boolean* - set to true if you want to enable scanning of barcodes with inverse intensity values (e.g. white barcode on black background)
    + **frontFace** - *Boolean* - to use front facing camera. Note that front facing cameras do not have autofocus support, so it will not be possible to scan denser and smaller codes.


+ All license parameters must be provided (for **iOS** and **Android**) even if you do not plan to run the application on both platforms. The licenses that you do not have/use must be set to `null`.

+ For obtaining US Driver's license parsing result, see the sample code above, and usdl_keys.js javascript file which contains information about values which you can obtain from scanned USDL. 

## How to get started

- [Download](https://github.com/PDF417/pdf417-phonegap/archive/master.zip) PDF417.mobi PhoneGap SDK, and try the sample app for iOS or Android.

Sample app is generated with a script

```shell
./initDemoApp.sh
```

To run iOS demo application open Xcode project Pdf417Demo.xcodeproj

To run Android demo application type

```shell
cd PDF417Demo
cordova run android
```
- [Generate](https://microblink.com/login?url=/customer/generatedemolicence) a **free demo license key** to start using the SDK in your app (registration required)

- Get information about pricing and licensing od [microblink.com](https://microblink.com/products/pdf417?type=blink_sales#contact-us-form)
