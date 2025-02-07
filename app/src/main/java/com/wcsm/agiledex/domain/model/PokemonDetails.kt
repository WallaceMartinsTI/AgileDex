package com.wcsm.agiledex.domain.model

data class PokemonDetails(
    val id: Int,
    val order: Int,
    val name: String,
    val baseExperience: Int,
    val types: List<String>,
    val weight: Int,
    val height: Int,
    val baseStats: List<PokemonStats>
)