package com.example.mtg_decks.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mtg_decks.R
import com.example.mtg_decks.ui.viewmodel.RandomCardState
import com.example.mtg_decks.ui.viewmodel.RandomCardViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomCardScreen(
    onCardClick: (String) -> Unit,
    viewModel: RandomCardViewModel = hiltViewModel(),
) {
    val cardState by viewModel.cardState.collectAsState()

    var isFlipped by remember { mutableStateOf(true) }
    var offsetY by remember { mutableStateOf(0f) }
    var rotation by remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(1f) }
    var isAnimatingOut by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.getRandomCard()
    }

    // Animationen
    val flipRotation =
        animateFloatAsState(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec =
                tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing,
                ),
        )

    val offsetYAnimated =
        animateFloatAsState(
            targetValue = offsetY,
            animationSpec =
                tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing,
                ),
            finishedListener = {
                if (isAnimatingOut) {
                    isVisible = false
                    kotlinx.coroutines.MainScope().launch {
                        viewModel.getRandomCard()
                        delay(50)
                        isAnimatingOut = false
                        offsetY = 0f
                        rotation = 0f
                        scale = 1f
                        isFlipped = true
                        isVisible = true
                    }
                }
            },
        )

    val spinRotation =
        animateFloatAsState(
            targetValue = rotation,
            animationSpec =
                tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing,
                ),
        )

    val scaleAnimated =
        animateFloatAsState(
            targetValue = scale,
            animationSpec =
                tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing,
                ),
        )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Random Card") },
                actions = {
                    IconButton(
                        onClick = {
                            isAnimatingOut = true
                            offsetY = -1000f
                            rotation += 360f
                            scale = 0f
                        },
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Get New Random Card",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            when (val state = cardState) {
                is RandomCardState.Loading -> {
                    CircularProgressIndicator()
                }
                is RandomCardState.Success -> {
                    if (isVisible) {
                        Box(
                            modifier =
                                Modifier
                                    .width(300.dp)
                                    .height(420.dp)
                                    .offset { IntOffset(0, offsetYAnimated.value.toInt()) }
                                    .pointerInput(Unit) {
                                        detectDragGestures(
                                            onDragEnd = {
                                                if (abs(offsetY) > size.height / 4) {
                                                    isAnimatingOut = true
                                                    offsetY = -size.height.toFloat() * 2
                                                    rotation += 360f
                                                    scale = 0f
                                                } else {
                                                    offsetY = 0f
                                                }
                                            },
                                            onDrag = { change, dragAmount ->
                                                if (!isAnimatingOut) {
                                                    change.consume()
                                                    offsetY += dragAmount.y
                                                    rotation = (offsetY / size.height) * 45f
                                                    scale = 1f - abs(offsetY / size.height) * 0.2f
                                                }
                                            },
                                        )
                                    }.pointerInput(Unit) {
                                        detectTapGestures {
                                            if (!isAnimatingOut) {
                                                isFlipped = !isFlipped // Neu: Karte umdrehen
                                            }
                                        }
                                    }.graphicsLayer {
                                        rotationY = flipRotation.value
                                        rotationZ = spinRotation.value
                                        scaleX = scaleAnimated.value
                                        scaleY = scaleAnimated.value
                                        cameraDistance = 8 * density
                                    },
                        ) {
                            // Vorderseite
                            AsyncImage(
                                model = state.card.imageUrls?.large,
                                contentDescription = state.card.name,
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .graphicsLayer {
                                            alpha = if (flipRotation.value <= 90f) 1f else 0f
                                        },
                            )

                            // Rückseite
                            Image(
                                painter = painterResource(id = R.drawable.card_back), // Dein Kartenhintergrund
                                contentDescription = "Card Back",
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .graphicsLayer {
                                            rotationY = 180f
                                            alpha = if (flipRotation.value > 90f) 1f else 0f
                                        },
                            )
                        }
                    }
                }
                is RandomCardState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                else -> { /* Initial state */ }
            }

            FloatingActionButton(
                onClick = {
                    isAnimatingOut = true
                    offsetY = -1000f
                    rotation += 360f
                    scale = 0f
                },
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
            ) {
                Icon(Icons.Default.Refresh, "New Random Card")
            }
        }
    }
}
