package com.wcsm.agiledex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.model.PokemonStats
import com.wcsm.agiledex.ui.theme.AgileDexTheme
import com.wcsm.agiledex.ui.theme.DarkGrayColor
import com.wcsm.agiledex.ui.theme.PokemonStatsAttackColor
import com.wcsm.agiledex.ui.theme.PokemonStatsDefenseColor
import com.wcsm.agiledex.ui.theme.PokemonStatsExpColor
import com.wcsm.agiledex.ui.theme.PokemonStatsHpColor
import com.wcsm.agiledex.ui.theme.PokemonStatsSpeedColor
import com.wcsm.agiledex.ui.theme.PrimaryColor
import com.wcsm.agiledex.utils.getPokemonTypeColor
import kotlinx.coroutines.delay

@Composable
fun PokemonDetails(
    pokemonDetails: PokemonDetails?,
    pokemonImageUrl: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1500)
    }

    if(pokemonDetails == null) {
        Column(
            modifier = Modifier.fillMaxWidth().heightIn(80.dp).padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(80.dp)
                    .padding(16.dp),
                color = PrimaryColor
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(DarkGrayColor)
                .padding(bottom = 100.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                    .background(getPokemonTypeColor(pokemonDetails.types[0])),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "#${pokemonDetails.order.toString().padStart(3, '0')}",
                        color = Color.Black
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon.",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .clickable { onDismiss() }
                    )
                }

                AsyncImage(
                    model = pokemonImageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
                /*Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )*/

                Spacer(Modifier.height(16.dp))
            }

            Text(
                text = pokemonDetails.name,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if(pokemonDetails.types.isNotEmpty()) {
                    PokemonTypeContainer(
                        pokemonType = pokemonDetails.types[0]
                    )
                }

                if(pokemonDetails.types.size == 2) {
                    Spacer(Modifier.width(16.dp))

                    PokemonTypeContainer(
                        pokemonType = pokemonDetails.types[1]
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PokemonInfoContainer(
                    value = pokemonDetails.weight,
                    valueSuffix = "KG",
                    label = "Weight"
                )

                PokemonInfoContainer(
                    value = pokemonDetails.height,
                    valueSuffix = "M",
                    label = "Height"
                )
            }

            Text(
                text = "Base Stats",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            PokemonStatsContainer(
                pokemonStatsList = pokemonDetails.baseStats,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PokemonDetailsPreview() {
    AgileDexTheme {
        val pokemonDetails = PokemonDetails(
            id = 1,
            order = 1,
            name = "bulbasaur",
            baseExperience = 550,
            types = listOf("GRASS", "POISON"),
            weight = 69,
            height = 7,
            baseStats = listOf(
                PokemonStats(
                    name = "hp",
                    progress = Pair(45,300)
                ),
                PokemonStats(
                    name = "attack",
                    progress = Pair(49,300)
                ),
                PokemonStats(
                    name = "defense",
                    progress = Pair(49,300)
                ),
                PokemonStats(
                    name = "speed",
                    progress = Pair(45,300)
                )
            )
        )
        PokemonDetails(
            pokemonDetails = pokemonDetails,
            pokemonImageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            onDismiss = {}
        )
    }
}

@Composable
private fun PokemonInfoContainer(
    value: Int,
    valueSuffix: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$value $valueSuffix",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = label,
            color = Color.Gray
        )
    }
}

@Preview
@Composable
fun PokemonInfoContainerPreview() {
    AgileDexTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PokemonInfoContainer(90, "KG","Weight")
            Spacer(Modifier.height(8.dp))
            PokemonInfoContainer(2, "M","Height")
        }
    }
}

@Composable
fun StatsProgressBar(
    currentValue: Int,
    maxValue: Int,
    filledBarColor: Color,
    modifier: Modifier = Modifier
) {
    val progress = currentValue.toFloat() / maxValue.toFloat()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp),
        contentAlignment = Alignment.Center
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(50)),
            color = filledBarColor,
            trackColor = Color.White,
        )


        Text(
            text = "$currentValue / $maxValue",
            color = Color.Black.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun StatsProgressBarPreview() {
    AgileDexTheme {
        StatsProgressBar(
            currentValue = 250,
            maxValue = 300,
            filledBarColor = Color.Red
        )
    }
}

@Composable
fun PokemonStats(
    label: String,
    currentValue: Int,
    maxValue: Int,
    filledBarColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.Gray,
            modifier = Modifier.width(40.dp)
        )

        StatsProgressBar(
            currentValue = currentValue,
            maxValue = maxValue,
            filledBarColor = filledBarColor
        )
    }
}

@Preview
@Composable
fun PokemonStatsPreview() {
    AgileDexTheme {
        PokemonStats(
            label = "HP",
            currentValue = 180,
            maxValue = 300,
            filledBarColor = Color.Red
        )
    }
}

@Composable
fun PokemonStatsContainer(
    pokemonStatsList: List<PokemonStats>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        val labelColorsMap: Map<String,Color> = mapOf(
            "hp" to PokemonStatsHpColor,
            "attack" to PokemonStatsAttackColor,
            "defense" to PokemonStatsDefenseColor,
            "speed" to PokemonStatsSpeedColor,
            "exp" to PokemonStatsExpColor
        )

        pokemonStatsList.forEach {
            PokemonStats(
                label = getShortStatus(it.name).uppercase(),
                currentValue = it.progress.first,
                maxValue = it.progress.second,
                filledBarColor = labelColorsMap[it.name] ?: Color.Gray
            )
        }
    }
}


@Preview
@Composable
fun PokemonStatsContainerPreview() {
    AgileDexTheme {
        val pokemonStatsList = listOf(
            PokemonStats(
                name = "hp",
                progress = Pair(168, 300)
            ),
            PokemonStats(
                name = "attack",
                progress = Pair(205, 300)
            ),
            PokemonStats(
                name = "defense",
                progress = Pair(64, 300)
            ),
            PokemonStats(
                name = "speed",
                progress = Pair(204, 300)
            ),
            PokemonStats(
                name = "exp",
                progress = Pair(295, 1000)
            )
        )

        PokemonStatsContainer(
            pokemonStatsList = pokemonStatsList
        )
    }
}

@Composable
private fun PokemonTypeContainer(
    pokemonType: String
) {
    Box(
        Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(getPokemonTypeColor(pokemonType))
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = pokemonType.uppercase(),
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Preview
@Composable
private fun PokemonTypeContainerPreview() {
    AgileDexTheme {
        PokemonTypeContainer("grass")
    }
}

private fun getShortStatus(status: String) : String {
    return when(status) {
        "attack" -> "atk"
        "defense" -> "atk"
        "speed" -> "spd"
        else -> status
    }
}

