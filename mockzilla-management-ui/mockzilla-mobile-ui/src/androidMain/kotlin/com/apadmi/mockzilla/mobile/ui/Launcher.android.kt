package com.apadmi.mockzilla.mobile.ui

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat

import com.apadmi.mockzilla.MockzillaManagementSdkActivity

fun launchManagementUi(context: Context) = context.startActivity(
    Intent(
        context,
        MockzillaManagementSdkActivity::class.java,
    ),
    ActivityOptionsCompat.makeCustomAnimation(context, 0, 0).toBundle(),
)
