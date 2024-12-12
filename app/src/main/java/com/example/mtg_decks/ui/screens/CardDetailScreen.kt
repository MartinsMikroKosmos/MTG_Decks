package com.example.mtg_decks.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mtg_decks.R
import com.example.mtg_decks.ui.viewmodel.CardDetailState
import com.example.mtg_decks.ui.viewmodel.CardDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    cardId: String,
    onBackClick: () -> Unit,
    viewModel: CardDetailViewModel = hiltViewModel(),
) {
    var isFlipped by remember { mutableStateOf(true) }

    LaunchedEffect(cardId) {
        viewModel.loadCard(cardId)
    }

    val cardState by viewModel.cardState.collectAsState()

    val flipRotation =
        animateFloatAsState(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec =
                tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing,
                ),
        )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Card Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Check, contentDescription = "Back")
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
                is CardDetailState.Loading -> {
                    CircularProgressIndicator()
                }
                is CardDetailState.Success -> {
                    Box(
                        modifier =
                            Modifier
                                .width(300.dp)
                                .height(420.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        isFlipped = !isFlipped
                                    }
                                }.graphicsLayer {
                                    rotationY = flipRotation.value
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
                            painter = painterResource(id = R.drawable.card_back),
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
                is CardDetailState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                else -> { /* Initial state */ }
            }
        }
    }
}
