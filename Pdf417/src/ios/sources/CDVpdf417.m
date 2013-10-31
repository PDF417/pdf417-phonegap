//
//  pdf417Plugin.m
//  CDVpdf417
//
//  Created by Jurica Cerovec, Marko Mihovilic on 10/01/13.
//  Copyright (c) 2013 Racuni.hr. All rights reserved.
//

/**
 * Copyright (c)2013 Racuni.hr d.o.o. All rights reserved.
 *
 * ANY UNAUTHORIZED USE OR SALE, DUPLICATION, OR DISTRIBUTION
 * OF THIS PROGRAM OR ANY OF ITS PARTS, IN SOURCE OR BINARY FORMS,
 * WITH OR WITHOUT MODIFICATION, WITH THE PURPOSE OF ACQUIRING
 * UNLAWFUL MATERIAL OR ANY OTHER BENEFIT IS PROHIBITED!
 * THIS PROGRAM IS PROTECTED BY COPYRIGHT LAWS AND YOU MAY NOT
 * REVERSE ENGINEER, DECOMPILE, OR DISASSEMBLE IT.
 */

#import "CDVpdf417.h"

@interface CDVPlugin () <PPBarcodeDelegate>

@property (nonatomic, retain) CDVInvokedUrlCommand* lastCommand;

- (void)presentCameraViewController:(UIViewController*)cameraViewController isModal:(BOOL)isModal;

- (void)dismissCameraViewControllerModal:(BOOL)isModal;

@end

@implementation CDVpdf417

@synthesize lastCommand;

- (void)scan:(CDVInvokedUrlCommand*)command {
    
    [self setLastCommand:command];
    
    // Check if barcode scanning is supported
    NSError *error;
    if ([PPBarcodeCoordinator isScanningUnsupported:&error]) {
        NSString *messageString = [error localizedDescription];
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Warning"
                                                        message:messageString
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    
    // Create object which stores pdf417 framework settings
    NSMutableDictionary* coordinatorSettings = [[NSMutableDictionary alloc] init];
    
    NSArray* types = [command argumentAtIndex:0];
    
    // Set YES/NO for scanning pdf417 barcode standard (default YES)
    [coordinatorSettings setValue:[NSNumber numberWithBool:[types containsObject:@"PDF417"]] forKey:kPPRecognizePdf417Key];
    // Set YES/NO for scanning qr code barcode standard (default NO)
    [coordinatorSettings setValue:[NSNumber numberWithBool:[types containsObject:@"QR Code"]] forKey:kPPRecognizeQrCodeKey];
    // Set YES/NO for scanning code 128 barcode standard (default NO)
    [coordinatorSettings setValue:[NSNumber numberWithBool:[types containsObject:@"Code 128"]] forKey:kPPRecognizeCode128Key];
    // Set YES/NO for scanning code 39 barcode standard (default NO)
    [coordinatorSettings setValue:[NSNumber numberWithBool:[types containsObject:@"Code 39"]] forKey:kPPRecognizeCode39Key];
    // Set YES/NO for scanning EAN 8 barcode standard (default NO)
    [coordinatorSettings setValue:[NSNumber numberWithBool:[types containsObject:@"EAN 8"]] forKey:kPPRecognizeEAN8Key];
    // Set YES/NO for scanning EAN 13 barcode standard (default NO)
    [coordinatorSettings setValue:[NSNumber numberWithBool:[types containsObject:@"EAN 13"]] forKey:kPPRecognizeEAN13Key];
    // Set YES/NO for scanning ITF barcode standard (default NO)
    [coordinatorSettings setValue:[NSNumber numberWithBool:[types containsObject:@"ITF"]] forKey:kPPRecognizeITFKey];
    // Set YES/NO for scanning UPCA barcode standard (default NO)
    [coordinatorSettings setValue:[NSNumber numberWithBool:[types containsObject:@"UPCA"]] forKey:kPPRecognizeUPCAKey];
    // Set YES/NO for scanning UPCE barcode standard (default NO)
    [coordinatorSettings setValue:[NSNumber numberWithBool:[types containsObject:@"UPCE"]] forKey:kPPRecognizeUPCEKey];
    
    // Set only one resolution mode
    //    [coordinatorSettings setValue:[NSNumber numberWithBool:YES] forKey:kPPUseVideoPreset640x480];
    //    [coordinatorSettings setValue:[NSNumber numberWithBool:YES] forKey:kPPUseVideoPresetMedium];
    //    [coordinatorSettings setValue:[NSNumber numberWithBool:YES] forKey:kPPUseVideoPresetHigh];
    [coordinatorSettings setValue:[NSNumber numberWithBool:YES] forKey:kPPUseVideoPresetHighest];
    
    /** Set the license key */
    //    [coordinatorSettings setValue:@"Enter_License_Key_Here" forKey:kPPLicenseKey];
    
    // present modal (recommended and default) - make sure you dismiss the view controller when done
    // you also can set this to NO and push camera view controller to navigation view controller
    [coordinatorSettings setValue:[NSNumber numberWithBool:YES] forKey:kPPPresentModal];
    // You can set orientation mask for allowed orientations, default is UIInterfaceOrientationMaskAll
    [coordinatorSettings setValue:[NSNumber numberWithInt:UIInterfaceOrientationMaskAll] forKey:kPPHudOrientation];
    
    // Define the sound filename played on successful recognition
    if ([command arguments].count < 2 || [command argumentAtIndex:1]) {
        NSString* soundPath = [[NSBundle mainBundle] pathForResource:@"beep" ofType:@"mp3"];
        [coordinatorSettings setValue:soundPath forKey:kPPSoundFile];
    }
    
    // Allocate the recognition coordinator object
    PPBarcodeCoordinator *coordinator = [[PPBarcodeCoordinator alloc] initWithSettings:coordinatorSettings];
    
    // Create camera view controller
    UIViewController *cameraViewController = [coordinator cameraViewControllerWithDelegate:self];
    
    // present it modally
    cameraViewController.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
    [self presentCameraViewController:cameraViewController isModal:YES];
}


- (void)returnResult:(PPScanningResult *)data cancelled:(BOOL)cancelled {
    NSMutableDictionary* resultDict = [[NSMutableDictionary alloc] init];
    [resultDict setObject:[NSNumber numberWithInt: (cancelled ? 1 : 0)] forKey:@"Cancelled"];
    
    if (data != nil) {
    	NSString* textData = [[NSString alloc] initWithData:[data data]
                                                    encoding:NSUTF8StringEncoding];

    	if (textData) {
    		[resultDict setObject:textData forKey:@"data"];
    	}

        [resultDict setObject:[data toUrlDataString] forKey:@"raw"];
        [resultDict setObject:[PPScanningResult toTypeName:data.type] forKey:@"type"];
    } else {
        NSLog(@"Result is nil!");
    }
    
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                            messageAsDictionary:resultDict];
    
    /*
     NSString* js = [result toSuccessCallbackString:[[self lastCommand] callbackId]];
     
     [self writeJavascript:js];
     */
    
    [self.commandDelegate sendPluginResult:result callbackId:self.lastCommand.callbackId];
    
    [self dismissCameraViewControllerModal:YES];
}

