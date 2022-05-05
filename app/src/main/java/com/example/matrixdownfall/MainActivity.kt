package com.example.matrixdownfall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.example.matrixdownfall.ui.screen.MatrixScreen
import com.example.matrixdownfall.ui.theme.MatrixDownfallTheme
import com.example.matrixdownfall.util.Mode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MatrixDownfallTheme {

                var mode by remember { mutableStateOf(Mode.GREEN) }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { mode = if(mode == Mode.COLOR) Mode.GREEN else Mode.COLOR }
                            )
                        },
                    color = MaterialTheme.colors.background
                ) {
                    MatrixScreen(mode)
                }
            }
        }
    }
}

