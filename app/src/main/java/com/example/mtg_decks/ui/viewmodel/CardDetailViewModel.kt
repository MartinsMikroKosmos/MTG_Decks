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
class CardDetailViewModel
    @Inject
    constructor(
        private val repository: CardRepository,
    ) : ViewModel() {
        private val _cardState = MutableStateFlow<CardDetailState>(CardDetailState.Initial)
        val cardState: StateFlow<CardDetailState> = _cardState.asStateFlow()

        fun loadCard(cardId: String) {
            viewModelScope.launch {
                repository.getCard(cardId).collect { result ->
                    _cardState.value =
                        when (result) {
                            is NetworkResult.Success -> CardDetailState.Success(result.data)
                            is NetworkResult.Error -> CardDetailState.Error(result.exception.message ?: "Unknown error")
                            is NetworkResult.Loading -> CardDetailState.Loading
                        }
                }
            }
        }
    }

sealed class CardDetailState {
    object Initial : CardDetailState()

    object Loading : CardDetailState()

    data class Success(
        val card: Card,
    ) : CardDetailState()

    data class Error(
        val message: String,
    ) : CardDetailState()
}
