package com.example.mtg_decks.domain.model

data class Card(
    val id: String,
    val name: String,
    val imageUrls: ImageUrls?,
    val manaCost: String?,
    val cmc: Double?,
    val typeLine: String?,
    val oracleText: String?,
    val power: String?,
    val toughness: String?,
    val colors: List<String>?,
    val colorIdentity: List<String>,
    val set: String,
    val setName: String,
    val rarity: String,
    val prices: Prices?,
)

data class ImageUrls(
    val small: String?,
    val normal: String?,
    val large: String?,
    val png: String?,
    val artCrop: String?,
    val borderCrop: String?,
)

data class Prices(
    val usd: Double?,
    val usdFoil: Double?,
    val eur: Double?,
    val eurFoil: Double?,
)
