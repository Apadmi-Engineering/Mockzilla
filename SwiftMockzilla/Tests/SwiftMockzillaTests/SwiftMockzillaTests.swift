import XCTest
@testable import SwiftMockzilla
import mockzilla

final class SwiftMockzillaTests: XCTestCase {
    func testSanityCheck() throws {
        XCTAssertEqual(Ktor_httpHttpStatusCode.Companion.shared.OK, HttpStatusCode.OK)
        XCTAssertEqual(MockzillaHttpResponse(),
                       MockzillaHttpResponse(statusCode: HttpStatusCode.OK, headers: [:], body: ""))
    }
}

