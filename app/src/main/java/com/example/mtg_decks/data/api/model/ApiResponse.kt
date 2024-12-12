package com.example.mtg_decks.data.api.model

import com.google.gson.annotations.SerializedName

data class AutocompleteResponse(
    val data: List<String>,
    val total_values: Int,
)

data class CardSearchResponse(
    val data: List<CardDto>,
    val has_more: Boolean,
    val next_page: String?,
    val total_cards: Int,
)

data class CardDto(
    val id: String,
    val name: String,
    val lang: String,
    val released_at: String?,
    val image_uris: ImageUrisDto?,
    val mana_cost: String?,
    val cmc: Double?,
    val type_line: String?,
    val oracle_text: String?,
    val power: String?,
    val toughness: String?,
    val colors: List<String>?,
    val color_identity: List<String>,
    val legalities: LegalitiesDto,
    val set: String,
    val set_name: String,
    val rarity: String,
    @SerializedName("prices")
    val priceInfo: PricesDto,
)

data class ImageUrisDto(
    val small: String?,
    val normal: String?,
    val large: String?,
    val png: String?,
    val art_crop: String?,
    val border_crop: String?,
)

data class LegalitiesDto(
    val standard: String,
    val pioneer: String,
    val modern: String,
    val legacy: String,
    val vintage: String,
    val commander: String,
)

data class PricesDto(
    val usd: String?,
    val usd_foil: String?,
    val eur: String?,
    val eur_foil: String?,
)
