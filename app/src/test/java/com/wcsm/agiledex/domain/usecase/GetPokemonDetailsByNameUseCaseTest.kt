package com.wcsm.agiledex.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.model.PokemonStats
import com.wcsm.agiledex.domain.model.Response
import com.wcsm.agiledex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetPokemonDetailsByNameUseCaseTest {

    @Mock
    private lateinit var pokemonRepository: PokemonRepository

    private lateinit var getPokemonDetailsByNameUseCase: GetPokemonDetailsByNameUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        getPokemonDetailsByNameUseCase = GetPokemonDetailsByNameUseCase(pokemonRepository)
    }

    @Test
    fun getPokemonDetailsByNameUseCase_getPokemonDetailsByNameFromRepository_returnPokemonDetails() = runTest {
        val pokemonDetailsResponse = PokemonDetails(
            id = 1,
            order = 1,
            name = "bulbasaur",
            baseExperience = 64,
            types = listOf("grass", "poison"),
            weight=69,
            height = 7,
            baseStats = listOf(
                PokemonStats(
                    name = "hp",
                    progress = Pair(45, 100)
                ),
                PokemonStats(
                    name = "attack",
                    progress = Pair(49, 100)
                ),
                PokemonStats(
                    name = "defense",
                    progress = Pair(49, 100)
                ),
                PokemonStats(
                    name = "speed",
                    progress = Pair(45, 100)
                ),
            )
        )

        Mockito.`when`(pokemonRepository.getPokemonDetailsByName(anyString())).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(pokemonDetailsResponse))
            }
        )

        val pokemonName = "bulbasaur"
        getPokemonDetailsByNameUseCase(pokemonName).test {
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            val secondItemEmitted = awaitItem()
            assertThat(secondItemEmitted).isInstanceOf(Response.Success::class.java)

            val pokemonDetails = (secondItemEmitted as Response.Success).data
            assertThat(pokemonDetails).isNotNull()
            assertThat(pokemonDetails).isEqualTo(pokemonDetailsResponse)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getPokemonDetailsByNameUseCase_getPokemonDetailsByNameFromRepositoryWhenInvalidPokemonName_returnRequestFailedError() = runTest {
        Mockito.`when`(pokemonRepository.getPokemonDetailsByName(anyString())).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Error("Error getting pokemon details: Request failed."))
            }
        )

        val pokemonName = "kjkj"
        getPokemonDetailsByNameUseCase(pokemonName).test {
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            val secondItemEmitted = awaitItem()
            assertThat(secondItemEmitted).isInstanceOf(Response.Error::class.java)

            val pokemonDetailsErrorMessage = (secondItemEmitted as Response.Error).message
            assertThat(pokemonDetailsErrorMessage).isEqualTo("Error getting pokemon details: Request failed.")

            cancelAndIgnoreRemainingEvents()
        }
    }
}