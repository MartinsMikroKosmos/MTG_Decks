package com.example.mtg_decks.util

sealed class NetworkResult<out T> {
    data class Success<T>(
        val data: T,
    ) : NetworkResult<T>()

    data class Error(
        val exception: Exception,
    ) : NetworkResult<Nothing>()

    object Loading : NetworkResult<Nothing>()
}
