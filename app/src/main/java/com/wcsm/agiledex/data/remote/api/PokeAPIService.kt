package com.wcsm.agiledex.data.remote.api

import com.wcsm.agiledex.data.remote.api.dto.pokemonDetails.PokemonDetailsDTO
import com.wcsm.agiledex.data.remote.api.dto.pokemons.PokeAPIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeAPIService {
    @GET("pokemon/")
    suspend fun getPokemons(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ) : Response<PokeAPIResponse>

    @GET("pokemon/{pokemon_name}/")
    suspend fun getPokemonDetails(
        @Path("pokemon_name") pokemonName: String
    ) : Response<PokemonDetailsDTO>
}