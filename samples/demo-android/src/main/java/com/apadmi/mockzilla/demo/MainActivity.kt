package com.apadmi.mockzilla.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (application as RootApplication).repository
        val viewModel = MainViewModel(
            repository = repository
        ) { isRelease ->
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
                    setIsReleaseMode = viewModel::setIsReleaseMode
                )
            }
        }
    }
}

@Composable
fun MainContent(
    state: MainViewModel.State,
    setRequestText: (request: String) -> Unit,
    makeRequest: (someValue: String) -> Unit,
    setIsReleaseMode: (isRelease: Boolean) -> Unit,
) = Column(
    modifier = Modifier
        .fillMaxSize(1f)
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    Text(
        "Enter a value here to be passed into the request body",
        fontSize = 24.sp
    )
    TextField(
        value = state.request,
        onValueChange = setRequestText,
        label = {
            Text("Request Value")
        }
    )

    Button(
        onClick = { makeRequest(state.request) }
    ) {
        Text("Make Network Request")
    }

    Button(
        onClick = { setIsReleaseMode(!state.isRelease) }
    ) {
        Text("Release Mode is ${if (state.isRelease) "On" else "Off"}")
    }
    state.cowResult?.let { cowResult ->
        Text("Network Request Body")
        Text(
            "${GetCowRequestDto(state.request)}",
            fontFamily = FontFamily.Monospace
        )
        HorizontalDivider()
        Text("Response: ${if (cowResult.isSuccess()) "Success!" else "Failed"}")
        Text(
            "${cowResult.dataOrNull() ?: cowResult.errorOrNull()}",
            fontFamily = FontFamily.Monospace
        )
    }
}
