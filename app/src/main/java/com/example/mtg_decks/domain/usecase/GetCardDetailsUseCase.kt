package com.example.mtg_decks.domain.usecase

import com.example.mtg_decks.data.repository.CardRepository
import com.example.mtg_decks.domain.model.Card
import com.example.mtg_decks.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class GetCardDetailsUseCase
    @Inject
    constructor(
        private val repository: CardRepository,
    ) {
        suspend operator fun invoke(cardId: String): Flow<NetworkResult<Card>> =
            repository
                .getCard(cardId)
                .catch { e ->
                    emit(NetworkResult.Error(Exception("Failed to load card details: ${e.message}")))
                }
    }
