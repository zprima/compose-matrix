package com.example.matrixdownfall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.matrixdownfall.ui.screen.MatrixScreen
import com.example.matrixdownfall.ui.theme.MatrixDownfallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MatrixDownfallTheme {
                MatrixScreen()
            }
        }
    }
}

