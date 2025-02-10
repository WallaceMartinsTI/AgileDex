package com.wcsm.agiledex.presentation.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.model.Response
import com.wcsm.agiledex.domain.usecase.GetNextPokemonPageUseCase
import com.wcsm.agiledex.domain.usecase.GetPokemonDetailsByNameUseCase
import com.wcsm.agiledex.domain.usecase.GetPokemonListUseCase
import com.wcsm.agiledex.presentation.model.PokemonOperationType
import com.wcsm.agiledex.presentation.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonDetailsByNameUseCase: GetPokemonDetailsByNameUseCase,
    private val getNextPokemonPageUseCase: GetNextPokemonPageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState<PokemonOperationType>())
    val uiState = _uiState.asStateFlow()

    private val _originalPokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    private val originalPokemonList = _originalPokemonList.asStateFlow()

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList = _pokemonList.asStateFlow()

    private val _pokemonDetails = MutableStateFlow<PokemonDetails?>(null)
    val pokemonDetails = _pokemonDetails.asStateFlow()

    private val _isFetching = MutableStateFlow(false)
    val isFetching = _isFetching.asStateFlow()

    private val _isFirstScreenLoading = MutableStateFlow(false)
    val isFirstScreenLoading = _isFirstScreenLoading.asStateFlow()

    private val _isErrorGettingPokemons = MutableStateFlow(false)
    val isErrorGettingPokemons = _isErrorGettingPokemons.asStateFlow()

    private val _isErrorGettingPokemonDetails = MutableStateFlow(false)
    val isErrorGettingPokemonDetails = _isErrorGettingPokemonDetails.asStateFlow()

    private val _cachedPokemonDetails = MutableStateFlow<Map<String, PokemonDetails>>(emptyMap())

    private var offset = 0
    private var limit = 30
    private var maxPokemons = 300

    init {
        getPokemons()
    }

    fun resetUiState() {
        _uiState.value = UiState()
    }

    private fun updateUiState(newUiState: UiState<PokemonOperationType>) {
        _uiState.value = newUiState
        onUiStateChange()
    }

    private fun onUiStateChange() {
        _isFirstScreenLoading.value = pokemonList.value.isEmpty() &&
                uiState.value.isLoading &&
                uiState.value.pokemonOperationType == PokemonOperationType.GET_POKEMONS

        _isErrorGettingPokemons.value = !uiState.value.isLoading &&
                uiState.value.error?.isNotBlank() == true &&
                uiState.value.pokemonOperationType == PokemonOperationType.GET_POKEMONS

        _isErrorGettingPokemonDetails.value = !uiState.value.isLoading &&
                uiState.value.error?.isNotBlank() == true &&
                uiState.value.pokemonOperationType == PokemonOperationType.GET_POKEMON_DETAILS
    }

    fun resetPokemonDetails() {
        _pokemonDetails.value = null
    }

    fun getPokemonsByName(pokemonName: String) {
        if(pokemonName.isEmpty()) {
            _pokemonList.value = originalPokemonList.value
        } else {
            _pokemonList.value = originalPokemonList.value.filter {
                it.name.contains(pokemonName, ignoreCase = true)
            }
        }
    }

    fun getPokemons() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(
                pokemonOperationType = PokemonOperationType.GET_POKEMONS
            )

            getPokemonListUseCase(offset, limit).collect { getPokemonListResponse ->
                when(getPokemonListResponse) {
                    is Response.Loading -> {
                        updateUiState(uiState.value.copy(isLoading =  true))
                    }
                    is Response.Error -> {
                        updateUiState(uiState.value.copy(
                            isLoading =  false, error = getPokemonListResponse.message
                        ))
                    }
                    is Response.Success -> {
                        _pokemonList.value = getPokemonListResponse.data
                        _originalPokemonList.value = getPokemonListResponse.data
                        offset += limit

                        updateUiState(uiState.value.copy(isLoading =  false, success = true))
                    }
                }
            }
        }
    }

    fun getNextPokemonsPage() {
        if(isFetching.value || offset >= maxPokemons) return

        _isFetching.value = true

        viewModelScope.launch(Dispatchers.IO) {
            getNextPokemonPageUseCase(offset, limit).collect { getNextPokemonListResponse ->
                when(getNextPokemonListResponse) {
                    is Response.Loading -> {
                        updateUiState(uiState.value.copy(isLoading =  true))
                    }
                    is Response.Error -> {
                        updateUiState(uiState.value.copy(
                            isLoading =  false, error = getNextPokemonListResponse.message
                        ))
                    }

                    is Response.Success -> {
                        _originalPokemonList.value = pokemonList.value + getNextPokemonListResponse.data
                        _pokemonList.value =  pokemonList.value + getNextPokemonListResponse.data
                        offset += limit

                        updateUiState(uiState.value.copy(isLoading =  false, success = true))
                    }
                }
            }

            _isFetching.value = false
        }
    }

    fun getPokemonDetails(pokemonName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val cachedDetails = _cachedPokemonDetails.value[pokemonName]
            if(cachedDetails != null) {
                _pokemonDetails.value = cachedDetails
                return@launch
            }

            _uiState.value = uiState.value.copy(
                pokemonOperationType = PokemonOperationType.GET_POKEMON_DETAILS
            )

            getPokemonDetailsByNameUseCase(pokemonName).collect { getPokemonDetailsResponse ->
                when(getPokemonDetailsResponse) {
                    is Response.Loading -> {
                        updateUiState(uiState.value.copy(isLoading =  true))
                    }
                    is Response.Error -> {
                        updateUiState(uiState.value.copy(
                            isLoading =  false, error = getPokemonDetailsResponse.message
                        ))
                    }
                    is Response.Success -> {
                        getPokemonDetailsResponse.data?.let { pokemonDetailsData ->
                            _pokemonDetails.value = pokemonDetailsData

                            _cachedPokemonDetails.value += (pokemonName to pokemonDetailsData)

                            updateUiState(uiState.value.copy(isLoading =  false, success = true))
                        }
                    }
                }
            }
        }
    }
}