package com.wcsm.agiledex.presentation.ui.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.agiledex.presentation.ui.components.AgileDexTopBar
import com.wcsm.agiledex.presentation.ui.components.ErrorContainer
import com.wcsm.agiledex.presentation.ui.components.PokemonCard
import com.wcsm.agiledex.presentation.ui.components.PokemonDetails
import com.wcsm.agiledex.presentation.ui.components.PokemonSearchBar
import com.wcsm.agiledex.presentation.ui.theme.BackgroundColor
import com.wcsm.agiledex.presentation.ui.theme.DarkGrayColor
import com.wcsm.agiledex.presentation.ui.theme.WhiteIceColor
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonView() {
    val pokemonViewModel: PokemonViewModel = hiltViewModel()

    val pokemonList by pokemonViewModel.pokemonList.collectAsStateWithLifecycle()
    val pokemonDetails by pokemonViewModel.pokemonDetails.collectAsStateWithLifecycle()
    val uiState by pokemonViewModel.uiState.collectAsStateWithLifecycle()
    val isFetchingMorePokemons by pokemonViewModel.isFetching.collectAsStateWithLifecycle()
    val isFirstScreenLoading by pokemonViewModel.isFirstScreenLoading.collectAsStateWithLifecycle()
    val isErrorGettingPokemons by pokemonViewModel.isErrorGettingPokemons.collectAsStateWithLifecycle()
    val isErrorGettingPokemonDetails by pokemonViewModel.isErrorGettingPokemonDetails.collectAsStateWithLifecycle()

    var selectedPokemonImageUrl by remember { mutableStateOf("") }

    var showPokemonDetails by remember { mutableStateOf(false) }
    var canLoadMorePokemons by remember { mutableStateOf(true) }
    var isFilteringPokemons by remember { mutableStateOf(false) }

    val lazyGridState = rememberLazyGridState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        pokemonViewModel.resetOffset()
        pokemonViewModel.getPokemons()
    }

    LaunchedEffect(uiState) {
        if(uiState.success) {
            pokemonViewModel.resetUiState()
        }
    }

    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.layoutInfo }
            .distinctUntilChanged()
            .collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                val buffer = 4

                if (lastVisibleItemIndex >= totalItemsCount - buffer && pokemonList.isNotEmpty()) {
                    if(canLoadMorePokemons && !isFilteringPokemons) {
                        pokemonViewModel.getNextPokemonsPage()
                        canLoadMorePokemons = false
                    }
                } else {
                    canLoadMorePokemons = true
                }
            }
    }

    LaunchedEffect(isFilteringPokemons) {
        lazyGridState.animateScrollToItem(0)
    }

    Scaffold(
        topBar = { AgileDexTopBar(title = "AGILE DEX") }
    ) {  paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundColor)
        ) {
            if(isFirstScreenLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(Modifier.size(80.dp))
                }
            }

            if(isErrorGettingPokemons) {
                ErrorContainer(
                    errorTitle = "There was a problem.",
                    errorMessage = uiState.error ?: "",
                    modifier = Modifier.fillMaxSize()
                ) {
                    pokemonViewModel.resetUiState()
                    pokemonViewModel.getPokemons()
                }
            }

            if(pokemonList.isNotEmpty() || isFilteringPokemons) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PokemonSearchBar(
                        isDarkTheme = isSystemInDarkTheme(),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        onFilterValueChange = { textFilter ->
                            isFilteringPokemons = textFilter.isNotEmpty()
                            pokemonViewModel.getPokemonsByName(pokemonName = textFilter)
                        }
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        state = lazyGridState,
                    ) {
                        items(
                            items = pokemonList,
                            key = { it.id }
                        ) { pokemon ->
                            PokemonCard(
                                pokemon = pokemon,
                                imageUrl = pokemon.spriteUrl,
                                isDarkTheme = isSystemInDarkTheme(),
                                modifier = Modifier.padding(4.dp),
                                onPokemonCardClick = {
                                    pokemonViewModel.getPokemonDetails(pokemon.name)
                                    selectedPokemonImageUrl = pokemon.spriteUrl
                                    showPokemonDetails = true
                                }
                            )
                        }

                        if(pokemonList.isEmpty() && isFilteringPokemons) {
                            item(span = { GridItemSpan(3) }) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "No pokemon found.",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }

                        if(isFetchingMorePokemons) {
                            item(span = { GridItemSpan(3) }) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }

        if(showPokemonDetails) {
            ModalBottomSheet(
                onDismissRequest = {
                    selectedPokemonImageUrl = ""

                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showPokemonDetails = false
                        }
                    }

                    pokemonViewModel.resetUiState()
                    pokemonViewModel.resetPokemonDetails()
                },
                sheetState = sheetState,
                containerColor = WhiteIceColor,
                dragHandle = {
                    BottomSheetDefaults.DragHandle(
                        color = DarkGrayColor
                    )
                }
            ) {
                PokemonDetails(
                    pokemonDetails = pokemonDetails,
                    pokemonImageUrl = selectedPokemonImageUrl,
                    isDarkTheme = isSystemInDarkTheme(),
                    isError = Pair(isErrorGettingPokemonDetails, uiState.error ?: "")
                )
            }
        }
    }
}