package com.apadmi.mockzilla.mobile.ui

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat

import com.apadmi.mockzilla.MockzillaManagementSdkActivity

public fun launchManagementUi(context: Context): Unit = context.startActivity(
    Intent(
        context,
        MockzillaManagementSdkActivity::class.java,
    ),
    ActivityOptionsCompat.makeCustomAnimation(context, 0, 0).toBundle(),
)
