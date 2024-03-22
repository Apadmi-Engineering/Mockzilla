//
//  PlatformUtils.swift
//  mockzilla_ios
//
//  Created by Tom Handcock on 22/03/2024.
//

import Foundation
import mockzilla

internal func extractMetaData() -> MetaData {
    let bundle = Bundle.main
    return MetaData(
        appName: bundle.object(forInfoDictionaryKey: "CFBundleName") as? String ?? "",
        appPackage: bundle.bundleIdentifier ?? "",
        operatingSystemVersion: UIDevice.current.systemVersion,
        deviceModel: UIDevice.current.systemName,
        appVersion: bundle.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String ?? "",
        operatingSystem: "iOS",
        mockzillaVersion: ""
    )
}
