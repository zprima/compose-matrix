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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matrixdownfall.util.characters
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun MatrixScreen(rows:Int = 10) {
    Row(modifier = Modifier.background(Color.Black)){
        repeat(rows){
            MatrixDropDown(
                modifier = Modifier.weight(1f),
                yDelay = Random.nextInt(10) * 1000L,
                speed = (Random.nextInt(10) * 10L) + 100
            )
        }
    }
}

@Composable
fun MatrixDropDown(modifier: Modifier, yDelay:Long, speed:Long){
//    Log.d("APP", "ydelay $yDelay")
//    Log.d("APP", "speed $speed")

    BoxWithConstraints(
        modifier = modifier
            .fillMaxHeight(),
        contentAlignment = Alignment.TopCenter
    ) {
        val pxWidth = with(LocalDensity.current){ maxWidth.toPx() }
        val pxHeigh = with(LocalDensity.current){ maxHeight.toPx() }

        val charsPerDropdown = remember {
            Array((pxHeigh / pxWidth).toInt()){
                characters.random()
            }
        }
//        Log.d("APP", "charsPerDropdown ${charsPerDropdown.size}")

        val lettersToDraw = remember { mutableStateOf(0) }
//        Log.d("APP", "lettersToDraw ${lettersToDraw.value}")

        Column(){
            repeat(lettersToDraw.value){
                MatrixCharacter(
                    character = charsPerDropdown[it],
                    pxSize = pxWidth,
                    speed = speed
                ){
                    if(it >= (charsPerDropdown.size * 0.6).toInt()){
                        lettersToDraw.value = 0
                    }
                }
            }
        }

        LaunchedEffect(key1 = yDelay){
            delay(yDelay)
            while(true){
                if(lettersToDraw.value < charsPerDropdown.size){
                    lettersToDraw.value += 1
                }
                delay(speed)
            }
        }
    }
}

@Composable
fun MatrixCharacter(
    character:String,
    pxSize: Float,
    speed: Long,
    onAlphaFinished: () -> Unit
){
    val spSize = with(LocalDensity.current) { pxSize.toSp() }
    val textColor = remember { mutableStateOf(Color.White) }

    val startFade = remember { mutableStateOf(false) }
    val alpha = animateFloatAsState(
        targetValue = if(startFade.value) 0f else 1f,
        animationSpec = tween(
            durationMillis = 2000
        ),
        finishedListener = { onAlphaFinished() }
    )

    Text(
        character,
        fontSize = spSize,
        color = textColor.value.copy(alpha = alpha.value)
    )

    LaunchedEffect(Unit){
        delay(speed)
        textColor.value = Color.Green
        startFade.value = true
    }
}