//
//  NearbyMessagesApiMCMs.h
//  Pods
//
//  Created by Thomas Cross on 3/2/19.
//

#ifndef NearbyMessagesApiMCMs_h
#define NearbyMessagesApiMCMs_h

#import <Flutter/Flutter.h>

@interface NearbyMessagesApiMCMs : NSObject
- (instancetype)initWithChannel:(FlutterMethodChannel *)channel NS_DESIGNATED_INITIALIZER;
- (void)backgroundSubscribe;
- (void)backgroundUnsubscribe;
@end

#endif /* NearbyMessagesApiMCMs_h */
