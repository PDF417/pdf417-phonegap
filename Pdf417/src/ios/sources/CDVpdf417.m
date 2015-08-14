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

#import <MicroBlink/MicroBlink.h>

@interface CDVPlugin () <PPScanDelegate>

@property (nonatomic, retain) CDVInvokedUrlCommand* lastCommand;

@end

@implementation CDVpdf417

@synthesize lastCommand;

- (BOOL)shouldUsePdf417RecognizerForTypes:(NSArray *)types {
    return [types containsObject:@"PDF417"];
}

- (PPPdf417RecognizerSettings *)pdf417RecognizerSettingsWithOptions:(NSDictionary *)options
                                                              types:(NSArray *)types {

    PPPdf417RecognizerSettings *pdf417RecognizerSettings = [[PPPdf417RecognizerSettings alloc] init];

    // Set this to true to scan barcodes which don't have quiet zone (white area) around it
    // Use only if necessary because it slows down the recognition process
    id quietZone = [options objectForKey:@"quietZone"];
    pdf417RecognizerSettings.allowNullQuietZone = (quietZone && [quietZone boolValue]);

    // Set this to true to scan even barcode not compliant with standards
    // For example, malformed PDF417 barcodes which were incorrectly encoded
    // Use only if necessary because it slows down the recognition process
    id uncertain = [options objectForKey:@"uncertain"];
    pdf417RecognizerSettings.scanUncertain = (uncertain && [uncertain boolValue]);

    return pdf417RecognizerSettings;
}

- (BOOL)shouldUseUsdlRecognizerForTypes:(NSArray *)types {
    return [types containsObject:@"USDL"];
}

- (PPUsdlRecognizerSettings *)usdlRecognizerSettingsWithOptions:(NSDictionary *)options
                                                          types:(NSArray *)types {

    PPUsdlRecognizerSettings *usdlRecognizerSettings = [[PPUsdlRecognizerSettings alloc] init];

    return usdlRecognizerSettings;
}

- (BOOL)shouldUseBarDecoderRecognizerForTypes:(NSArray *)types {

    BOOL code128 = [types containsObject:@"Code 128"];
    BOOL code39 = [types containsObject:@"Code 39"];

    return (code128 || code39);
}

- (PPBarDecoderRecognizerSettings *)barDecoderRecognizerSettingsWithOptions:(NSDictionary *)options
                                                                      types:(NSArray *)types {

    PPBarDecoderRecognizerSettings *barDecoderRecognizerSettings = [[PPBarDecoderRecognizerSettings alloc] init];

    BOOL code128 = [types containsObject:@"Code 128"];
    BOOL code39 = [types containsObject:@"Code 39"];

    barDecoderRecognizerSettings.scanCode128 = code128;
    barDecoderRecognizerSettings.scanCode39 = code39;

    // Set this to YES to allow scanning barcodes with inverted intensities
    // (i.e. white barcodes on black background)
    id scanInverse = [options objectForKey:@"inverseScanning"];
    barDecoderRecognizerSettings.scanInverse = (scanInverse && [scanInverse boolValue]);

    return barDecoderRecognizerSettings;
}

- (BOOL)shouldUseZxingRecognizerForTypes:(NSArray *)types {

    BOOL aztec = [types containsObject:@"Aztec"];
    BOOL dataMatrix = [types containsObject:@"Data Matrix"];
    BOOL ean8 = [types containsObject:@"EAN 8"];
    BOOL ean13 = [types containsObject:@"EAN 13"];
    BOOL itf = [types containsObject:@"ITF"];
    BOOL qrcode = [types containsObject:@"QR Code"];
    BOOL upca = [types containsObject:@"UPCA"];
    BOOL upce = [types containsObject:@"UPCE"];

    return (qrcode || ean8 || ean13 || itf || upca || upce || aztec || dataMatrix);
}

- (PPZXingRecognizerSettings *)zxingRecognizerSettingsWithOptions:(NSDictionary *)options
                                                            types:(NSArray *)types {

    PPZXingRecognizerSettings *zxingRecognizerSettings = [[PPZXingRecognizerSettings alloc] init];

    BOOL aztec = [types containsObject:@"Aztec"];
    BOOL dataMatrix = [types containsObject:@"Data Matrix"];
    BOOL ean8 = [types containsObject:@"EAN 8"];
    BOOL ean13 = [types containsObject:@"EAN 13"];
    BOOL itf = [types containsObject:@"ITF"];
    BOOL qrcode = [types containsObject:@"QR Code"];
    BOOL upca = [types containsObject:@"UPCA"];
    BOOL upce = [types containsObject:@"UPCE"];

    zxingRecognizerSettings.scanAztec = aztec;
    zxingRecognizerSettings.scanCode128 = NO;
    zxingRecognizerSettings.scanCode39 = NO;
    zxingRecognizerSettings.scanDataMatrix = dataMatrix;
    zxingRecognizerSettings.scanEAN13 = ean13;
    zxingRecognizerSettings.scanEAN8 = ean8;
    zxingRecognizerSettings.scanITF = itf;
    zxingRecognizerSettings.scanQR = qrcode;
    zxingRecognizerSettings.scanUPCA = upca;
    zxingRecognizerSettings.scanUPCE = upce;

    // Set this to YES to allow scanning barcodes with inverted intensities
    // (i.e. white barcodes on black background)
    id scanInverse = [options objectForKey:@"inverseScanning"];
    zxingRecognizerSettings.scanInverse = (scanInverse && [scanInverse boolValue]);

    return zxingRecognizerSettings;
}

