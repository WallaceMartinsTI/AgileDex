package com.wcsm.agiledex.domain.mapper

import com.wcsm.agiledex.data.remote.api.dto.pokemons.PokemonDTO
import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.utils.capitalizeFirstLetter

fun PokemonDTO.toPokemon() : Pokemon {
    val currentTime = System.currentTimeMillis()
    return Pokemon(
        id = "${this.name}+${currentTime}",
        name = capitalizeFirstLetter(this.name),
        spriteUrl = getPokemonImage(getLastValueFromUrl(this.url))
    )
}

private fun getPokemonImage(imageUrl: String) : String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$imageUrl.png"
}

private fun getLastValueFromUrl(url: String): String {
    return url.split("/").dropLast(1).last()
}

