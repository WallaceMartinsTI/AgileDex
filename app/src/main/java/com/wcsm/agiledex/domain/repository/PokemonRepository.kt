package com.wcsm.agiledex.domain.repository

import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun getPokemons(offset: Int, limit: Int): Flow<Response<List<Pokemon>>>
    suspend fun getPokemonDetailsByName(pokemonName: String): Flow<Response<PokemonDetails?>>
}