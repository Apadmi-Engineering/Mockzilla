//
//  MockServerConfig.swift
//  demo-ios
//
//  Created by Sam Da Costa on 28/11/2022.
//

import mockzilla
import SwiftMockzilla

import Foundation

extension MockzillaConfig {
    
    static func createConfig() -> MockzillaConfig {
        MockzillaConfig.Builder()
           .setAppName(name: "My App")
           .setFailureProbabilityPercentage(percentage: 0)
           .setIsReleaseModeEnabled(isRelease: false) // Change to true to test release mode
           .setMeanDelayMillis(delay: 100)
           .setDelayVarianceMillis(variance: 40)
           .setLogLevel(level: LogLevel.verbose)
           .addEndpoint(endpoint: getCowEndpoint)
           .build()
    }
}

fileprivate extension MockzillaConfig {
    static var getCowEndpoint: EndpointConfiguration {
        get {
            EndpointConfiguration.Builder(id: "cow")
                .setSwiftPatternMatcher {
                    $0.uri.hasSuffix("cow")
                }
                .setWebApiDefaultResponse(response: MockzillaHttpResponse(
                    body: CowDto.empty.toJson())
                )
                .setErrorHandler { _ in
                    MockzillaHttpResponse(status: HttpStatusCode.InternalServerError)
                }
                .setFailureProbability(percentage: 0)
                .setMeanDelayMillis(delay: 10)
                .setDelayVarianceMillis(variance: 0)
                .setDefaultHandler { request in
                    let request = try! GetCowRequestDto.fromJson(
                        data: request.body.data(using: .utf8)!
                    )
                    
                    return MockzillaHttpResponse(
                        statusCode: HttpStatusCode.BadRequest,
                        headers: ["i_am_a_cow": "Yes a great cow"],
                        body: CowDto(
                            name: "Bessie",
                            age: 41,
                            likesGrass: true,
                            hasHorns: false,
                            mooSample: "Mooooooooooooo",
                            someValueFromRequest: request.aValueInTheRequest
                        ).toJson()
                    )
                }.build()
        }
    }
}
