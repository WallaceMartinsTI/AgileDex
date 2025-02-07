package com.wcsm.agiledex.domain.repository

import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.PokemonDetails

interface PokemonRepository {
    suspend fun getPokemons(): List<Pokemon>
    suspend fun getPokemonDetailsByName(pokemonName: String): PokemonDetails?
}