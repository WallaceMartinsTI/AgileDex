package com.wcsm.agiledex.domain.mapper

import com.wcsm.agiledex.data.remote.api.dto.pokemonDetails.PokemonDetailsDTO
import com.wcsm.agiledex.data.remote.api.dto.pokemonDetails.Stat
import com.wcsm.agiledex.data.remote.api.dto.pokemonDetails.Type
import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.model.PokemonStats
import com.wcsm.agiledex.utils.capitalizeFirstLetter

fun PokemonDetailsDTO.toPokemonDetails(): PokemonDetails {
    val types = getPokemonTypes(this.types)
    val baseStats = getPokemonStats(this.stats)

    return PokemonDetails(
        id = this.id,
        order = this.order,
        name = capitalizeFirstLetter(this.name),
        baseExperience = this.base_experience,
        types = types,
        weight = this.weight,
        height = this.height,
        baseStats = baseStats
    )
}

private fun getPokemonTypes(types: List<Type>): List<String> {
    val filteredTypes = mutableListOf<String>()

    types.forEachIndexed { index, type ->
        if(index < 2) {
            filteredTypes.add(type.type.name)
        }
    }

    return filteredTypes
}

private fun getPokemonStats(stats: List<Stat>): List<PokemonStats> {
    val filteredStats = mutableListOf<PokemonStats>()

    stats.forEach {  stat ->
        if(stat.stat.name in listOf("hp", "attack", "defense", "speed")) {
            val pokemonStats = PokemonStats(
                name = stat.stat.name,
                progress = Pair(stat.base_stat, 300)
            )
            filteredStats.add(pokemonStats)
        }
    }

    return filteredStats
}