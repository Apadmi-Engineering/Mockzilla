import shared
import SwiftUI

struct Fonts {
    static let debug = Font
        .system(size: 14)
        .monospaced()
}

struct RootView: View {
    @State private var text: String = ""
    @State private var cowResult: DataResult<CowDto, NSString>? = nil

    var body: some View {
        VStack(alignment: .leading, spacing: 14) {
            Text("KMM iOS demo")
                .font(.system(size: 24))
            
            Text("Enter a value here to be passed into the request body")
                .font(.system(size: 24))
            TextField(
                "Request Data",
                text: $text
            ).font(.system(size: 16))

            Button(action: {
                AppDelegate.repository.getCow(someRequestValue: text) { result, _ in
                    cowResult = result
                }
            }) {
                Text("Make Network Request").font(.system(size: 16))
            }

            Divider()
            
            if let result = cowResult {
                Text("Network Request Body").font(.system(size: 16))

                Text(
                    "\(GetCowRequestDto(valueInTheRequest: text))"
                ).font(Fonts.debug)

                Divider()
                Text("Response: \(result.isSuccess() ? "Success!" : "Failed")")
                    .font(.system(size: 24))

                if let data = result.dataOrNull() {
                    Text(String(describing: data))
                        .font(Fonts.debug)
                } else {
                    Text(String(describing: result.errorOrNull()))
                        .font(Fonts.debug)
                }
            }
            Spacer()
        }.frame(maxHeight: .infinity)
            .padding(16)
    }
}
