package com.wcsm.agiledex.data.remote.api.repository

import com.wcsm.agiledex.data.remote.api.PokeAPIService
import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.model.Response
import com.wcsm.agiledex.domain.repository.PokemonRepository
import com.wcsm.agiledex.mappers.toPokemon
import com.wcsm.agiledex.mappers.toPokemonDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokeAPIService: PokeAPIService
) : PokemonRepository {
    private val actualOffsetPage = 0

    override suspend fun getPokemons(): Flow<Response<List<Pokemon>>> = flow {
        try {
            emit(Response.Loading)

            val response = pokeAPIService.getPokemons(actualOffsetPage, 20) // 20, I'm using 5 for test
            if(response.isSuccessful && response.body() != null) {
                val pokemonList = response.body()?.results
                if(pokemonList != null) {
                    emit(Response.Success(pokemonList.map { it.toPokemon() }))
                } else {
                    emit(Response.Error("Error getting pokemon list: null pokemon list."))
                }
            } else {
                emit(Response.Error("Error getting pokemon list: Request failed."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Unknown error while searching for pokemons, please inform the administrator."))
        }
    }

    override suspend fun getPokemonDetailsByName(
        pokemonName: String
    ): Flow<Response<PokemonDetails?>> = flow {
        try {
            emit(Response.Loading)

            delay(2000)
            val response = pokeAPIService.getPokemonDetails(pokemonName)
            if(response.isSuccessful && response.body() != null) {
                val pokemonDetails = response.body()
                if(pokemonDetails != null) {
                    emit(Response.Success(pokemonDetails.toPokemonDetails()))
                } else {
                    emit(Response.Error("Error getting pokemon details: null pokemon details."))
                }
            } else {
                emit(Response.Error("Error getting pokemon details: Request failed."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Response.Error("Unknown error while fetching pokemon details, please inform the administrator."))
        }
    }


    /*override suspend fun getPokemons(): List<Pokemon> {
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
    }*/
}