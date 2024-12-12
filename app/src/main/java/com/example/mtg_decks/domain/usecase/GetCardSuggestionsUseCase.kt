package com.example.mtg_decks.domain.usecase

import com.example.mtg_decks.data.repository.CardRepository
import com.example.mtg_decks.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class GetCardSuggestionsUseCase
    @Inject
    constructor(
        private val repository: CardRepository,
    ) {
        suspend operator fun invoke(query: String): Flow<NetworkResult<List<String>>> =
            repository
                .autocomplete(query)
                .catch { e ->
                    emit(NetworkResult.Error(Exception("Failed to load suggestions: ${e.message}")))
                }
    }
