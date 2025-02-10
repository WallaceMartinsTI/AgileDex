package com.wcsm.agiledex.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.agiledex.R
import com.wcsm.agiledex.presentation.ui.theme.AgileDexTheme
import com.wcsm.agiledex.presentation.ui.theme.OnPrimaryColor
import com.wcsm.agiledex.presentation.ui.theme.ThemeBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgileDexTopBar(title: String) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF8287C9), Color(0xFF2ACB78))
                        )
                    )
                )

                Spacer(Modifier.width(8.dp))

                Image(
                    painter = painterResource(R.drawable.pokebal),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)

                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ThemeBackgroundColor,
            titleContentColor = OnPrimaryColor
        )
    )
}

@Preview
@Composable
private fun AgileDexTopBarPreview() {
    AgileDexTheme {
        AgileDexTopBar("AGILE DEX")
    }
}