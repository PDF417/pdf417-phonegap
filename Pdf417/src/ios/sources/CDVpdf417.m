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

@interface CDVpdf417()<MBBarcodeOverlayViewControllerDelegate>

@property (nonatomic, retain) CDVInvokedUrlCommand *lastCommand;

@property (nonatomic, nullable) MBBarcodeRecognizer *barcodeRecognizer;

@property (nonatomic, nullable) MBUsdlRecognizer *usdlRecognizer;

@end

static NSString * const kScan = @"scan";
static NSString * const kCancelled = @"cancelled";

static NSString * const kBarcodeResult = @"Barcode result";
static NSString * const kUSDLResult = @"USDL result";

static NSString * const kResultList = @"resultList";
static NSString * const kResultType = @"resultType";
static NSString * const kType = @"type";
static NSString * const kData = @"data";
static NSString * const kFields = @"fields";
static NSString * const kRawData = @"raw";

// barcode types
static NSString * const kTypePDF417 = @"PDF417";
static NSString * const kTypeCode128 = @"Code 128";
static NSString * const kTypeCode39 = @"Code 39";
static NSString * const kTypeAztec = @"Aztec";
static NSString * const kTypeDataMatrix = @"Data Matrix";
static NSString * const kTypeEAN13 = @"EAN 13";
static NSString * const kTypeEAN8 = @"EAN 8";
static NSString * const kTypeITF = @"ITF";
static NSString * const kTypeQRCode = @"QR Code";
static NSString * const kTypeUPCA = @"UPCA";
static NSString * const kTypeUPCE = @"UPCE";
static NSString * const kTypeUSDL = @"USDL";

// scanning options
static NSString * const kOptionBeepSound = @"beep";
static NSString * const kOptionNoDialog = @"noDialog";
static NSString * const kOptionUncertain = @"uncertain";
static NSString * const kOptionQuietZone = @"quietZone";
static NSString * const kOptionHighRes = @"highRes";
static NSString * const kOptionInverseScanning = @"inverseScanning";
static NSString * const kOptionFrontFace = @"frontFace";

@implementation CDVpdf417

@synthesize lastCommand;

- (MBUsdlRecognizer *)usdlRecognizerSettingsWithOption:(NSDictionary<NSString *, NSNumber *> *)options {

    MBUsdlRecognizer *recognizer = [[MBUsdlRecognizer alloc] init];

    // Set this to true to scan barcodes which don't have quiet zone (white area) around it
    // Use only if necessary because it slows down the recognition process
    NSNumber *shouldQuietZone = options[kOptionQuietZone];
    recognizer.allowNullQuietZone = shouldQuietZone && shouldQuietZone.boolValue;

    // Set this to true to scan even barcode not compliant with standards
    // For example, malformed barcodes which were incorrectly encoded
    // Use only if necessary because it slows down the recognition process
    NSNumber *shouldAllowUncertain = options[kOptionUncertain];
    recognizer.scanUncertain = shouldAllowUncertain && shouldAllowUncertain.boolValue;

    return recognizer;
}

- (MBBarcodeRecognizer *)barcodeRecognizerSettingsWithOptions:(NSDictionary<NSString *, NSNumber *> *)options
                                                        types:(NSArray<NSString *> *)types {
    MBBarcodeRecognizer *recognizer = [[MBBarcodeRecognizer alloc] init];

    recognizer.scanAztec = [types containsObject:kTypeAztec];

    recognizer.scanCode39 = [types containsObject:kTypeCode39];

    recognizer.scanCode128 = [types containsObject:kTypeCode128];

    recognizer.scanDataMatrix = [types containsObject:kTypeDataMatrix];

    recognizer.scanEAN8 = [types containsObject:kTypeEAN8];

    recognizer.scanEAN13 = [types containsObject:kTypeEAN13];

    recognizer.scanITF = [types containsObject:kTypeITF];

    recognizer.scanPdf417 = [types containsObject:kTypePDF417];

    recognizer.scanQR = [types containsObject:kTypeQRCode];

    recognizer.scanUPCA = [types containsObject:kTypeUPCA];

    recognizer.scanUPCE = [types containsObject:kTypeUPCE];

    // Set this to true to scan barcodes which don't have quiet zone (white area) around it
    // Use only if necessary because it slows down the recognition process
    NSNumber *shouldQuietZone = options[kOptionQuietZone];
    recognizer.allowNullQuietZone = shouldQuietZone && shouldQuietZone.boolValue;

    // Set this to true to scan even barcode not compliant with standards
    // For example, malformed barcodes which were incorrectly encoded
    // Use only if necessary because it slows down the recognition process
    NSNumber *shouldAllowUncertain = options[kOptionUncertain];
    recognizer.scanUncertain = shouldAllowUncertain && shouldAllowUncertain.boolValue;

    // Set this to YES to allow scanning barcodes with inverted intensities
    // (i.e. white barcodes on black background)
    NSNumber *shouldInverse = options[kOptionInverseScanning];
    recognizer.scanInverse = shouldInverse && shouldInverse.boolValue;

    return recognizer;
}

