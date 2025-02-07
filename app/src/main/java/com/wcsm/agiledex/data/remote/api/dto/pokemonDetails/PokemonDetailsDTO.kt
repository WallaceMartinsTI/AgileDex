package com.wcsm.agiledex.data.remote.api.dto.pokemonDetails

data class PokemonDetailsDTO(
    val base_experience: Int,
    val height: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val species: Species,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Int
)