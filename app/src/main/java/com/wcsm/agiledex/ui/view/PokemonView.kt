package com.wcsm.agiledex.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.agiledex.ui.components.AgileDexTopBar
import com.wcsm.agiledex.ui.components.PokemonCard
import com.wcsm.agiledex.ui.components.PokemonDetails
import com.wcsm.agiledex.ui.theme.BackgroundColor
import com.wcsm.agiledex.ui.theme.PrimaryColor
import com.wcsm.agiledex.ui.theme.SecondaryColor
import com.wcsm.agiledex.utils.getPokemonTypeColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonView() {
    val pokemonViewModel: PokemonViewModel = hiltViewModel()

    val pokemonList by pokemonViewModel.pokemonList.collectAsStateWithLifecycle()
    val pokemonDetails by pokemonViewModel.pokemonDetails.collectAsStateWithLifecycle()
    val errorState by pokemonViewModel.errorState.collectAsStateWithLifecycle()
    val isLoading by pokemonViewModel.isLoading.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    var showPokemonDetails by remember { mutableStateOf(false) }
    var selectedPokemonImageUrl by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AgileDexTopBar(title = "AGILE DEX") }
    ) {  paddingValues ->
        if(!isLoading && errorState) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Não foi possível carregar os Pokémons, tente novamente mais tarde.",
                    color = SecondaryColor,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                )
            }
        }

        if(isLoading) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(160.dp)
                        .padding(16.dp),
                    strokeWidth = 5.dp,
                    color = PrimaryColor
                )
            }
        } else if(pokemonList.isNotEmpty()) {
            Surface(
                modifier = Modifier.fillMaxSize().padding(paddingValues).background(BackgroundColor)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = pokemonList,
                        key = { it.name }
                    ) { pokemon ->
                        PokemonCard(
                            pokemon = pokemon,
                            imageUrl = pokemon.spriteUrl,
                            cardColor = pokemon.typeColor,
                            onPokemonCardClick = {
                                pokemonViewModel.getPokemonDetails(pokemon.name)
                                selectedPokemonImageUrl = pokemon.spriteUrl
                                showPokemonDetails = true
                            }
                        )
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
                    },
                    sheetState = sheetState,
                    containerColor = getPokemonTypeColor(pokemonDetails?.types?.get(0) ?: ""),
                    dragHandle = {
                        BottomSheetDefaults.DragHandle(
                            color = Color.White
                        )
                    }
                ) {
                    PokemonDetails(
                        pokemonDetails = pokemonDetails,
                        pokemonImageUrl = selectedPokemonImageUrl,
                        onDismiss = {
                            selectedPokemonImageUrl = ""

                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showPokemonDetails = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}