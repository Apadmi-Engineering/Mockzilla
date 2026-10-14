package com.apadmi.mockzilla.lib.internal.discovery

import android.app.Activity
import android.app.Application
import android.os.Bundle

import java.lang.ref.WeakReference

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull

/**
 * Keeps track of the activity currently in the foreground, so there's somewhere to launch a permission
 * request from when Mockzilla is started from `Application.onCreate`. Only holds weak references.
 */
internal object ForegroundActivityTracker : Application.ActivityLifecycleCallbacks {
    private val resumed = MutableStateFlow<WeakReference<Activity>?>(null)
    private var registered = false

    @Synchronized
    fun register(application: Application) {
        if (registered) {
            return
        }
        registered = true
        application.registerActivityLifecycleCallbacks(this)
    }

    /**
     * Suspends until an activity is in the foreground.
     *
     * @return
     */
    suspend fun awaitResumedActivity(): Activity = resumed.mapNotNull { it?.get() }.first()

    override fun onActivityResumed(activity: Activity) {
        if (activity !is LocalNetworkPermissionActivity) {
            resumed.value = WeakReference(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (resumed.value?.get() === activity) {
            resumed.value = null
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}