- (PPCoordinator *)coordinatorWithError:(NSError**)error {

    /** 0. Check if scanning is supported */

    if ([PPCoordinator isScanningUnsupported:error]) {
        return nil;
    }

    NSArray* types = [self.lastCommand argumentAtIndex:0];

    NSDictionary* options = nil;

    if ([self.lastCommand arguments].count >= 2) {
        options = [self.lastCommand argumentAtIndex:1];
    }


    /** 1. Initialize the Scanning settings */

    // Initialize the scanner settings object. This initialize settings with all default values.
    PPSettings *settings = [[PPSettings alloc] init];

    id highRes = [options valueForKey:@"highRes"];
    if (highRes && [highRes boolValue]) {
        settings.cameraSettings.cameraPreset = PPCameraPresetMax;
    } else {
        settings.cameraSettings.cameraPreset = PPCameraPresetOptimal;
    }

    // Set front facing camera if requested
    id frontFace = [options objectForKey:@"frontFace"];
    if (frontFace && [frontFace boolValue]) {
        settings.cameraSettings.cameraType = PPCameraTypeFront;
    }


    /** 2. Setup the license key */

    // Visit www.microblink.com to get the license key for your app
    settings.licenseSettings.licenseKey = [self.lastCommand argumentAtIndex:2];

    /**
     * 3. Set up what is being scanned. See detailed guides for specific use cases.
     * Here's an example for initializing PDF417 scanning
     */

    // Add PDF417 Recognizer setting to a list of used recognizer settings
    if ([self shouldUsePdf417RecognizerForTypes:types]) {
        [settings.scanSettings addRecognizerSettings:[self pdf417RecognizerSettingsWithOptions:options types:types]];
    }

    if ([self shouldUseZxingRecognizerForTypes:types]) {
        [settings.scanSettings addRecognizerSettings:[self zxingRecognizerSettingsWithOptions:options types:types]];
    }

    if ([self shouldUseUsdlRecognizerForTypes:types]) {
        [settings.scanSettings addRecognizerSettings:[self usdlRecognizerSettingsWithOptions:options types:types]];
    }

    if ([self shouldUseBarDecoderRecognizerForTypes:types]) {
        [settings.scanSettings addRecognizerSettings:[self barDecoderRecognizerSettingsWithOptions:options types:types]];
    }

    // To specify we want to perform recognition of other barcode formats, initialize the ZXing recognizer settings
    PPZXingRecognizerSettings *zxingRecognizerSettings = [[PPZXingRecognizerSettings alloc] init];
    zxingRecognizerSettings.scanQR = YES; // we use just QR code

    // Add ZXingRecognizer setting to a list of used recognizer settings
    [settings.scanSettings addRecognizerSettings:zxingRecognizerSettings];

    /** 4. Initialize the Scanning Coordinator object */

    PPCoordinator *coordinator = [[PPCoordinator alloc] initWithSettings:settings];
    
    return coordinator;
}

- (void)scan:(CDVInvokedUrlCommand*)command {
    
    [self setLastCommand:command];

    /** Instantiate the scanning coordinator */
    NSError *error;
    PPCoordinator *coordinator = [self coordinatorWithError:&error];

    /** If scanning isn't supported, present an error */
    if (coordinator == nil) {
        NSString *messageString = [error localizedDescription];
        [[[UIAlertView alloc] initWithTitle:@"Warning"
                                    message:messageString
                                   delegate:nil
                          cancelButtonTitle:@"OK"
                          otherButtonTitles:nil, nil] show];

        return;
    }
    
    /** Allocate and present the scanning view controller */
    UIViewController<PPScanningViewController>* scanningViewController = [coordinator cameraViewControllerWithDelegate:self];

    /** You can use other presentation methods as well */
    [[self viewController] presentViewController:scanningViewController animated:YES completion:nil];
}

- (void)setDictionary:(NSMutableDictionary*)dict withPdf417RecognizerResult:(PPPdf417RecognizerResult*)data {

    if ([data stringUsingGuessedEncoding]) {
        [dict setObject:[data stringUsingGuessedEncoding] forKey:@"data"];
    }
    
    [dict setObject:[PPRecognizerResult urlStringFromData:[data data]] forKey:@"raw"];
    [dict setObject:@"PDF417" forKey:@"type"];
    [dict setObject:@"Barcode result" forKey:@"resultType"];
}

