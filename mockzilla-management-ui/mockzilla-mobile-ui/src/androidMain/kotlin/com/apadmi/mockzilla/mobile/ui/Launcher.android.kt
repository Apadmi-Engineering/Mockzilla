package com.apadmi.mockzilla.mobile.ui

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.apadmi.mockzilla.MockzillaManagementSdkActivity

fun launchManagementUi(context: Context) = context.startActivity(
    Intent(
        context,
        MockzillaManagementSdkActivity::class.java
    ),
    null
)
