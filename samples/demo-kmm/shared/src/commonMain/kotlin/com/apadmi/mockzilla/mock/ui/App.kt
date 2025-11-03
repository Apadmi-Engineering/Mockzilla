package com.apadmi.mockzilla.mock.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apadmi.mockzilla.mock.CowDto
import com.apadmi.mockzilla.mock.DataResult
import com.apadmi.mockzilla.mock.GetCowRequestDto
import com.apadmi.mockzilla.mock.Repository
import kotlinx.coroutines.launch

@Composable
fun App(repository: Repository, launchManagementUi: () -> Unit) {
    val scope = rememberCoroutineScope()
    val response = remember { mutableStateOf<DataResult<CowDto, String>?>(null) }
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
            launchManagementUi = launchManagementUi
        )
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
