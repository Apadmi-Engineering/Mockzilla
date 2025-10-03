package com.apadmi.mockzilla.mock.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apadmi.mockzilla.mobile.ui.launchManagementUi

import com.apadmi.mockzilla.mock.CowDto
import com.apadmi.mockzilla.mock.DataResult
import com.apadmi.mockzilla.mock.GetCowRequestDto
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val repository = (application as RootApplication).repository
        val response = mutableStateOf<DataResult<CowDto, String>?>(null)

        setContent {
            val scope = rememberCoroutineScope()
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainContent(
                    cowResult = response.value,
                    makeRequest = { someValue ->
                        scope.launch {
                            response.value = repository.getCow(someValue)
                        }
                    },
                    launchManagementUi = {
                        launchManagementUi(this)
                    }
                )
            }
        }
    }
}

@Composable
fun MainContent(
    cowResult: DataResult<CowDto, String>?,
    makeRequest: (someValue: String) -> Unit,
    launchManagementUi: () -> Unit
) = Column(
    modifier = Modifier
        .fillMaxSize(1f)
        .statusBarsPadding()
        .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    val text = remember { mutableStateOf("") }

    Text(
        text = "Enter a value here to be passed into the request body",
        fontSize = 24.sp
    )
    TextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = {
            Text("Request Value")
        }
    )

    Button(
        onClick = { makeRequest(text.value) }
    ) {
        Text("Make Network Request")
    }

    Button(
        onClick = launchManagementUi
    ) {
        Text("Launch Management UI")
    }

    cowResult?.let {
        Text("Network Request Body")
        Text(
            text = "${GetCowRequestDto(text.value)}",
            fontFamily = FontFamily.Monospace
        )
        HorizontalDivider()
        Text("Response: ${if (cowResult.isSuccess()) "Success!" else "Failed"}")
        Text(
            text = "${cowResult.dataOrNull() ?: cowResult.errorOrNull()}",
            fontFamily = FontFamily.Monospace
        )
    }
}
