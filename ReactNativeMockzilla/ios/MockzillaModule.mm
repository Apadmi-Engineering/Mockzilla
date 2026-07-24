#import "MockzillaModule.h"
#import "ReactNativeMockzilla-Swift.h"

@interface MockzillaModule () <MockzillaEventSender>
@property (nonatomic, strong) MockzillaSwiftBridge *swiftBridge;
@end

@implementation MockzillaModule

RCT_EXPORT_MODULE()

- (instancetype)init {
    if (self = [super init]) {
        _swiftBridge = [[MockzillaSwiftBridge alloc] initWithEventSender:self];
    }
    return self;
}

+ (BOOL)requiresMainQueueSetup { return NO; }

- (NSArray<NSString *> *)supportedEvents { return @[@"MockzillaRequest"]; }

- (void)emitRequestEvent:(NSDictionary *)body {
    [self sendEventWithName:@"MockzillaRequest" body:body];
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
    return std::make_shared<facebook::react::NativeMockzillaModuleSpecJSI>(params);
}

- (void)startMockzilla:(NSDictionary *)config
               resolve:(RCTPromiseResolveBlock)resolve
                reject:(RCTPromiseRejectBlock)reject {
    [_swiftBridge startMockzillaWithConfig:config
                                   resolve:^(NSDictionary *p) { resolve(p); }
                                    reject:^(NSString *c, NSString *m) { reject(c, m, nil); }];
}

- (void)stopMockzilla:(RCTPromiseResolveBlock)resolve
               reject:(RCTPromiseRejectBlock)reject {
    [_swiftBridge stopMockzillaWithResolve:^{ resolve(nil); }
                                     reject:^(NSString *c, NSString *m) { reject(c, m, nil); }];
}

- (void)respondToMatcher:(NSString *)requestId matches:(BOOL)matches {
    [_swiftBridge respondToMatcherWithRequestId:requestId matches:matches];
}

- (void)respondToHandler:(NSString *)requestId response:(NSDictionary *)response {
    [_swiftBridge respondToHandlerWithRequestId:requestId response:response];
}

@end
