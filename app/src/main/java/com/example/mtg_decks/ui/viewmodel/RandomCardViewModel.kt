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
class RandomCardViewModel
    @Inject
    constructor(
        private val repository: CardRepository,
    ) : ViewModel() {
        private val _cardState = MutableStateFlow<RandomCardState>(RandomCardState.Initial)
        val cardState: StateFlow<RandomCardState> = _cardState.asStateFlow()

        fun getRandomCard() {
            viewModelScope.launch {
                repository.getRandomCard().collect { result ->
                    _cardState.value =
                        when (result) {
                            is NetworkResult.Success -> RandomCardState.Success(result.data)
                            is NetworkResult.Error -> RandomCardState.Error(result.exception.message ?: "Unknown error")
                            is NetworkResult.Loading -> RandomCardState.Loading
                        }
                }
            }
        }
    }

sealed class RandomCardState {
    object Initial : RandomCardState()

    object Loading : RandomCardState()

    data class Success(
        val card: Card,
    ) : RandomCardState()

    data class Error(
        val message: String,
    ) : RandomCardState()
}
