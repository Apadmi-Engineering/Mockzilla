//
//  AppDelegate.swift
//  iosApp
//
//  Created by Sam Da Costa on 29/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    static var repository: Repository!
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        let params = MockServerKt.startMockServer()
        AppDelegate.repository = Repository(baseUrl: params.mockBaseUrl)
   
        return true
    }
    
    func application(_: UIApplication,
                     configurationForConnecting connectingSceneSession: UISceneSession,
                     options _: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }
    
    func applicationWillTerminate(_ application: UIApplication) {
        MockServerKt.stopMockServer()
    }
}
