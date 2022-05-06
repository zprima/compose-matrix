package com.example.matrixdownfall.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.matrixdownfall.util.Mode

class MatrixViewModel: ViewModel() {
    var uiState by mutableStateOf(MatrixUiState())
        private set

    fun changeMode(){
        val newMode =
            when(uiState.mode){
                Mode.GREEN -> Mode.COLOR
                Mode.COLOR -> Mode.BLACK_WHITE
                Mode.BLACK_WHITE -> Mode.GREEN
            }

        uiState = uiState.copy(mode = newMode)
    }

}