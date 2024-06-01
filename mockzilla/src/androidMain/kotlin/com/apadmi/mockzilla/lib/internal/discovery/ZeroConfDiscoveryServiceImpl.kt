package com.apadmi.mockzilla.lib.internal.discovery

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.apadmi.mockzilla.lib.models.MetaData
import java.util.UUID

class ZeroConfDiscoveryServiceImpl(private val context: Context) : ZeroConfDiscoveryService {
    private val registrationListener = object : NsdManager.RegistrationListener {
        override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
            // Save the service name. Android may have changed it in order to
            // resolve a conflict, so update the name you initially requested
            // with the name Android actually used.
            // mServiceName = serviceInfo.serviceName
        }

        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Registration failed! Put debugging code here to determine why.
        }

        override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
            // Service has been unregistered. This only happens when you call
            // NsdManager.unregisterService() and pass in this listener.
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Unregistration failed. Put debugging code here to determine why.
        }
    }

    override fun makeDiscoverable(metaData: MetaData, port: Int) {
        val serviceInfo = NsdServiceInfo().apply {
            // TODO: Persist this to disk so that we're not generating IDs each time
            serviceName = UUID.randomUUID().toString()
            serviceType = "_mockzilla._tcp."
            this.port = port

            metaData.toMap().forEach {
                setAttribute(it.key, it.value)
            }
        }

        (context.getSystemService(Context.NSD_SERVICE) as NsdManager).registerService(
            serviceInfo,
            NsdManager.PROTOCOL_DNS_SD,
            registrationListener
        )
    }
}