- (BOOL)shouldUseBarcodeRecognizerForTypes:(NSArray *)types {

    BOOL aztec = [types containsObject:kTypeAztec];
    BOOL code39 = [types containsObject:kTypeCode39];
    BOOL code128 = [types containsObject:kTypeCode128];
    BOOL dataMatrix = [types containsObject:kTypeDataMatrix];
    BOOL ean8 = [types containsObject:kTypeEAN8];
    BOOL ean13 = [types containsObject:kTypeEAN13];
    BOOL itf = [types containsObject:kTypeITF];
    BOOL pdf417 = [types containsObject:kTypePDF417];
    BOOL qrcode = [types containsObject:kTypeQRCode];
    BOOL upca = [types containsObject:kTypeUPCA];
    BOOL upce = [types containsObject:kTypeUPCE];

    return (qrcode || ean8 || ean13 || itf || upca || upce || aztec || dataMatrix || code39 || code128 || pdf417);
}

- (BOOL)shouldUseUSDLRecognizerForTypes:(NSArray *)types {
    return [types containsObject:kTypeUSDL];
}

- (MBBarcodeOverlaySettings *)createSettings {

    [[MBMicroblinkSDK sharedInstance] setLicenseKey:[self.lastCommand argumentAtIndex:2]];
    
    NSArray *types = [self.lastCommand argumentAtIndex:0];

    NSDictionary<NSString *, NSNumber *>* options = nil;

    if ([self.lastCommand arguments].count >= 2) {
        options = [self.lastCommand argumentAtIndex:1];
    }

    MBBarcodeOverlaySettings *settings = [[MBBarcodeOverlaySettings alloc] init];

    NSNumber *shouldUseFrontCamera = options[kOptionFrontFace];
    if (shouldUseFrontCamera && shouldUseFrontCamera.boolValue) {
        settings.cameraSettings.cameraType = PPCameraTypeFront;
    }

    NSNumber *shouldUseHighRes = options[kOptionHighRes];
    if (shouldUseHighRes && shouldUseHighRes.boolValue) {
        settings.cameraSettings.cameraPreset = PPCameraPresetMax;
    }

    NSMutableArray<MBRecognizer *> *recognizers = [[NSMutableArray alloc] init];

    if ([self shouldUseBarcodeRecognizerForTypes:types]) {
        self.barcodeRecognizer = [self barcodeRecognizerSettingsWithOptions:options types:types];
        [recognizers addObject:self.barcodeRecognizer];
    }

    if ([self shouldUseUSDLRecognizerForTypes:types]) {
        self.usdlRecognizer = [self usdlRecognizerSettingsWithOption:options];
        [recognizers addObject:self.usdlRecognizer];
    }

    MBRecognizerCollection *recognizerCollection = [[MBRecognizerCollection alloc] initWithRecognizers:recognizers];

    settings.uiSettings.recognizerCollection = recognizerCollection;

    return settings;
}

