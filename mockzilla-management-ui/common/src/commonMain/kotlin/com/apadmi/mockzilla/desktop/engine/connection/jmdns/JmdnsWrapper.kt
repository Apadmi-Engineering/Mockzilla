package com.apadmi.mockzilla.desktop.engine.connection.jmdns

import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceTypeListener

interface JmdnsWrapper {

    fun registerListener(serviceType: String, listener: (ServiceInfoWrapper) -> Unit)
}

internal class JmdnsWrapperImpl: JmdnsWrapper {
    private val jmdns: JmDNS = JmDNS.create(InetAddress.getLocalHost())

    override fun registerListener(serviceType: String, listener: (ServiceInfoWrapper) -> Unit) {
        jmdns.addServiceTypeListener(ServiceTypeAddedListener { event ->
            if (event?.type?.startsWith(ZeroConfConfig.serviceType) == true) {
                jmdns.addServiceListener(serviceType, ServiceEventResolvedListener {
                    if (it != null) {
                        listener(ServiceInfoWrapper(it.info))
                    }
                })
            }
        })
    }
}
