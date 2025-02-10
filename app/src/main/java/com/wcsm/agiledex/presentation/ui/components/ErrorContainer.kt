package com.wcsm.agiledex.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.agiledex.presentation.ui.theme.AgileDexTheme
import com.wcsm.agiledex.presentation.ui.theme.PrimaryColor

@Composable
fun ErrorContainer(
    errorTitle: String,
    errorMessage: String,
    modifier: Modifier = Modifier,
    onTryAgain: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorTitle,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = errorMessage,
            color = PrimaryColor,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(280.dp)
        )

        Button(
            onClick = {
                onTryAgain()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = "TRY AGAIN",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun ErrorContainer(
    errorTitle: String,
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorTitle,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = errorMessage,
            color = PrimaryColor,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(280.dp)
        )
    }
}

@Preview
@Composable
fun ErrorContainerWithButtonPreview() {
    AgileDexTheme {
        ErrorContainer(
            errorTitle = "There was a problem.",
            errorMessage = "Error: request failed.",
        ) { }
    }
}

@Preview
@Composable
fun ErrorContainerWithoutButtonPreview() {
    AgileDexTheme {
        ErrorContainer(
            errorTitle = "There was a problem.",
            errorMessage = "Error: request failed.",
        )
    }
}