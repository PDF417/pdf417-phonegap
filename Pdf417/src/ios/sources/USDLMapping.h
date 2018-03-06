//
//  USDLMapping.h
//  Pdf417Demo
//
//  Created by Jure Cular on 06/03/2018.
//

#import <Foundation/Foundation.h>

@interface USDLMapping : NSObject

@property (nonatomic) NSDictionary<NSNumber *, NSString *> *mKeyMappings;

+ (USDLMapping *)sharedInstance;

@end
