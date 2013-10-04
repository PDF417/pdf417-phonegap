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