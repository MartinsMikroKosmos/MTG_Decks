package com.example.mtg_decks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mtg_decks.data.repository.CardRepository
import com.example.mtg_decks.domain.model.Card
import com.example.mtg_decks.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardSearchViewModel
    @Inject
    constructor(
        private val repository: CardRepository,
    ) : ViewModel() {
        private val _searchState = MutableStateFlow<SearchState>(SearchState.Initial)
        val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

        fun onSearchQueryChange(query: String) {
            _searchQuery.value = query
            if (query.length >= 2) {
                searchCards(query)
            }
        }

        fun searchCards(query: String) {
            viewModelScope.launch {
                repository.searchCards(query).collect { result ->
                    _searchState.value =
                        when (result) {
                            is NetworkResult.Success -> SearchState.Success(result.data)
                            is NetworkResult.Error -> SearchState.Error(result.exception.message ?: "Unknown error")
                            is NetworkResult.Loading -> SearchState.Loading
                        }
                }
            }
        }
    }

sealed class SearchState {
    object Initial : SearchState()

    object Loading : SearchState()

    data class Success(
        val cards: List<Card>,
    ) : SearchState()

    data class Error(
        val message: String,
    ) : SearchState()
}
