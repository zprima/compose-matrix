package com.example.matrixdownfall.ui.screen

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matrixdownfall.util.characters
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun MatrixScreen(matrixRows:Int = 20){
    Row(modifier = Modifier.background(Color.Black)){
        repeat(matrixRows){
            MatrixStrip(
                modifier = Modifier.weight(1f),
                stripDelay = Random.nextInt(1, 5) * 1000L,
                stripSpeed = Random.nextInt(1, 10) * 10L + 100
            )
        }
    }
}

@Composable
fun MatrixStrip(modifier: Modifier, stripDelay: Long, stripSpeed: Long){
    var lettersToDraw by remember { mutableStateOf(0) }

    BoxWithConstraints(modifier = modifier.fillMaxHeight()) {

        val ratio = remember { (maxHeight / maxWidth).toInt() }
        val stripCharaters = remember {
            Array(ratio){ characters.random() }
        }

        val textSizeFactor = remember { Random.nextInt(5, 10) * 0.1f }
        val textSize = with(LocalDensity.current){
            (maxWidth * textSizeFactor).toSp()
        }

        Column() {
            repeat(lettersToDraw) {
                MatrixChar(
                    character = stripCharaters[it],
                    textSize = textSize,
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
fun MatrixChar(character: String, textSize: TextUnit, onAnimationFinished: () -> Unit){
    var runAnimation by remember { mutableStateOf(false) }
    val alpha = animateFloatAsState(
        targetValue = if(runAnimation) 0f else 1f,
        animationSpec = tween(
            durationMillis = 2000,
        ),
        finishedListener = { onAnimationFinished() }
    )

    Text(
        character,
        color = Color.Green.copy(alpha = alpha.value),
        fontSize = textSize
    )

    LaunchedEffect(Unit){
        runAnimation = true
    }
}