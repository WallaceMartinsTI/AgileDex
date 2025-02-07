package com.wcsm.agiledex.domain.usecase

import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.model.Response
import com.wcsm.agiledex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonDetailsByNameUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(pokemonName: String): Flow<Response<PokemonDetails?>> {
        return pokemonRepository.getPokemonDetailsByName(pokemonName)
    }
}