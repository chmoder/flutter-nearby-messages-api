//
//  NearbyMessagesApiMCMs.m
//  nearby_messages_api
//
//  Created by Thomas Cross on 3/2/19.
//

#import "NearbyMessagesApiMCMs.h"
#import <Flutter/Flutter.h>
#import <GNSMessages.h>

static NSString * const kMyAPIKey = @"<insert API key here>";
static NSString * const kBackgroundModeSaveKey = @"NearbyBackgroundEnabled";

@interface NearbyMessagesApiMCMs ()
@property(nonatomic) GNSPermission *nearbyPermission;
@property(nonatomic) GNSMessageManager *messageMgr;
@property(nonatomic) id<GNSPublication> publication;
@property(nonatomic) id<GNSSubscription> subscription;
@property(nonatomic) FlutterMethodChannel *channel;
@end

@implementation NearbyMessagesApiMCMs

- (instancetype)init {
    return [self initWithChannel:nil];
}

- (instancetype)initWithChannel:(FlutterMethodChannel *)channel {
    self = [super init];
    if (self) {
        self.channel = channel;
    }
    return self;
}

/// Starts sharing with a randomized name and scanning for others.
- (void)backgroundSubscribe {
    [GNSMessageManager setDebugLoggingEnabled:YES];

    NSString *name = [NSString stringWithFormat:@"Anonymous %d", rand() % 100];
    
    // Create a strategy that enabled background mode.
    GNSStrategy *strategy = [GNSStrategy strategyWithParamsBlock:^(GNSStrategyParams *params) {
        params.allowInBackground = YES;
    }];
    
    // Publish the name to nearby devices.
    GNSMessage *pubMessage =
    [GNSMessage messageWithContent:[name dataUsingEncoding:NSUTF8StringEncoding]];
    _publication = [_messageMgr publicationWithMessage:pubMessage
                                           paramsBlock:^(GNSPublicationParams *params) {
                                               params.strategy = strategy;
                                           }];
    
    // Subscribe to messages from nearby devices and display them in the message view.
    NSString *(^stringFromData)(NSData *) = ^(NSData *content) {
        return [[NSString alloc] initWithData:content encoding:NSUTF8StringEncoding];
    };
    _subscription = [_messageMgr
                     subscriptionWithMessageFoundHandler:^(GNSMessage *message) {
                         NSString *messageString = stringFromData(message.content);
                         [self.channel invokeMethod:@"onFound" arguments:messageString];
                     }
                     messageLostHandler:^(GNSMessage *message) {
                         NSString *messageString = stringFromData(message.content);
                         [self.channel invokeMethod:@"onLost" arguments:messageString];
                     }
                     paramsBlock:^(GNSSubscriptionParams *params) {
                         params.strategy = strategy;
                     }];
    
    [[NSUserDefaults standardUserDefaults] setBool:YES forKey:kBackgroundModeSaveKey];
}

/// Stops sharing and scanning.
- (void)backgroundUnsubscribe {
    _publication = nil;
    _subscription = nil;
    [[NSUserDefaults standardUserDefaults] setBool:NO forKey:kBackgroundModeSaveKey];
}

@end
