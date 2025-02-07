package com.wcsm.agiledex.data.remote.api.dto.pokemons

data class PokeAPIResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<PokemonDTO>
)