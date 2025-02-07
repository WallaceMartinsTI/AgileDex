package com.wcsm.agiledex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.ui.theme.AgileDexTheme


@Composable
fun PokemonCard(
    pokemon: Pokemon,
    imageUrl: String,
    cardColor: Color,
    onPokemonCardClick: () -> Unit
) {
    ElevatedCard(
        onClick = { onPokemonCardClick() },
        colors = CardDefaults.elevatedCardColors(
            containerColor = cardColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )

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
                        cardColor = Color.White,
                        onPokemonCardClick = {}
                    )
                }
            }
        }
    }
}