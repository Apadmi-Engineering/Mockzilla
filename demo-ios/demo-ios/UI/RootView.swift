//
//  RootView.swift
//  demo-ios
//
//  Created by Sam Da Costa on 07/11/2022.
//

import mockzilla
import SwiftUI

struct Fonts {
    static let debug = Font
        .system(size: 14)
        .monospaced()
}

struct RootView: View {
    @State private var text: String = ""
    @State private var cowResult: CowDto? = nil
    @State private var errorResult: String? = nil

    var body: some View {
        VStack(alignment: .leading, spacing: 14) {
            Text("Native iOS Demo")
                .font(.system(size: 24))
            
            Text("Enter a value here to be passed into the request body")
                .font(.system(size: 24))
            TextField(
                "Request Data",
                text: $text
            ).font(.system(size: 16))

            Button(action: {
                AppDelegate.repository.getCow(someRequestValue: text) { result, error in
                    cowResult = result
                    errorResult = error
                }
            }) {
                Text("Make Network Request").font(.system(size: 16))
            }

            Divider()

            if cowResult != nil || errorResult != nil {
                Text("Network Request Body").font(.system(size: 16))

                Text(
                    "\(String(describing: GetCowRequestDto(aValueInTheRequest: text)))"
                ).font(Fonts.debug)

                Divider()

                Text("Response: \(cowResult != nil ? "Success!" : "Failed")")
                    .font(.system(size: 24))

                if let data = cowResult {
                    Text(String(describing: data))
                        .font(Fonts.debug)
                } else {
                    Text(errorResult ?? "")
                        .font(Fonts.debug)
                }
            }
            Spacer()
        }.frame(maxHeight: .infinity)
            .padding(16)
    }
}
