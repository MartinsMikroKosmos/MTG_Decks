package com.example.mtg_decks.di

import com.example.mtg_decks.data.repository.CardRepository
import com.example.mtg_decks.domain.usecase.GetCardDetailsUseCase
import com.example.mtg_decks.domain.usecase.GetCardSuggestionsUseCase
import com.example.mtg_decks.domain.usecase.GetCardsUseCase
import com.example.mtg_decks.domain.usecase.GetRandomCardUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetCardsUseCase(repository: CardRepository): GetCardsUseCase = GetCardsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetRandomCardUseCase(repository: CardRepository): GetRandomCardUseCase = GetRandomCardUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCardDetailsUseCase(repository: CardRepository): GetCardDetailsUseCase = GetCardDetailsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCardSuggestionsUseCase(repository: CardRepository): GetCardSuggestionsUseCase = GetCardSuggestionsUseCase(repository)
}
