package com.example.mtg_decks.data.mapper

import com.example.mtg_decks.data.api.model.CardDto
import com.example.mtg_decks.data.api.model.ImageUrisDto
import com.example.mtg_decks.data.api.model.PricesDto
import com.example.mtg_decks.domain.model.Card
import com.example.mtg_decks.domain.model.ImageUrls
import com.example.mtg_decks.domain.model.Prices

fun CardDto.toDomain(): Card =
    Card(
        id = id,
        name = name,
        imageUrls = image_uris?.toDomain(),
        manaCost = mana_cost,
        cmc = cmc,
        typeLine = type_line,
        oracleText = oracle_text,
        power = power,
        toughness = toughness,
        colors = colors,
        colorIdentity = color_identity,
        set = set,
        setName = set_name,
        rarity = rarity,
        prices = priceInfo.toDomain(),
    )

fun ImageUrisDto.toDomain(): ImageUrls =
    ImageUrls(
        small = small,
        normal = normal,
        large = large,
        png = png,
        artCrop = art_crop,
        borderCrop = border_crop,
    )

fun PricesDto.toDomain(): Prices =
    Prices(
        usd = usd?.toDoubleOrNull(),
        usdFoil = usd_foil?.toDoubleOrNull(),
        eur = eur?.toDoubleOrNull(),
        eurFoil = eur_foil?.toDoubleOrNull(),
    )
