//
//  AppDelegate.swift
//  demo-ios
//
//  Created by Sam Da Costa on 26/10/2022.
//


import UIKit
import SwiftMockzilla
import mockzilla

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    static var repository: Repository!


    func application(_: UIApplication,
                     didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]?) -> Bool
    {
        // Start the mockzilla server
        let params = startMockzilla(config: MockzillaConfig.createConfig())
        AppDelegate.repository = Repository(baseUrl: params.mockBaseUrl,
                                            authHeaderProvider: params.authHeaderProvider)

        return true
    }

    // MARK: UISceneSession Lifecycle

    func application(_: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options _: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_: UIApplication, didDiscardSceneSessions _: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }

    func applicationWillTerminate(_: UIApplication) {
        // Stop the mockzilla server to ensure a clean start next time the app launches
        stopMockzilla()
    }
}
