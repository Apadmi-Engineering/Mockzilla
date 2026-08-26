// swift-tools-version: 5.9
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "mockzilla_ui_mobile",
    platforms:[.iOS(.v15)],
    products: [
        .library(name: "mockzilla-ui-mobile", targets: ["mockzilla_ui_mobile"])
    ],
    dependencies: [
        .package(name: "FlutterFramework", path: "../FlutterFramework"),
        .package(
            url: "https://github.com/Apadmi-Engineering/SwiftMockzillaMobileUi.git",
            // x-release-please-start-version
                .upToNextMajor(from: "1.1.0")
            // # x-release-please-end
)
    ],
    targets: [
        .target(
            name: "mockzilla_ui_mobile",
            dependencies: [
                "SwiftMockzillaMobileUi",
                .product(name: "FlutterFramework", package: "FlutterFramework")
            ],
            resources: []
        )
    ]
)
