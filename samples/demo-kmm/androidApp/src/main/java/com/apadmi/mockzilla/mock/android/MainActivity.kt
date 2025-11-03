package com.apadmi.mockzilla.mock.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.apadmi.mockzilla.mobile.ui.launchManagementUi

import com.apadmi.mockzilla.mock.ui.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val repository = (application as RootApplication).repository

        setContent {
            App(repository) {
                launchManagementUi(this)
            }
        }
    }
}
