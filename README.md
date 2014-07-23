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

    // This sample license key removes overlay views for this application ID: net.photopay.barcode.pdf417-sample
    var licenseiOs = "1672a675bc3f3697c404a87aed640c8491b26a4522b9d4a2b61ad6b225e3b390d58d662131708451890b33";

    // This sample license is only valid for package name "mobi.pdf417"
    var licenseAndroid = "1c61089106f282473fbe6a5238ec585f8ca0c29512b2dea3b7c17b8030c9813dc965ca8e70c8557347177515349e6e";     
    
    cordova.plugins.pdf417Scanner.scanWithOptions(
        // Register the callback handler
        function callback(data) {
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


+ The following options are available:
    + **beep** - *Boolean* - set to true to play beep sound after successful scan
    + **noDialog** - *Boolean* - set to true to show confirm dialog after successful scan (license required)
    + **removeOverlay** - *Boolean* - set to true to remove Pdf417.mobi logo overlay on scan (license required)
    + **uncertain** - *Boolean* - set to true to scan even barcode not compliant with standards. For example, malformed PDF417 barcodes which were incorrectly encoded. Use only if necessary because it slows down the recognition process
    + **quietZone** - *Boolean* - set to true to scan barcodes which don't have quiet zone (white area) around it. Use only if necessary because it drastically slows down the recognition process 
    + **highRes** - *Boolean* - Set to true if you want to always use highest possible camera resolution (enabled by default for all devices that support at least 720p camera preview frame size)
    + **frontFace** - *Boolean* - to use front facing camera. Note that front facing cameras do not have autofocus support, so it will not be possible to scan denser and smaller codes.


+ Both license parameters must be provided (for **iOS** and **Android**) even if you do not plan to run the application on both platforms. The licenses that you do not have/use must be set to `null`.

In the previous versions of the plugin you could start a scan without extra options (this approach is deprecated now):

```javascript
cordova.exec(
	// Register the callback handler
	function callback(data) {
		//alert("got result " + data.data + " type " + data.type);
		if (data.cancelled == true) {
			resultDiv.innerHTML = "Cancelled!";
		} else {
			resultDiv.innerHTML = hex2a(data.raw) + " (" + data.type + ")";
		}
	},
	// Register the errorHandler
	function errorHandler(err) {
		alert('Error');
	},
	"Pdf417Scanner", //Service (plugin name) 
	"scan", //Action
	[ ["PDF417", "QR Code"], false ] //We want qr codes and pdf417 scanned with the beep sound off
);
```
