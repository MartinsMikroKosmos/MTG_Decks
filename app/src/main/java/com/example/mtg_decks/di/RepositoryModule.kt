package com.example.mtg_decks.di

import com.example.mtg_decks.data.repository.CardRepository
import com.example.mtg_decks.data.repository.CardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCardRepository(cardRepositoryImpl: CardRepositoryImpl): CardRepository
}