- (void)scan:(CDVInvokedUrlCommand*)command {
    
    [self setLastCommand:command];

    MBBarcodeOverlaySettings *settings = [self createSettings];

    MBBarcodeOverlayViewController *overlayViewController = [[MBBarcodeOverlayViewController alloc] initWithSettings:settings andDelegate:self];
    
    /** Allocate and present the scanning view controller */
    UIViewController<MBRecognizerRunnerViewController>* recognizerRunnerViewController = [MBViewControllerFactory recognizerRunnerViewControllerWithOverlayViewController:overlayViewController];

    /** You can use other presentation methods as well */
    [[self viewController] presentViewController:recognizerRunnerViewController animated:YES completion:nil];
}

- (void)setDictionary:(NSMutableDictionary*)dict withBarcodeRecognizerResult:(MBBarcodeRecognizerResult*)data {

    NSString *output = [data stringData];
    if (output) {
        dict[kData] = output;
    }
    
    dict[kType] = data.barcodeType == MBBarcodeTypePdf417 ? kTypePDF417 : [MBBarcodeRecognizerResult toTypeName:data.barcodeType];
    dict[kResultType] = kBarcodeResult;
}

- (void)setDictionary:(NSMutableDictionary*)dict withUsdlRecognizerResult:(MBUsdlRecognizerResult*)usdlResult {

    NSMutableArray *fields = [[NSMutableArray alloc] init];

    for (NSUInteger key = DocumentType; key <= SecurityVersion; ++key) {
        [fields addObject:[usdlResult getField:key]];
    }

    dict[kFields] = fields;
    dict[kResultType] = kUSDLResult;
}

- (void)returnResultsCancelled:(BOOL)cancelled {

    NSMutableDictionary* resultDict = [[NSMutableDictionary alloc] init];
    resultDict[kCancelled] = [NSNumber numberWithInt: (cancelled ? 1 : 0)];

    NSMutableArray *resultArray = [[NSMutableArray alloc] init];

    if (self.barcodeRecognizer && self.barcodeRecognizer.result.resultState == MBRecognizerResultStateValid) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        [self setDictionary:dict withBarcodeRecognizerResult:self.barcodeRecognizer.result];
        [resultArray addObject:dict];
    }

    if (self.usdlRecognizer && self.usdlRecognizer.result.resultState == MBRecognizerResultStateValid) {
        NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
        [self setDictionary:dict withUsdlRecognizerResult:self.usdlRecognizer.result];
        [resultArray addObject:dict];
    }

    if ([resultArray count] > 0) {
        resultDict[kResultList] = resultArray;
    }

    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:resultDict];

    [self.commandDelegate sendPluginResult:result callbackId:self.lastCommand.callbackId];

    // As scanning view controller is presented full screen and modally, dismiss it
    [[self viewController] dismissViewControllerAnimated:YES completion:nil];
}

- (void)returnError:(NSString*)message {

    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                messageAsString:message];
    
    [self.commandDelegate sendPluginResult:result callbackId:self.lastCommand.callbackId];
    
    // As scanning view controller is presented full screen and modally, dismiss it
    [[self viewController] dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - MBBarcodeOverlayViewControllerDelegate

- (void)barcodeOverlayViewControllerDidFinishScanning:(MBBarcodeOverlayViewController *)barcodeOverlayViewController state:(MBRecognizerResultState)state {
    /** This callback is done on background thread and you need to be careful to not do any UI operations on it */
    [barcodeOverlayViewController.recognizerRunnerViewController pauseScanning];

    if (state != MBRecognizerResultStateValid) {
        [barcodeOverlayViewController.recognizerRunnerViewController resumeScanningAndResetState:YES];
        return;
    }

    [barcodeOverlayViewController.recognizerRunnerViewController playScanSuccesSound];

    /** Needs to be called on main thread beacuse everything prior is on background thread */
    dispatch_async(dispatch_get_main_queue(), ^{
        [self returnResultsCancelled:NO];
    });
}

- (void)barcodeOverlayViewControllerDidTapClose:(MBBarcodeOverlayViewController *)barcodeOverlayViewController {
    // Close button tapped on overlay view controller
    [self returnResultsCancelled:YES];
}

@end
