package com.wcsm.agiledex.presentation.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.presentation.ui.theme.AgileDexTheme
import com.wcsm.agiledex.presentation.ui.theme.OnBackgroundColor
import com.wcsm.agiledex.presentation.ui.theme.OnSurfaceColor


@Composable
fun PokemonCard(
    pokemon: Pokemon,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onPokemonCardClick: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    var isImageLoading: Boolean? by remember { mutableStateOf(null) }

    ElevatedCard(
        onClick = { onPokemonCardClick() },
        colors = CardDefaults.elevatedCardColors(
            containerColor = if(isDarkTheme) OnBackgroundColor else Color.Unspecified
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp),
                    onLoading = {
                        isImageLoading = true
                    },
                    onSuccess = {
                        isImageLoading = false
                    }
                )

                if(isImageLoading == true) {
                    CircularProgressIndicator()
                }
            }

            Text(
                text = pokemon.name,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
private fun PokemonCardPreview() {
    AgileDexTheme {
        val pokemon = Pokemon(
            id = "bulbasaur+1",
            name = "bulbasaur",
            spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"
        )

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(5) {
                    PokemonCard(
                        pokemon = pokemon,
                        imageUrl = pokemon.spriteUrl,
                        onPokemonCardClick = {}
                    )
                }
            }
        }
    }
}