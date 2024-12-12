package com.example.mtg_decks.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mtg_decks.ui.viewmodel.RandomCardState
import com.example.mtg_decks.ui.viewmodel.RandomCardViewModel

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomCardScreen(
    onCardClick: (String) -> Unit,
    viewModel: RandomCardViewModel = hiltViewModel(),
) {
    val cardState by viewModel.cardState.collectAsState()
    var isFlipped by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getRandomCard()
    }

    val rotation =
        animateFloatAsState(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec =
                tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing,
                ),
            finishedListener = {
                if (isFlipped) {
                    // Wenn die Flip-Animation beendet ist, neue Karte laden
                    viewModel.getRandomCard()
                    isFlipped = false // Karte zurückdrehen
                }
            },
        )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Random Card") },
                actions = {
                    IconButton(onClick = { isFlipped = true }) {
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
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth(0.8f)
                                .aspectRatio(0.7f)
                                .clickable {
                                    // Beim Klick auf die Karte Details anzeigen
                                    onCardClick(state.card.id)
                                }.graphicsLayer {
                                    rotationY = rotation.value
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
                                        alpha = if (rotation.value <= 90f) 1f else 0f
                                    },
                        )

                        // Rückseite (MTG Card Back)
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        rotationY = 180f
                                        alpha = if (rotation.value > 90f) 1f else 0f
                                    },
                        ) {
                            // Hier könntest du ein Bild der MTG-Kartenrückseite anzeigen
                            // Oder einen Platzhalter
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.primary,
                            ) {
                                // MTG Card Back Design
                            }
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

            // Floating Action Button für neue Karte
            FloatingActionButton(
                onClick = { isFlipped = true },
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
