package com.example.mtg_decks.data.repository

import com.example.mtg_decks.data.api.ScryfallApi
import com.example.mtg_decks.data.api.model.CardDto
import com.example.mtg_decks.data.mapper.toDomain
import com.example.mtg_decks.domain.model.Card
import com.example.mtg_decks.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.emptyList

interface CardRepository {
    suspend fun searchCards(query: String): Flow<NetworkResult<List<Card>>>

    suspend fun getRandomCard(): Flow<NetworkResult<Card>>

    suspend fun getCard(cardId: String): Flow<NetworkResult<Card>>

    suspend fun autocomplete(query: String): Flow<NetworkResult<List<String>>>
}

@Singleton
class CardRepositoryImpl
    @Inject
    constructor(
        private val api: ScryfallApi,
    ) : CardRepository {
        override suspend fun searchCards(query: String) =
            flow {
                emit(NetworkResult.Loading)
                try {
                    val response = api.searchCards(query)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val cardList =
                            if (responseBody != null) {
                                responseBody.data.map { card: CardDto -> card.toDomain() }
                            } else {
                                emptyList<Card>() // Expliziter Typ für emptyList
                            }
                        emit(NetworkResult.Success(cardList))
                    } else {
                        emit(NetworkResult.Error(Exception("API error: ${response.code()}")))
                    }
                } catch (e: Exception) {
                    emit(NetworkResult.Error(e))
                }
            }

        override suspend fun getRandomCard() =
            flow {
                emit(NetworkResult.Loading)
                try {
                    val response = api.getRandomCard()
                    if (response.isSuccessful) {
                        val card =
                            response.body()?.toDomain()
                                ?: throw Exception("Card data is null")
                        emit(NetworkResult.Success(card))
                    } else {
                        emit(NetworkResult.Error(Exception("API error: ${response.code()}")))
                    }
                } catch (e: Exception) {
                    emit(NetworkResult.Error(e))
                }
            }

        override suspend fun getCard(cardId: String) =
            flow {
                emit(NetworkResult.Loading)
                try {
                    val response = api.getCard(cardId)
                    if (response.isSuccessful) {
                        val card =
                            response.body()?.toDomain()
                                ?: throw Exception("Card data is null")
                        emit(NetworkResult.Success(card))
                    } else {
                        emit(NetworkResult.Error(Exception("API error: ${response.code()}")))
                    }
                } catch (e: Exception) {
                    emit(NetworkResult.Error(e))
                }
            }

        override suspend fun autocomplete(query: String) =
            flow {
                emit(NetworkResult.Loading)
                try {
                    val response = api.autocomplete(query)
                    if (response.isSuccessful) {
                        val suggestions = response.body()?.data ?: emptyList()
                        emit(NetworkResult.Success(suggestions))
                    } else {
                        emit(NetworkResult.Error(Exception("API error: ${response.code()}")))
                    }
                } catch (e: Exception) {
                    emit(NetworkResult.Error(e))
                }
            }
    }
