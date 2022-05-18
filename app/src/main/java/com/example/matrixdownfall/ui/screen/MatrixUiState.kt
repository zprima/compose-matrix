package com.example.matrixdownfall.ui.screen

import androidx.compose.ui.graphics.Color
import com.example.matrixdownfall.util.Mode

data class MatrixUiState(
    val mode: Mode = Mode.GREEN,
    val rows: Int = 20,
    val stripDelayMin: Int = 1,
    val stripDelayMax: Int = 8,
    val stripSpeedMin: Int = 1,
    val stripSpeedMax: Int = 4,
    val textMinFactor: Int = 5,
    val textMaxFactor: Int = 15,
    val colors: List<Color> = listOf(Color.Red, Color.Cyan, Color.Yellow, Color.Magenta)
)