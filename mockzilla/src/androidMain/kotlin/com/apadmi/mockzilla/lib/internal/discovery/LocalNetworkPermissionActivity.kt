package com.apadmi.mockzilla.lib.internal.discovery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver

/**
 * A transparent activity whose only job is to show the local network permission prompt on behalf of
 * Mockzilla, since the result of a permission request is delivered to the activity that asked.
 *
 * Use [intent] to launch it. The result is reported back through a [ResultReceiver] in the intent.
 */
internal class LocalNetworkPermissionActivity : Activity() {
    private var resultSent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If we were recreated mid-request the system redelivers the result to us, don't ask again.
        savedInstanceState?.let {
            return
        }
        requestPermissions(arrayOf(localNetworkPermissionName), requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // An empty result means the request was interrupted, rather than answered.
        val result = when (grantResults.firstOrNull()) {
            PackageManager.PERMISSION_GRANTED -> resultGranted
            null -> resultInterrupted
            else -> resultDenied
        }
        report(result)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Closed without an answer, so don't leave Mockzilla waiting.
        if (isFinishing) {
            report(resultInterrupted)
        }
    }

    private fun report(result: Int) {
        if (resultSent) {
            return
        }
        resultSent = true
        @Suppress("DEPRECATION")
        intent.getParcelableExtra<ResultReceiver>(extraResultReceiver)?.send(result, null)
    }

    companion object {
        private const val extraResultReceiver = "result_receiver"
        private const val requestCode = 1
        private const val resultDenied = 2
        private const val resultGranted = 1
        private const val resultInterrupted = 3

        /**
         * Creates an intent that shows the prompt. [onResult] is called on the main thread with `true` if
         * the permission was granted, `false` if it was denied or `null` if the request was interrupted.
         *
         * @param context
         * @param onResult
         * @return
         */
        fun intent(context: Context, onResult: (granted: Boolean?) -> Unit): Intent {
            val receiver = object : ResultReceiver(Handler(Looper.getMainLooper())) {
                override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                    onResult(
                        when (resultCode) {
                            resultGranted -> true
                            resultDenied -> false
                            else -> null
                        }
                    )
                }
            }
            return Intent(context, LocalNetworkPermissionActivity::class.java)
                .putExtra(extraResultReceiver, receiver)
        }
    }
}
