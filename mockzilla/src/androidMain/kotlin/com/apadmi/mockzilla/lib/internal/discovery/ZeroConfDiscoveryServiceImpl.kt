package com.apadmi.mockzilla.lib.internal.discovery

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo

import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import com.apadmi.mockzilla.lib.models.MetaData

import co.touchlab.kermit.Logger
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight

import java.util.UUID

class ZeroConfDiscoveryServiceImpl(
    private val logger: Logger,
    private val context: Context
) : ZeroConfDiscoveryService {
    private val registrationListener = object : NsdManager.RegistrationListener {
        override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
            logger.e("ZeroConf Registered: ${serviceInfo.serviceName}")
        }

        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            logger.e("ZeroConf Registration failed: $errorCode")
        }

        override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
            logger.e("ZeroConf Unregistered: ${serviceInfo.serviceType}")
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            logger.e("ZeroConf Unregistration failed: $errorCode")
        }
    }

    override suspend fun makeDiscoverable(metaData: MetaData, port: Int) {
        val serviceInfo = NsdServiceInfo().apply {
            serviceName = metaData.bonjourServiceName(getOrPutDeviceIdentifier())
            serviceType = ZeroConfConfig.serviceType
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

    private suspend fun getOrPutDeviceIdentifier(): String {
        val sharedPrefs = context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE)
        val identifierOrNull = sharedPrefs.getString(deviceIdentifierKey, null)

        identifierOrNull?.let {
            return identifierOrNull
        }

        val newIdentifier = getAdvertiserIdOrNull() ?: UUID.randomUUID().toString()
        // Save the new identifier in shared preferences even if it's the AdvertisingID.
        // The AdId could change but we don't really care and pulling the value from shared prefs
        // should be faster than calling out to Google play.
        sharedPrefs.edit().putString(deviceIdentifierKey, newIdentifier).apply()

        return newIdentifier
    }

    private suspend fun getAdvertiserIdOrNull() = if (isGoogleServiceApiAvailable()) {
        AdvertisingIdClient.getAdvertisingIdInfo(context)
            .id
            ?.takeIf { it.isValidAdvertisingId() }
            .also { logger.d("AdvertiserId found, using it for the ZeroConf service: $it") }
    } else {
        null
    }

    private fun isGoogleServiceApiAvailable() =
        GoogleApiAvailabilityLight.getInstance()
            .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS

    private fun String.isValidAdvertisingId() = isNotBlank() && !(all { it == '-' || it == '0' })

    companion object {
        private const val deviceIdentifierKey = "device_identifier"
        private const val sharedPrefsName = "mockzilla_shared_prefs"
    }
}
