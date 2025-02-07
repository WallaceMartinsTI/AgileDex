package com.wcsm.agiledex.presentation.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.model.Response
import com.wcsm.agiledex.domain.usecase.GetPokemonDetailsByNameUseCase
import com.wcsm.agiledex.domain.usecase.GetPokemonListUseCase
import com.wcsm.agiledex.presentation.model.PokemonOperationType
import com.wcsm.agiledex.presentation.model.UiState
import com.wcsm.agiledex.presentation.ui.theme.DarkGrayColor
import com.wcsm.agiledex.utils.getPokemonTypeColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonDetailsByNameUseCase: GetPokemonDetailsByNameUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState<PokemonOperationType>())
    val uiState = _uiState.asStateFlow()

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList = _pokemonList.asStateFlow()

    private val _pokemonDetails = MutableStateFlow<PokemonDetails?>(null)
    val pokemonDetails = _pokemonDetails.asStateFlow()

    init {
        getPokemons()
    }

    fun resetUiState() {
        _uiState.value = UiState()
    }

    fun resetPokemonDetails() {
        _pokemonDetails.value = null
    }

    private fun getPokemons() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                pokemonOperationType = PokemonOperationType.GET_POKEMONS
            )

            getPokemonListUseCase().collect { getPokemonListResponse ->
                when(getPokemonListResponse) {
                    is Response.Loading -> {
                        _uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }
                    is Response.Error -> {
                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            error = getPokemonListResponse.message
                        )
                    }
                    is Response.Success -> {
                        val updatedPokemonList = mutableListOf<Pokemon>()

                        getPokemonListResponse.data.map { pokemon ->
                            getPokemonDetailsByNameUseCase(pokemon.name).collect { pokemonDetailsResponse ->
                                when(pokemonDetailsResponse) {
                                    is Response.Loading -> {
                                        _uiState.value = uiState.value.copy(
                                            isLoading = true
                                        )
                                    }
                                    is Response.Error -> {
                                        _uiState.value = uiState.value.copy(
                                            isLoading = false,
                                            error = pokemonDetailsResponse.message
                                        )
                                    }
                                    is Response.Success -> {
                                        val pokemonMainType = pokemonDetailsResponse.data?.types
                                        if(!pokemonMainType.isNullOrEmpty()) {
                                            updatedPokemonList.add(
                                                pokemon.copy(
                                                    typeColor = getPokemonTypeColor(
                                                        pokemonMainType[0],
                                                        DarkGrayColor
                                                    )
                                                )
                                            )
                                        } else {
                                            updatedPokemonList.add(pokemon)
                                        }
                                    }
                                }
                            }
                        }

                        _pokemonList.value = updatedPokemonList
                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            success = true
                        )
                    }
                }
            }
        }
    }

    fun getPokemonDetails(pokemonName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                pokemonOperationType = PokemonOperationType.GET_POKEMON_DETAILS
            )

            getPokemonDetailsByNameUseCase(pokemonName).collect { result ->
                when(result) {
                    is Response.Loading -> {
                        _uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }
                    is Response.Error -> {
                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                    is Response.Success -> {
                        val pokemonDetailsResult = result.data?.copy(
                            typeColor = if(result.data.types.isNotEmpty()) {
                                getPokemonTypeColor(result.data.types[0], DarkGrayColor)
                            } else {
                                getPokemonTypeColor(null, DarkGrayColor)
                            }
                        )

                        _pokemonDetails.value = pokemonDetailsResult

                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            success = true
                        )
                    }
                }
            }
        }
    }


    /*fun getPokemons() {
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
    }*/
}