/**
 * Method presents a modal view controller and uses non deprecated method in iOS 6
 */
- (void)presentCameraViewController:(UIViewController*)cameraViewController isModal:(BOOL)isModal {
    if (isModal) {
        cameraViewController.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
        if ([self respondsToSelector:@selector(presentViewController:animated:completion:)]) {
            [[self viewController] presentViewController:cameraViewController animated:YES completion:nil];
        } else {
            [[self viewController] presentModalViewController:cameraViewController animated:YES];
        }
    } else {
        [[[self viewController] navigationController] pushViewController:cameraViewController animated:YES];
    }
}

/**
 * Method dismisses a modal view controller and uses non deprecated method in iOS 6
 */
- (void)dismissCameraViewControllerModal:(BOOL)isModal {
    if (isModal) {
        if ([self respondsToSelector:@selector(dismissViewControllerAnimated:completion:)]) {
            [[self viewController] dismissViewControllerAnimated:YES completion:nil];
        } else {
            [[self viewController] dismissModalViewControllerAnimated:YES];
        }
    }
}

- (void)returnError:(NSString*)message {
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                messageAsString:message];
    /*
     NSString* js = [result toErrorCallbackString:[[self lastCommand] callbackId]];
     
     [self writeJavascript:js];
     */
    
    [self.commandDelegate sendPluginResult:result callbackId:self.lastCommand.callbackId];
    
    [[self viewController] dismissModalViewControllerAnimated:YES];
}

#pragma mark -
#pragma mark PhotoPay delegate methods

- (void)cameraViewControllerWasClosed:(UIViewController *)cameraViewController {
    [self dismissCameraViewControllerModal:YES];
}

- (void)cameraViewController:(UIViewController *)cameraViewController obtainedResult:(PPScanningResult *)result {
    
    NSString *message = [[NSString alloc] initWithData:[result data] encoding:NSUTF8StringEncoding];
    
    if (message == nil) {
        message = [[NSString alloc] initWithData:[result data] encoding:NSASCIIStringEncoding];
    }
    
    NSLog(@"Barcode text:\n%@", message);
    
    NSString* type = [PPScanningResult toTypeName:[result type]];
    
    NSLog(@"Barcode type:\n%@", type);
    
    [self returnResult:result cancelled:(result == nil)];
    
    [self dismissCameraViewControllerModal:YES];
}

@end
