package com.wcsm.agiledex.domain.model

import androidx.compose.ui.graphics.Color
import com.wcsm.agiledex.utils.getPokemonTypeColor

data class Pokemon(
    val name: String,
    val spriteUrl: String,
    var typeColor: Color = getPokemonTypeColor("")
)
