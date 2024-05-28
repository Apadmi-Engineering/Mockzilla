import Foundation
import Network

@objc public class BonjourService: NSObject {
    /// The reference to the registering..
    private var sdRef: DNSServiceRef?

    /// Starts the broadcast.
    @objc public func start(type: String, txtRecord: [String: String], port: Int, name: String) {
        let domain = "local."

        let (txtRecordLength, txtRecordPointer) = createTXTRecord(from: txtRecord)

        let error = DNSServiceRegister(
            &sdRef,
            0,
            0,
            name,
            type,
            domain,
            nil,
            CFSwapInt16HostToBig(UInt16(port)),
            txtRecordLength,
            txtRecordPointer,
            BonjourService.registerCallback as DNSServiceRegisterReply,
            Unmanaged.passUnretained(self).toOpaque())
        
        if error == kDNSServiceErr_NoError {
            DNSServiceProcessResult(sdRef)
        } else {
            debugPrint("Failure", error)
            dispose()
        }

        txtRecordPointer?.deallocate()
    }

    public func dispose() {
        DNSServiceRefDeallocate(sdRef)
    }

    /// Callback triggered by`DNSServiceRegister`.
    private static let registerCallback: DNSServiceRegisterReply = { _, _, errorCode, name, _, _, context in
        let broadcast = Unmanaged<BonjourService>.fromOpaque(context!).takeUnretainedValue()
        if errorCode == kDNSServiceErr_NoError {
            debugPrint("BonjourService: Success", String(cString: name!))
        } else {
            debugPrint("BonjourService: Error", errorCode)
            broadcast.dispose()
        }
    }

    func createTXTRecord(from dictionary: [String: String]) -> (length: UInt16, pointer: UnsafeMutablePointer<UInt8>?) {
        var txtData = Data()

        for (key, value) in dictionary {
            guard let keyData = key.data(using: .utf8),
                  let valueData = value.data(using: .utf8) else { continue }

            var itemLength = UInt8(keyData.count + 1 + valueData.count)
            txtData.append(&itemLength, count: 1)
            txtData.append(keyData)
            txtData.append(UInt8(61)) // ASCII code for '='
            txtData.append(valueData)
        }

        let txtRecordPointer = UnsafeMutablePointer<UInt8>.allocate(capacity: txtData.count)
        txtData.copyBytes(to: txtRecordPointer, count: txtData.count)

        return (length: UInt16(txtData.count), pointer: txtRecordPointer)
    }
}
