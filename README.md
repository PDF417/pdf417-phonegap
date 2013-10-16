## Installation

First generate a empty project if needed:

    phonegap create <path> <package> <name>
    
> The shown instructions are for **PhoneGap**, the instructions for **Cordova** are practically the same, except for some slight command line argument differences.

Add the **pdf417** plugin to your project:

	cd <path_to_your_project>
	
    phonegap local plugin add <pdf417_plugin_path>

### Android

Add Android platform support to the project:

    phonegap local install android
    
Create a new Android project from existing source found in `platforms/android/`. Also, create another Android project from existing source in `pdf417-phonegap/Pdf417/src/android/Pdf417MobiSdk` for the library project.

Finally, add a library project reference to the Android pdf417 SDK in your project by going to *Project properties -> Android -> Add* and selecting the `Pdf417/src/android/Pdf417MobiSdk` library project folder in Eclipse.

### iOS

Add iOS plaform support to the project:

    phonegap local install ios
    
Open the generated Xcode project found in `platforms/ios/`.

Finally, add the iOS embedded framework to your project by draging and dropping the `Pdf417/src/ios/pdf417.emboeddedframework` folder onto the `Frameworks` group in Xcode.

## Sample

Here's a complete example of how to create and build a project for **Android** and **iOS** using **cordova**:

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

## Usage

To use the plugin you call it in your js code like the demo application:

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