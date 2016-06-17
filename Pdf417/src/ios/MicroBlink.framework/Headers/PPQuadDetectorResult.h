//
//  PPQuadDetectorResult.h
//  BlinkIdFramework
//
//  Created by Jura on 10/01/16.
//  Copyright © 2016 MicroBlink Ltd. All rights reserved.
//

#import "PPDetectorResult.h"

#import "PPQuadrangle.h"

NS_ASSUME_NONNULL_BEGIN

/**
 * Result of the detection of a Quad detector
 */
PP_CLASS_AVAILABLE_IOS(6.0) @interface PPQuadDetectorResult : PPDetectorResult

/**
 * Exact location of detected object on image
 */
@property (nonatomic) PPQuadrangle* detectionLocation;

@end

NS_ASSUME_NONNULL_END
