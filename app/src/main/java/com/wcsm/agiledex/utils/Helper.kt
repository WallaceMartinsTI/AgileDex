package com.wcsm.agiledex.utils

import androidx.compose.ui.graphics.Color

fun capitalizeFirstLetter(text: String) : String {
    return text.lowercase().replaceFirstChar { it.uppercase() }
}

fun getPokemonTypeColor(type: String?, defaultColor: Color): Color {
    return when (type?.lowercase()) {
        "normal" -> Color(0xFFBBB86C)
        "fighting" -> Color(0xFFE03028)
        "flying" -> Color(0xFF9D7CFA)
        "poison" -> Color(0xFFC930D0)
        "ground" -> Color(0xFFF4C36A)
        "rock" -> Color(0xFFD1A942)
        "bug" -> Color(0xFFC5D71B)
        "ghost" -> Color(0xFF8D67AE)
        "steel" -> Color(0xFFC9C9E1)
        "fire" -> Color(0xFFFF6D1A)
        "water" -> Color(0xFF3090FF)
        "grass" -> Color(0xFF49D161)
        "electric" -> Color(0xFFFFD500)
        "psychic" -> Color(0xFFFF4081)
        "ice" -> Color(0xFF77E5E8)
        "dragon" -> Color(0xFF703CFF)
        "dark" -> Color(0xFF886750)
        "fairy" -> Color(0xFFFF92D0)
        "stellar" -> Color(0xFFFFA6B9)
        "shadow" -> Color(0xFF464646)
        "unknown" -> Color(0xFF60C2B0)
        else -> defaultColor
    }
}