package com.apadmi.mockzilla.mock.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.apadmi.mockzilla.mock.CowDto
import com.apadmi.mockzilla.mock.DataResult
import com.apadmi.mockzilla.mock.GetCowRequestDto

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (application as RootApplication).repository
        val response = mutableStateOf<DataResult<CowDto, String>?>(null)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainContent(cowResult = response.value) { someValue ->
                    lifecycleScope.launchWhenCreated {
                        response.value = repository.getCow(someValue)
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(
    cowResult: DataResult<CowDto, String>?,
    makeRequest: (someValue: String) -> Unit,
) = Column(
    modifier = Modifier
        .fillMaxSize(1f)
        .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    val text = remember { mutableStateOf("") }

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

    cowResult?.let {
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
