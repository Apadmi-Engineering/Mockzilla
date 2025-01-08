// swift-tools-version: 5.9
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "mockzilla_ios",
    platforms:[.iOS(.v13)],
    products: [
        .library(name: "mockzilla-ios", targets: ["mockzilla_ios"])
    ],
    dependencies: [
        .package(
            url: "https://github.com/Apadmi-Engineering/SwiftMockzilla.git",
            .upToNextMajor(from: "2.1.2")
        )
    ],
    targets: [
        .target(
            name: "mockzilla_ios",
            dependencies: ["SwiftMockzilla"],
            resources: []
        )
    ]
)
