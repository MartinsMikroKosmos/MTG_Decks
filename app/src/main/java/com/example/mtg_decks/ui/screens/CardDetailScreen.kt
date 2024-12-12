package com.example.mtg_decks.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mtg_decks.domain.model.Card
import com.example.mtg_decks.ui.viewmodel.CardDetailState
import com.example.mtg_decks.ui.viewmodel.CardDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun CardDetailScreen(
    onBackClick: () -> Unit,
    viewModel: CardDetailViewModel = hiltViewModel(),
) {
    val cardState by viewModel.cardState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Card Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Build, contentDescription = "Back")
                    }
                },
            )
        },
    ) { paddingValues ->
        when (val state = cardState) {
            is CardDetailState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            is CardDetailState.Success -> {
                FlipCardDetail(
                    card = state.card,
                    modifier = Modifier.padding(paddingValues),
                )
            }
            is CardDetailState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            else -> {} // Handle initial state
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun FlipCardDetail(
    card: Card,
    modifier: Modifier = Modifier,
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation =
        animateFloatAsState(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec =
                tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing,
                ),
        )

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
                    .clickable { isFlipped = !isFlipped }
                    .graphicsLayer {
                        rotationY = rotation.value
                        cameraDistance = 8 * density
                    },
        ) {
            // Vorderseite
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = if (rotation.value <= 90f) 1f else 0f
                        },
            ) {
                AsyncImage(
                    model = card.imageUrls?.large,
                    contentDescription = card.name,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            // Rückseite (Kartendetails)
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = 180f
                            alpha = if (rotation.value > 90f) 1f else 0f
                        },
            ) {
                ElevatedCard(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = card.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )

                        card.manaCost?.let { cost ->
                            Text("Mana Cost: $cost")
                        }

                        card.typeLine?.let { type ->
                            Text(type)
                        }

                        card.oracleText?.let { text ->
                            Text(text)
                        }

                        if (card.power != null && card.toughness != null) {
                            Text("P/T: ${card.power}/${card.toughness}")
                        }

                        // Set Information
                        Text(
                            text = "Set: ${card.setName} (${card.set})",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = "Rarity: ${card.rarity}",
                            style = MaterialTheme.typography.bodyMedium,
                        )

                        // Prices
                        card.prices?.let { prices ->
                            Text(
                                text = "Prices:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            prices.usd?.let { Text("USD: $$it") }
                            prices.usdFoil?.let { Text("USD Foil: $$it") }
                            prices.eur?.let { Text("EUR: €$it") }
                            prices.eurFoil?.let { Text("EUR Foil: €$it") }
                        }
                    }
                }
            }
        }

        // Scrollbare Details unterhalb der Karte
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
        ) {
            Text(
                text = "Tap card to see details",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}
