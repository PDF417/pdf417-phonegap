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

Finally, add a library project reference to the Android pdf417 SDK by going to *Project properties -> Android -> Add* and selecting the `Pdf417/src/android/Pdf417MobiSdk` library project folder in Eclipse.

### iOS

Add iOS plaform support to the project:

    phonegap local install ios

Finally, add the iOS embedded framework to your project by draging and dropping the `Pdf417/src/ios/pdf417.emboeddedframework` folder onto the `Frameworks` group in Xcode.

## Usage

To use the plugin you call it in your js code like the demo application:

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