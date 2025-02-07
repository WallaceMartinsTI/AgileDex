package com.wcsm.agiledex

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wcsm.agiledex.ui.theme.AgileDexTheme
import com.wcsm.agiledex.ui.theme.PrimaryColor
import com.wcsm.agiledex.ui.view.PokemonView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // App runs only in PORTRAIT ORIENTATION for now
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            AgileDexTheme(dynamicColor = false) {
                SetBarColor(PrimaryColor)

                PokemonView()
            }
        }
    }

    @Composable
    fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(color = color)
        }
    }
}