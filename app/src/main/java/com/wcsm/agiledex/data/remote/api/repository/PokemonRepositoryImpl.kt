package com.wcsm.agiledex.data.remote.api.repository

import com.wcsm.agiledex.data.remote.api.PokeAPIService
import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.repository.PokemonRepository
import com.wcsm.agiledex.mappers.toPokemon
import com.wcsm.agiledex.mappers.toPokemonDetails
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokeAPIService: PokeAPIService
) : PokemonRepository {
    private val actualOffsetPage = 0

    override suspend fun getPokemons(): List<Pokemon> {
        try {
            val result = pokeAPIService.getPokemons(actualOffsetPage, 20)
            if(result.isSuccessful && result.body() != null) {
                val pokemonList = result.body()?.results
                if(pokemonList != null) {
                    return pokemonList.map { it.toPokemon() }
                }
            }
        } catch (error: Exception) {
            error.printStackTrace()
        }
        return emptyList()
    }

    override suspend fun getPokemonDetailsByName(pokemonName: String): PokemonDetails? {
        try {
            val result = pokeAPIService.getPokemonDetails(pokemonName)
            if(result.isSuccessful && result.body() != null) {
                val pokemonDetails = result.body()
                if(pokemonDetails != null) return pokemonDetails.toPokemonDetails()
            }
        } catch (error: Exception) {
            error.printStackTrace()
        }

        return null
    }
}