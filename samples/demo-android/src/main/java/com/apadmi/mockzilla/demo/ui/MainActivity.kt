package com.apadmi.mockzilla.demo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.apadmi.mockzilla.mobile.ui.launchManagementUi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val repository = (application as RootApplication).repository
        val viewModel = MainViewModel(repository = repository) { isRelease ->
            (application as RootApplication).setReleaseMode(isRelease)
        }
        val state = viewModel.state

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainContent(
                    state = state.value,
                    setRequestText = viewModel::setRequestText,
                    makeRequest = viewModel::makeRequest,
                    setIsReleaseMode = viewModel::setIsReleaseMode,
                    launchManagementUi = { launchManagementUi(this) }
                )
            }
        }
    }
}
