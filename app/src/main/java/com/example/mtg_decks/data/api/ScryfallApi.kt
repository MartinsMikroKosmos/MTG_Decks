package com.example.mtg_decks.data.api

import com.example.mtg_decks.data.api.model.AutocompleteResponse
import com.example.mtg_decks.data.api.model.CardDto
import com.example.mtg_decks.data.api.model.CardSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScryfallApi {
    @GET("cards/search")
    suspend fun searchCards(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("order") order: String = "name",
    ): Response<CardSearchResponse>

    @GET("cards/random")
    suspend fun getRandomCard(): Response<CardDto>

    @GET("cards/{id}")
    suspend fun getCard(
        @Path("id") cardId: String,
    ): Response<CardDto>

    @GET("cards/autocomplete")
    suspend fun autocomplete(
        @Query("q") query: String,
    ): Response<AutocompleteResponse>
}
