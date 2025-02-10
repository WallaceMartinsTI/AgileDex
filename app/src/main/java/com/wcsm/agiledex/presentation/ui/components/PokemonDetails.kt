package com.wcsm.agiledex.presentation.ui.components

import android.content.res.Configuration
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.model.PokemonStats
import com.wcsm.agiledex.presentation.ui.theme.AgileDexTheme
import com.wcsm.agiledex.presentation.ui.theme.DarkGrayColor
import com.wcsm.agiledex.presentation.ui.theme.PokemonStatsAttackColor
import com.wcsm.agiledex.presentation.ui.theme.PokemonStatsDefenseColor
import com.wcsm.agiledex.presentation.ui.theme.PokemonStatsExpColor
import com.wcsm.agiledex.presentation.ui.theme.PokemonStatsHpColor
import com.wcsm.agiledex.presentation.ui.theme.PokemonStatsSpeedColor
import com.wcsm.agiledex.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.agiledex.presentation.ui.theme.PrimaryColor
import com.wcsm.agiledex.presentation.ui.theme.White06Color
import com.wcsm.agiledex.presentation.ui.theme.WhiteIceColor
import com.wcsm.agiledex.utils.getDominantColor
import com.wcsm.agiledex.utils.getPokemonTypeColor
import com.wcsm.agiledex.utils.toVibrantColor
import kotlinx.coroutines.delay

@Composable
fun PokemonDetails(
    pokemonDetails: PokemonDetails?,
    pokemonImageUrl: String,
    isDarkTheme: Boolean,
    isError: Pair<Boolean, String>,
    modifier: Modifier = Modifier
) {
    var dominantColor by remember { mutableStateOf(Color.Gray) }

    var pokemonStatsList: List<PokemonStats> by remember { mutableStateOf(emptyList()) }

    val themeBasedMainColor = if(isDarkTheme) DarkGrayColor else WhiteIceColor

    LaunchedEffect(pokemonDetails) {
        if(pokemonDetails != null) {
            pokemonStatsList = pokemonDetails.baseStats + PokemonStats("experience", Pair(pokemonDetails.baseExperience, 1000))
        }
    }

    Box(
        modifier = Modifier.height(620.dp)
    ) {
        if(isError.first) {
            ErrorContainer(
                errorTitle = "There was a problem.",
                errorMessage = isError.second,
                modifier = Modifier.fillMaxSize()
            )
        }

        if(pokemonDetails == null && !isError.first) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp),
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
        }

        if(pokemonDetails != null) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(themeBasedMainColor)
                    .padding(bottom = 80.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    WhiteIceColor,
                                    dominantColor.toVibrantColor()
                                )
                            )
                        )
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "#${pokemonDetails.order.toString().padStart(3, '0')}",
                        color = DarkGrayColor,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    AsyncImage(
                        model = pokemonImageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp),
                        onSuccess = { imageState->
                            val bitmap = (imageState.result.drawable as BitmapDrawable).bitmap
                            dominantColor = Color(getDominantColor(bitmap))
                        }
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
                    color = if(isDarkTheme) WhiteIceColor else DarkGrayColor,
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
                        label = "Weight",
                        isDarkTheme = isDarkTheme
                    )

                    PokemonInfoContainer(
                        value = pokemonDetails.height,
                        valueSuffix = "M",
                        label = "Height",
                        isDarkTheme = isDarkTheme
                    )
                }

                Text(
                    text = "Base Stats",
                    color = if(isDarkTheme) Color.White.copy(alpha = 0.75f) else DarkGrayColor,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                PokemonStatsContainer(
                    pokemonStatsList = pokemonStatsList,
                    isDarkTheme = isDarkTheme,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Preview(name = "Light Theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PokemonDetailsLightPreview() {
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
            isDarkTheme = false,
            isError = Pair(false, "")
        )
    }
}

@Preview(name = "Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PokemonDetailsDarkPreview() {
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
            isDarkTheme = true,
            isError = Pair(false, "")
        )
    }
}

@Composable
private fun PokemonInfoContainer(
    value: Int,
    valueSuffix: String,
    label: String,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val valueAndSuffixColor = if(isDarkTheme) Color.White else DarkGrayColor

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$value $valueSuffix",
            color = valueAndSuffixColor,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = label,
            color = Color.Gray,
            fontFamily = PoppinsFontFamily
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
            PokemonInfoContainer(90, "KG","Weight", true)
            Spacer(Modifier.height(8.dp))
            PokemonInfoContainer(2, "M","Height", false)
        }
    }
}

@Composable
fun StatsProgressBar(
    currentValue: Int,
    maxValue: Int,
    filledBarColor: Color,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val progress = currentValue.toFloat() / maxValue.toFloat()
    val trackColor = if(isDarkTheme) Color.White else DarkGrayColor.copy(0.3f)
    val textColor = if(isDarkTheme) Color.Black.copy(alpha = 0.6f) else WhiteIceColor

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
            trackColor = trackColor
        )


        Text(
            text = "$currentValue / $maxValue",
            color = textColor,
            style = MaterialTheme.typography.titleMedium,
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
            filledBarColor = Color.Red,
            isDarkTheme = false
        )
    }
}

@Composable
fun PokemonStats(
    label: String,
    currentValue: Int,
    maxValue: Int,
    filledBarColor: Color,
    isDarkTheme: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.Gray,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.width(40.dp)
        )

        StatsProgressBar(
            currentValue = currentValue,
            maxValue = maxValue,
            filledBarColor = filledBarColor,
            isDarkTheme = isDarkTheme
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
            filledBarColor = Color.Red,
            isDarkTheme = false
        )
    }
}

@Composable
fun PokemonStatsContainer(
    pokemonStatsList: List<PokemonStats>,
    isDarkTheme: Boolean,
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
            "experience" to PokemonStatsExpColor
        )

        pokemonStatsList.forEach {
            PokemonStats(
                label = getShortStatus(it.name).uppercase(),
                currentValue = it.progress.first,
                maxValue = it.progress.second,
                filledBarColor = labelColorsMap[it.name] ?: Color.Gray,
                isDarkTheme = isDarkTheme
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
            pokemonStatsList = pokemonStatsList,
            isDarkTheme = false
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
            .background(getPokemonTypeColor(pokemonType, DarkGrayColor))
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = pokemonType.uppercase(),
            style = MaterialTheme.typography.titleMedium,
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
        "defense" -> "def"
        "speed" -> "spd"
        "experience" -> "exp"
        else -> status
    }
}

