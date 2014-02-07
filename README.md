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
    
The build command will create the necesary sources and project files but the actuall build will fail for now because some manual setup described below is needed.
    
Create a new Android project in Eclipse from existing source found in (these sources will be created with the above phonegap command) `platforms/android/`. Also, create another Android project in Eclipse from existing source in `pdf417-phonegap/Pdf417/src/android/Pdf417MobiSdk` for the pdf417 sdk library project.

Finally, add a library project reference to Android pdf417 SDK in your phonegap Eclipse project by going to *Project properties -> Android -> Add* and selecting the `Pdf417/src/android/Pdf417MobiSdk` library project folder in Eclipse.

You can now build your project using Eclipse. To build using the cordova command line tools take a look at the [sample section](#Sample).

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

# add a reference to the pdf417 android library to the project
android update project --target 12 --path platforms/android/ --library ../../../pdf417-phonegap/Pdf417/src/android/Pdf417MobiSdk

# update the project build settings
android update project --name testphone --target 12 --path platforms/android/

# update the library build settings
android update lib-project --target 12 --path ../pdf417-phonegap/Pdf417/src/android/Pdf417MobiSdk/

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
    // Device plugin required to check device type, check http://docs.phonegap.com/en/3.0.0/cordova_device_device.md.html#Device for details
    var license = null;        
    if (device.platform == "Android") {
        // This license is only valid for package name "mobi.pdf417"
        license = "1c61089106f282473fbe6a5238ec585f8ca0c29512b2dea3b7c17b8030c9813dc965ca8e70c8557347177515349e6e";
    } else if (device.platform == "iPhone") {
        // This license key allows setting overlay views for this application ID: net.photopay.barcode.pdf417-sample
        license = "1672a675bc3f3697c404a87aed640c8491b26a4522b9d4a2b61ad6b225e3b390d58d662131708451890b33";
    }
    
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
        types, options, license
    );
```

For compatibility with older versions of the library this is also a valid way to initiate scan
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

The parameters for the scanner are a list of barcodes that the scanner should scan for (you can find this list) and a boolean telling the scanner whether to play the beep sound upon successfull recogintion (you can provide your own sound).

+ The list of supported barcodes is:
    + PDF417
    + QR Code
    + Code 128
    + Code 39
    + EAN 13
    + EAN 8
    + ITF
    + UPCA
    + UPCE