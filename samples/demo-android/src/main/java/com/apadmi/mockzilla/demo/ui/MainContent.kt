package com.apadmi.mockzilla.demo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apadmi.mockzilla.android.sample.R
import com.apadmi.mockzilla.demo.ui.components.AnimalCard
import com.apadmi.mockzilla.demo.ui.components.AppBar

@Composable
fun MainContent(
    state: MainViewModel.State,
    setRequestText: (request: String) -> Unit,
    makeRequest: (someValue: String) -> Unit,
    setIsReleaseMode: (isRelease: Boolean) -> Unit,
    launchManagementUi: () -> Unit
) = Column {
    AppBar(state = state, setIsReleaseMode = setIsReleaseMode)

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mockzilla Farm",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold)
            )
            Button(
                onClick = { makeRequest(state.values.requestBody) },
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = "Refresh")
            }
        }
        
        when (state) {
            is MainViewModel.State.FetchError -> ErrorContent(state = state)
            is MainViewModel.State.Success -> SuccessContent(
                state = state,
                setRequestText = setRequestText
            )
            else -> {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = launchManagementUi,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = "Launch Mockzilla Management UI")
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.mockzilla_logo),
                contentDescription = "Launch Mockzilla Management UI"
            )
        }
    }
}

@Composable
private fun SuccessContent(
    state: MainViewModel.State.Success,
    setRequestText: (request: String) -> Unit
) = Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(20.dp)
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(text = "Enter the owner of your animals below and then refresh!")
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.values.requestBody,
            onValueChange = setRequestText,
            placeholder = { Text(text = "Animals Owner") }
        )
    }

    state.cowResult?.let { cow ->
        AnimalCard(animal = cow)
    } ?: Text(text = "No cow found")

    state.sheepResult?.let { sheep ->
        AnimalCard(animal = sheep)
    } ?: Text(text = "No sheep found")

    state.pigResult?.let { pig ->
        AnimalCard(animal = pig)
    } ?: Text(text = "No pig found")
}

@Composable
private fun ErrorContent(
    state: MainViewModel.State.FetchError
) = Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = "An error occurred...",
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
    )
    Text(
        text = state.error,
        style = MaterialTheme.typography.bodyLarge
    )
}
