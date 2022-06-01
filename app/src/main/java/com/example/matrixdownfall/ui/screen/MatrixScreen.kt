package com.example.matrixdownfall.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import com.example.matrixdownfall.util.characters
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun MatrixScreen(){
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
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        MatrixRain(20)
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
        val fontSize = with(LocalDensity.current){ maxWidth.toSp() }
        val matrixStrip = remember { Array((maxHeight / maxWidth).toInt()){ characters.random() } }
        var lettersToDraw by remember { mutableStateOf(0) }

        Column(Modifier.fillMaxSize()){
            repeat(lettersToDraw){
                MatrixChar(
                    matrixStrip[it],
                    crawlSpeed,
                    fontSize = fontSize,
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

                if(lettersToDraw > matrixStrip.size * 0.6){
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
