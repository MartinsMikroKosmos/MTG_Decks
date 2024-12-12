package com.example.mtg_decks.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mtg_decks.domain.model.Card
import com.example.mtg_decks.ui.viewmodel.CardSearchViewModel
import com.example.mtg_decks.ui.viewmodel.SearchState

@Composable
fun SearchScreen(
    onCardClick: (String) -> Unit,
    viewModel: CardSearchViewModel = hiltViewModel(),
) {
    val searchState by viewModel.searchState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = searchState) {
            is SearchState.Initial -> {
                // Zeige initial Screen
                InitialScreen()
            }
            is SearchState.Loading -> {
                // Zeige Ladeindikator
                LoadingIndicator()
            }
            is SearchState.Success -> {
                // Zeige Suchergebnisse
                CardGrid(
                    cards = state.cards,
                    onCardClick = onCardClick,
                )
            }
            is SearchState.Error -> {
                // Zeige Fehlermeldung
                ErrorMessage(message = state.message)
            }
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = modifier,
        placeholder = { Text("Search cards...") },
        singleLine = true,
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
    )
}

@Composable
private fun CardGrid(
    cards: List<Card>,
    onCardClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(cards) { card ->
            CardItem(
                card = card,
                onClick = { onCardClick(card.id) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardItem(
    card: Card,
    onClick: () -> Unit,
) {
    ElevatedCard( // oder Card, je nachdem welchen Stil du bevorzugst
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f),
        onClick = onClick,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = card.imageUrls?.normal,
                contentDescription = card.name,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
private fun InitialScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Search for Magic: The Gathering cards",
            style = MaterialTheme.typography.titleLarge,
        )
    }
}
