package com.wcsm.agiledex.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.repository.PokemonRepository
import com.wcsm.agiledex.utils.getPokemonTypeColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList = _pokemonList.asStateFlow()

    private val _pokemonDetails = MutableStateFlow<PokemonDetails?>(null)
    val pokemonDetails = _pokemonDetails.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _errorState = MutableStateFlow(false)
    val errorState = _errorState.asStateFlow()

    init {
        getPokemons()
    }

    fun getPokemons() {
        viewModelScope.launch(Dispatchers.IO) {
            //delay(1500)
            val response = pokemonRepository.getPokemons()

            if (response.isNotEmpty()) {
                val updatedPokemonList = response.map { pokemon ->
                    val pokemonTypes = pokemonRepository.getPokemonDetailsByName(pokemon.name)?.types
                    if (!pokemonTypes.isNullOrEmpty()) {
                        pokemon.copy(typeColor = getPokemonTypeColor(pokemonTypes[0]))
                    } else {
                        pokemon
                    }
                }

                _pokemonList.value = updatedPokemonList
            }

            if (response.isEmpty()) {
                delay(5000)
                if (_pokemonList.value.isEmpty()) {
                    _errorState.value = true
                }
            }

            _isLoading.value = false
        }
    }

    fun getPokemonDetails(pokemonName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = pokemonRepository.getPokemonDetailsByName(pokemonName)
            _pokemonDetails.value = response
        }
    }
}