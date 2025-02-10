package com.wcsm.agiledex.domain.usecase

import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.Response
import com.wcsm.agiledex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNextPokemonPageUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(offset: Int, limit: Int): Flow<Response<List<Pokemon>>> {
        return if(offset <= 270) {
            pokemonRepository.getPokemons(offset, limit)
        } else {
            flow {
                emit(Response.Success(emptyList()))
            }
        }
    }
}