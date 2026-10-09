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

internal class ZeroConfDiscoveryServiceImpl(
    private val logger: Logger,
    private val context: Context
) : ZeroConfDiscoveryService {
    private val nsdManager by lazy { context.getSystemService(Context.NSD_SERVICE) as NsdManager }
    private val localNetworkPermission = LocalNetworkPermission(context, logger)
    private var registered = false
    private val registrationListener = object : NsdManager.RegistrationListener {
        override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
            logger.i("ZeroConf Registered: ${serviceInfo.serviceName}")
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
        if (!hasLocalNetworkAccess()) {
            return
        }

        val serviceInfo = NsdServiceInfo().apply {
            serviceName = metaData.bonjourServiceName(getOrPutDeviceIdentifier())
            serviceType = ZeroConfConfig.serviceType
            this.port = port

            metaData.toMap().forEach {
                setAttribute(it.key, it.value)
            }
        }

        try {
            nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
            registered = true
        } catch (e: SecurityException) {
            // Should have been caught by the permission check above, but never crash the host app over an
            // optional feature (e.g. if the user revoked the permission in the meantime).
            logger.w("Network discovery skipped: ${e.message}")
        }
    }

    /**
     * On Android 17 (API 37)+ advertising on the network needs the local network permission, which we
     * ask for here if the app has declared it. Without it the app would crash, so skip discovery instead.
     */
    private suspend fun hasLocalNetworkAccess() = when (localNetworkPermission.ensureGranted()) {
        LocalNetworkAccess.Granted -> true
        LocalNetworkAccess.Denied -> {
            logger.w(
                "Network discovery skipped: the local network permission was denied, so the Mockzilla " +
                        "desktop app won't be able to find or connect to this device."
            )
            false
        }
        LocalNetworkAccess.NotDeclared -> {
            logger.w(
                "Network discovery skipped: your app targets Android 17 (API 37)+ but doesn't declare the " +
                        "local network permission. Add <uses-permission " +
                        "android:name=\"android.permission.ACCESS_LOCAL_NETWORK\" /> to your AndroidManifest.xml " +
                        "(Mockzilla will request it for you), or disable discovery with " +
                        "MockzillaConfig.Builder.setIsNetworkDiscoveryEnabled(false)."
            )
            false
        }
    }

    override suspend fun stop() {
        if (registered) {
            runCatching { nsdManager.unregisterService(registrationListener) }
                .onFailure { logger.e("Failed to unregister NSD service: ${it.message}") }
            registered = false
        }
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
