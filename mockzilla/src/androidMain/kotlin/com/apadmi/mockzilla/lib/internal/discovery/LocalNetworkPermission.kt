package com.apadmi.mockzilla.lib.internal.discovery

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

import co.touchlab.kermit.Logger

import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal const val localNetworkPermissionName = "android.permission.ACCESS_LOCAL_NETWORK"
private const val localNetworkPermissionApiLevel = 37
private const val logTag = "Local network permission:"

internal enum class LocalNetworkAccess {
    /** The user said no, or the request was interrupted. */
    Denied,

    Granted,

    /** The app targets Android 17+ but hasn't declared the permission, so it can never be granted. */
    NotDeclared,
    ;
}

/**
 * Android 17 (API 37) blocks local network access for apps that target it, unless they hold the
 * `ACCESS_LOCAL_NETWORK` runtime permission. Mockzilla needs it to advertise itself to the desktop app.
 *
 * The app only has to declare the permission in its manifest, Mockzilla asks for it itself the first time
 * discovery is started, from whichever activity is in the foreground.
 */
internal class LocalNetworkPermission(
    private val context: Context,
    private val logger: Logger
) {
    init {
        if (Build.VERSION.SDK_INT >= localNetworkPermissionApiLevel) {
            (context.applicationContext as? Application)?.let(ForegroundActivityTracker::register)
        }
    }

    /**
     * Returns once local network access is known to be granted or denied. This may suspend while the user
     * answers the permission prompt, so don't call it from somewhere that must not wait.
     *
     * @return
     */
    suspend fun ensureGranted(): LocalNetworkAccess {
        val deviceApiLevel = Build.VERSION.SDK_INT
        val targetApiLevel = context.applicationInfo.targetSdkVersion
        logger.d("$logTag checking (device is on API $deviceApiLevel, app targets API $targetApiLevel)")

        return when {
            deviceApiLevel < localNetworkPermissionApiLevel -> {
                logger.d("$logTag not required before API $localNetworkPermissionApiLevel")
                LocalNetworkAccess.Granted
            }
            isGranted() -> {
                logger.i("$logTag already granted")
                LocalNetworkAccess.Granted
            }
            !isDeclared() -> accessWithoutDeclaration(targetApiLevel)
            else -> {
                logger.i("$logTag declared in the manifest but not granted, requesting it")
                request()
            }
        }
    }

    // Apps targeting 36 or lower keep implicit access, but only while they *haven't* declared the permission.
    private fun accessWithoutDeclaration(targetApiLevel: Int): LocalNetworkAccess =
        if (targetApiLevel >= localNetworkPermissionApiLevel) {
            logger.d("$logTag not declared in the manifest, and as the app targets API $targetApiLevel it can't be granted")
            LocalNetworkAccess.NotDeclared
        } else {
            logger.d("$logTag not declared in the manifest, but as the app targets API $targetApiLevel Android grants access implicitly")
            LocalNetworkAccess.Granted
        }

    private suspend fun request(): LocalNetworkAccess = requestMutex.withLock {
        if (hasBeenDenied) {
            logger.i("$logTag denied earlier in this session, not asking again")
            LocalNetworkAccess.Denied
        } else {
            logger.i("$logTag waiting for an activity in the foreground to show the prompt from")
            val activity = ForegroundActivityTracker.awaitResumedActivity()
            logger.d("$logTag found foreground activity ${activity::class.java.name}")
            // The answer may have arrived while we were waiting, e.g. if Mockzilla was restarted while a
            // previous prompt was still up.
            when {
                isGranted() -> {
                    logger.i("$logTag granted while waiting for an activity")
                    LocalNetworkAccess.Granted
                }
                hasBeenDenied -> {
                    logger.i("$logTag denied while waiting for an activity")
                    LocalNetworkAccess.Denied
                }
                showPrompt(activity) == true -> LocalNetworkAccess.Granted
                else -> LocalNetworkAccess.Denied
            }
        }
    }

    /**
     * Shows the prompt and suspends until it's answered.
     *
     * @return `true` if granted, `false` if denied or `null` if the request was interrupted.
     */
    private suspend fun showPrompt(activity: Activity): Boolean? = suspendCancellableCoroutine { continuation ->
        val intent = LocalNetworkPermissionActivity.intent(activity) { granted ->
            logger.i("$logTag prompt answered: ${granted.describe()}")
            // Remember a denial even if we've stopped waiting (e.g. Mockzilla was stopped with the prompt up).
            if (granted == false) {
                hasBeenDenied = true
            }
            if (continuation.isActive) {
                continuation.resume(granted)
            } else {
                logger.d("$logTag the answer arrived after Mockzilla stopped waiting for it")
            }
        }
        continuation.invokeOnCancellation {
            logger.d("$logTag stopped waiting for the prompt, it may still be showing")
        }
        logger.i("$logTag showing the prompt")
        activity.runOnUiThread { activity.startActivity(intent) }
    }

    private fun isGranted() =
        context.checkSelfPermission(localNetworkPermissionName) == PackageManager.PERMISSION_GRANTED

    @Suppress("DEPRECATION")
    private fun isDeclared() = runCatching {
        context.packageManager
            .getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
            .requestedPermissions
            .orEmpty()
            .contains(localNetworkPermissionName)
    }.getOrDefault(false)

    private fun Boolean?.describe() = when {
        this == true -> "granted"
        this == false -> "denied"
        else -> "interrupted (the prompt was closed without an answer)"
    }

    private companion object {
        val requestMutex = Mutex()

        // Process wide so we don't nag the user again if Mockzilla is restarted after they said no.
        @Volatile
        var hasBeenDenied = false
    }
}
