package com.wcsm.agiledex.domain.model

import androidx.compose.ui.graphics.Color
import com.wcsm.agiledex.presentation.ui.theme.DarkGrayColor
import com.wcsm.agiledex.utils.getPokemonTypeColor

data class PokemonDetails(
    val id: Int,
    val order: Int,
    val name: String,
    val baseExperience: Int,
    val types: List<String>,
    val weight: Int,
    val height: Int,
    val baseStats: List<PokemonStats>,
    var typeColor: Color = getPokemonTypeColor(null, DarkGrayColor)
)