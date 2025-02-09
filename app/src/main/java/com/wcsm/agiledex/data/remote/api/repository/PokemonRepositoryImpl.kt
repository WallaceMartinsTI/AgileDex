package com.wcsm.agiledex.data.remote.api.repository

import com.wcsm.agiledex.data.remote.api.PokeAPIService
import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.model.Response
import com.wcsm.agiledex.domain.repository.PokemonRepository
import com.wcsm.agiledex.domain.mapper.toPokemon
import com.wcsm.agiledex.domain.mapper.toPokemonDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokeAPIService: PokeAPIService
) : PokemonRepository {
    override suspend fun getPokemons(offset: Int, limit: Int): Flow<Response<List<Pokemon>>> = flow {
        try {
            emit(Response.Loading)
            delay(2000)
            val response = pokeAPIService.getPokemons(offset, limit)
            if(response.isSuccessful && response.body() != null) {
                val pokemonList = response.body()?.results
                if(pokemonList != null) {
                    if(pokemonList.isEmpty()) {
                        emit(Response.Error("Error getting pokemon list: pokemon list returned is empty."))
                    } else {
                        emit(Response.Success(pokemonList.map { it.toPokemon() }))
                    }
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

            //delay(2000)
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
}