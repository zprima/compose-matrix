package com.example.matrixdownfall.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import com.example.matrixdownfall.util.characters
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun MatrixScreen(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        MatrixRain(30)
    }
}

@Composable
fun MatrixRain(stripColumns: Int){
    var startScreenDone by remember { mutableStateOf(false) }

    Row(){
        repeat(stripColumns){
            if(!startScreenDone){
            MatrixColumn(
                100L,
                0L,
                true,
                onFinished = {
                    startScreenDone = true
                }
            )}
            else {
                MatrixColumn(
                    Random.nextInt(4) * 10L + 100L,
                    Random.nextInt(6) * 1000L,
                    false,
                    onFinished = {}
                )
            }
        }
    }
}

@Composable
fun RowScope.MatrixColumn(crawlSpeed: Long, columnDelay: Long, oneTime: Boolean, onFinished: () -> Unit){
    BoxWithConstraints(
        modifier = Modifier.weight(1f).fillMaxHeight()
    ) {
        val maxWidthDp = maxWidth
        val matrixStrip = remember { Array((maxHeight / maxWidth).toInt()){ characters.random() } }
        var lettersToDraw by remember { mutableStateOf(0) }

        Column(Modifier.fillMaxSize()){
            repeat(lettersToDraw){
                MatrixChar(
                    matrixStrip[it],
                    crawlSpeed,
                    fontSize = with(LocalDensity.current){ maxWidthDp.toSp() },
                    onFinished = {
                        if(oneTime && it == (matrixStrip.size * 0.6).toInt()){
                            onFinished()
                        } else {
                            if(it >= matrixStrip.size * 0.6){
                                lettersToDraw = 0
                            }
                        }
                    }
                )
            }
        }

        LaunchedEffect(Unit){
            delay(columnDelay)
            while(true){
                if(lettersToDraw < matrixStrip.size) {
                    lettersToDraw += 1
                }

                if(lettersToDraw > matrixStrip.size * 0.5){
                    matrixStrip[Random.nextInt(lettersToDraw)] = characters.random()
                }

                delay(crawlSpeed)

            }
        }
    }
}


@Composable
fun MatrixChar(char: String, crawlSpeed: Long, fontSize: TextUnit, onFinished: () -> Unit){
    var textColor by remember { mutableStateOf(Color.White) }
    var startFade by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if(startFade) 0f else 1f,
        animationSpec = tween(
            durationMillis = 4000,
            easing = LinearEasing
        ),
        finishedListener = { onFinished() }
    )

    Text(
        char,
        color = textColor.copy(alpha = alpha),
        fontSize = fontSize
    )

    LaunchedEffect(Unit){
        delay(crawlSpeed)
        textColor = Color(0xff43c728)
        startFade = true
    }
}
