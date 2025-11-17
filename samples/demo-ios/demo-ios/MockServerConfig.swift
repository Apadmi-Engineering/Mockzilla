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
           .setPort(port: 8034)
           .setIsReleaseModeEnabled(isRelease: false) // Change to true to test release mode
           .setLogLevel(level: LogLevel.verbose)
           .addEndpoint(endpoint: getCowEndpoint)
           .build()
    }
}

fileprivate extension MockzillaConfig {
    static var getCowEndpoint: EndpointConfiguration {
        get {
            EndpointConfiguration.Builder(key: "cow")
                .setSwiftPatternMatcher {
                    $0.uri.hasSuffix("cow")
                }
                .setSwiftErrorHandler { _ in
                    MockzillaHttpResponse(status: HttpStatusCode.InternalServerError)
                }
                .setSwiftDefaultHandler { request in
                    let request = try! await GetCowRequestDto.fromJson(
                        data: request.bodyAsString().data(using: .utf8)!
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