- (void)setDictionary:(NSMutableDictionary*)dict withZXingRecognizerResult:(PPZXingRecognizerResult*)data {

    if ([data stringUsingGuessedEncoding]) {
        [dict setObject:[data stringUsingGuessedEncoding] forKey:@"data"];
    }

    [dict setObject:[PPRecognizerResult urlStringFromData:[data data]] forKey:@"raw"];
    [dict setObject:[PPZXingRecognizerResult toTypeName:data.barcodeType] forKey:@"type"];
    [dict setObject:@"Barcode result" forKey:@"resultType"];
}

- (void)setDictionary:(NSMutableDictionary*)dict withBarDecoderRecognizerResult:(PPBarDecoderRecognizerResult*)data {

    if ([data stringUsingGuessedEncoding]) {
        [dict setObject:[data stringUsingGuessedEncoding] forKey:@"data"];
    }

    [dict setObject:[PPRecognizerResult urlStringFromData:[data data]] forKey:@"raw"];
    [dict setObject:[PPBarDecoderRecognizerResult toTypeName:data.barcodeType] forKey:@"type"];
    [dict setObject:@"Barcode result" forKey:@"resultType"];
}

- (void)setDictionary:(NSMutableDictionary*)dict withUsdlResult:(PPUsdlRecognizerResult*)usdlResult {
    [dict setObject:[usdlResult getAllStringElements] forKey:@"fields"];
    [dict setObject:@"USDL result" forKey:@"resultType"];
}

- (void)returnResults:(NSArray *)results cancelled:(BOOL)cancelled {

    NSMutableDictionary* resultDict = [[NSMutableDictionary alloc] init];
    [resultDict setObject:[NSNumber numberWithInt: (cancelled ? 1 : 0)] forKey:@"cancelled"];

    NSMutableArray *resultArray = [[NSMutableArray alloc] init];

    for (PPRecognizerResult* result in results) {

        if ([result isKindOfClass:[PPPdf417RecognizerResult class]]) {
            PPPdf417RecognizerResult *pdf417Result = (PPPdf417RecognizerResult *)result;

            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            [self setDictionary:dict withPdf417RecognizerResult:pdf417Result];

            [resultArray addObject:dict];
        }

        if ([result isKindOfClass:[PPZXingRecognizerResult class]]) {
            PPZXingRecognizerResult *zxingResult = (PPZXingRecognizerResult *)result;

            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            [self setDictionary:dict withZXingRecognizerResult:zxingResult];

            [resultArray addObject:dict];
        }

        if ([result isKindOfClass:[PPUsdlRecognizerResult class]]) {
            PPUsdlRecognizerResult *usdlResult = (PPUsdlRecognizerResult *)result;

            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            [self setDictionary:dict withUsdlResult:usdlResult];

            [resultArray addObject:dict];
        }

        if ([result isKindOfClass:[PPBarDecoderRecognizerResult class]]) {
            PPBarDecoderRecognizerResult *barDecoderResult = (PPBarDecoderRecognizerResult *)result;

            NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
            [self setDictionary:dict withBarDecoderRecognizerResult:barDecoderResult];

            [resultArray addObject:dict];
        }
    };

    if ([resultArray count] > 0) {
        [resultDict setObject:resultArray forKey:@"resultList"];
    }
    
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:resultDict];
    
    /*
     NSString* js = [result toSuccessCallbackString:[[self lastCommand] callbackId]];
     
     [self writeJavascript:js];
     */
    
    [self.commandDelegate sendPluginResult:result callbackId:self.lastCommand.callbackId];
    
    // As scanning view controller is presented full screen and modally, dismiss it
    [[self viewController] dismissViewControllerAnimated:YES completion:nil];
}

- (void)returnError:(NSString*)message {
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                messageAsString:message];
    /*
     NSString* js = [result toErrorCallbackString:[[self lastCommand] callbackId]];
     
     [self writeJavascript:js];
     */
    
    [self.commandDelegate sendPluginResult:result callbackId:self.lastCommand.callbackId];
    
    // As scanning view controller is presented full screen and modally, dismiss it
    [[self viewController] dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - PPScanDelegate delegate methods

- (void)scanningViewControllerUnauthorizedCamera:(UIViewController<PPScanningViewController> *)scanningViewController {
    // Add any logic which handles UI when app user doesn't allow usage of the phone's camera
}

- (void)scanningViewController:(UIViewController<PPScanningViewController> *)scanningViewController
                  didFindError:(NSError *)error {
    // Can be ignored. See description of the method
}

- (void)scanningViewControllerDidClose:(UIViewController<PPScanningViewController> *)scanningViewController {

    [self returnResults:nil cancelled:YES];

    // As scanning view controller is presented full screen and modally, dismiss it
    [[self viewController] dismissViewControllerAnimated:YES completion:nil];
}

- (void)scanningViewController:(UIViewController<PPScanningViewController> *)scanningViewController
              didOutputResults:(NSArray *)results {

    [self returnResults:results cancelled:(results == nil)];

    // As scanning view controller is presented full screen and modally, dismiss it
    [[self viewController] dismissViewControllerAnimated:YES completion:nil];
}

@end
