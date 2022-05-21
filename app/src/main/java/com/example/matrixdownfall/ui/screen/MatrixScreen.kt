package com.example.matrixdownfall.ui.screen

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matrixdownfall.util.Mode
import com.example.matrixdownfall.util.characters
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun MatrixScreen(
    matrixViewModel: MatrixViewModel = viewModel()
){
    val uiState = matrixViewModel.uiState
    val backgroundColor = if(uiState.mode == Mode.BLACK_WHITE) Color.White else Color.Black

    // Remember a SystemUiController
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = Color.Black,
            darkIcons = useDarkIcons
        )

        // setStatusBarsColor() and setNavigationBarColor() also exist
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { matrixViewModel.changeMode() }
                )
            },
        color = backgroundColor
    ) {
        MatrixStrips(uiState)
    }
}

@Composable
fun MatrixStrips(uiState: MatrixUiState){

    Row(modifier = Modifier){
        repeat(uiState.rows){
            MatrixStrip(
                modifier = Modifier.weight(1f),
                stripDelay = Random.nextInt(uiState.stripDelayMin, uiState.stripDelayMax) * 1000L,
                stripSpeed = Random.nextInt(uiState.stripSpeedMin, uiState.stripSpeedMax) * 10L + 100,
                stripColor =
                    when(uiState.mode){
                        Mode.GREEN -> Color.Green
                        Mode.COLOR -> uiState.colors.random()
                        Mode.BLACK_WHITE -> Color.Black
                    },
                textSizeFactor = Random.nextInt(uiState.textMinFactor, uiState.textMaxFactor) * 0.1f
            )
        }
    }
}

@Composable
fun MatrixStrip(modifier: Modifier, stripDelay: Long, stripSpeed: Long, stripColor: Color, textSizeFactor: Float){
    var lettersToDraw by remember { mutableStateOf(0) }

    BoxWithConstraints(modifier = modifier.fillMaxHeight()) {

        val ratio = remember { (maxHeight / maxWidth).toInt() }
        val stripCharaters = remember {
            Array(ratio){ characters.random() }
        }

        val textSize = with(LocalDensity.current){
            (maxWidth * textSizeFactor).toSp()
        }

        Column() {
            repeat(lettersToDraw) {
                MatrixChar(
                    character = stripCharaters[it],
                    textSize = textSize,
                    color = stripColor,
                    onAnimationFinished = {
                        if(it >= stripCharaters.size * 0.5){
                            lettersToDraw = 0
                        }
                    }
                )
            }
        }

        LaunchedEffect(key1 = stripDelay){
            delay(stripDelay)
            while(true){
                if(lettersToDraw < stripCharaters.size){
                    lettersToDraw += 1
                }
                delay(stripSpeed)
            }
        }
    }
}

@Composable
fun MatrixChar(
    character: String,
    textSize: TextUnit,
    color: Color = Color.Green,
    onAnimationFinished: () -> Unit){
    var runAnimation by remember { mutableStateOf(false) }
    val alpha = animateFloatAsState(
        targetValue = if(runAnimation) 0f else 1f,
        animationSpec = tween(
            durationMillis = (1..4).random() * 1000,
        ),
        finishedListener = { onAnimationFinished() }
    )

    Text(
        character,
        color = color.copy(alpha = alpha.value),
        fontSize = textSize
    )

    LaunchedEffect(Unit){
        runAnimation = true
    }
}