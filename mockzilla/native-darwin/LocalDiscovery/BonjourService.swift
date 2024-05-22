import Foundation
import Network

@objc public class BonjourService: NSObject {
    /// The reference to the registering..
    private var sdRef: DNSServiceRef?

    /// Starts the broadcast.
    @objc public func start(name: String) {
        let type = "_mockzilla._tcp"
        let domain = "local."
        let port = 8080
        
        let error = DNSServiceRegister(
            &sdRef,
            0,
            0,
            name,
            type,
            domain,
            nil,
            CFSwapInt16HostToBig(UInt16(port)),
            0,
            nil,
            BonjourService.registerCallback as DNSServiceRegisterReply,
            Unmanaged.passUnretained(self).toOpaque())
        
        if error == kDNSServiceErr_NoError {
            DNSServiceProcessResult(sdRef)
        } else {
            debugPrint("Failure", error)
            dispose()
        }
    }

    public func dispose() {
        DNSServiceRefDeallocate(sdRef)
    }

    /// Callback triggered by`DNSServiceRegister`.
    private static let registerCallback: DNSServiceRegisterReply = { _, _, errorCode, name, _, _, context in
        let broadcast = Unmanaged<BonjourService>.fromOpaque(context!).takeUnretainedValue()
        if errorCode == kDNSServiceErr_NoError {
            debugPrint("Registered", String(cString: name!))
        } else {
            debugPrint("Error", errorCode)
            broadcast.dispose()
        }
    }
}
