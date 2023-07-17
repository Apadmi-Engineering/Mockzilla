package com.apadmi.mockzilla.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (application as RootApplication).repository

        val response = mutableStateOf<DataResult<CowDto, String>?>(null)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                MainContent(cowResult = response.value, { someValue ->
                    lifecycleScope.launchWhenCreated {
                        response.value = repository.getCow(someValue)
                    }
                }, { isRelease ->
                    (application as RootApplication).setReleaseMode(isRelease)
                })
            }
        }
    }
}

@Composable
fun MainContent(
    cowResult: DataResult<CowDto, String>?,
    makeRequest: (someValue: String) -> Unit,
    setIsReleaseMode: (isRelease: Boolean) -> Unit,
) = Column(
    modifier = Modifier
        .fillMaxSize(1f)
        .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {

    val text = remember { mutableStateOf("") }
    val isRelease = remember { mutableStateOf(false) }

    Text(
        "Enter a value here to be passed into the request body",
        fontSize = 24.sp
    )
    TextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = {
            Text("Request Value")
        }
    )

    Button({
        makeRequest(text.value)
    }) {
        Text("Make Network Request")
    }

    Button({
        isRelease.value = !isRelease.value
        setIsReleaseMode(isRelease.value)
    }) {
        Text("Release Mode is ${if (isRelease.value) "On" else "Off"}")
    }


    if (cowResult != null) {
        Text("Network Request Body")
        Text(
            "${GetCowRequestDto(text.value)}",
            fontFamily = FontFamily.Monospace
        )
        Divider()
        Text("Response: ${if (cowResult.isSuccess()) "Success!" else "Failed"}")
        Text(
            "${cowResult.dataOrNull() ?: cowResult.errorOrNull()}",
            fontFamily = FontFamily.Monospace
        )
    }
}
