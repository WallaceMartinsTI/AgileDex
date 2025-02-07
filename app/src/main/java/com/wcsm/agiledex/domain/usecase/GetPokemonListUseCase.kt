package com.wcsm.agiledex.domain.usecase

import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.Response
import com.wcsm.agiledex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(): Flow<Response<List<Pokemon>>> {
        return pokemonRepository.getPokemons()
    }
}