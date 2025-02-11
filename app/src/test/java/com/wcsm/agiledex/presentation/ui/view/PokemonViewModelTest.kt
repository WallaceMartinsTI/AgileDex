package com.wcsm.agiledex.presentation.ui.view

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.PokemonDetails
import com.wcsm.agiledex.domain.model.PokemonStats
import com.wcsm.agiledex.domain.model.Response
import com.wcsm.agiledex.domain.usecase.GetNextPokemonPageUseCase
import com.wcsm.agiledex.domain.usecase.GetPokemonDetailsByNameUseCase
import com.wcsm.agiledex.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PokemonViewModelTest {
    @Mock
    private lateinit var getPokemonListUseCase: GetPokemonListUseCase

    @Mock
    private lateinit var getPokemonDetailsByNameUseCase: GetPokemonDetailsByNameUseCase

    @Mock
    private lateinit var getNextPokemonPageUseCase: GetNextPokemonPageUseCase


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getPokemons_getPokemonsWithSuccess_shouldFillPokemonList() = runTest {
        val pokemonListResponse = listOf(
            Pokemon(
                id = "1",
                name = "Bulbasaur",
                spriteUrl = "https://pokeapi.co/api/v2/pokemon/1/"
            ),
            Pokemon(
                id = "2",
                name = "Charmander",
                spriteUrl = "https://pokeapi.co/api/v2/pokemon/2/"
            ),
            Pokemon(id = "3", name = "Squirtle", spriteUrl = "https://pokeapi.co/api/v2/pokemon/3/")
        )

        Mockito.`when`(getPokemonListUseCase(anyInt(), anyInt())).thenReturn(
            flow {
                emit(Response.Success(pokemonListResponse))
            }
        )

        val pokemonViewModel = PokemonViewModel(
            getPokemonListUseCase,
            getPokemonDetailsByNameUseCase,
            getNextPokemonPageUseCase
        )

        pokemonViewModel.pokemonList.test {
            assertThat(awaitItem()).isEmpty()

            pokemonViewModel.getPokemons()

            assertThat(awaitItem()).isEqualTo(pokemonListResponse)
        }
    }

    @Test
    fun getPokemonDetails_getPokemonDetailsWithSuccess_shouldFillPokemonDetails() = runTest {
        val pokemonName = "Bulbasaur"
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

        Mockito.`when`(getPokemonDetailsByNameUseCase(pokemonName)).thenReturn(
            flow { emit(Response.Success(pokemonDetailsResponse)) }
        )

        val pokemonViewModel = PokemonViewModel(
            getPokemonListUseCase,
            getPokemonDetailsByNameUseCase,
            getNextPokemonPageUseCase
        )

        pokemonViewModel.pokemonDetails.test {
            assertThat(awaitItem()).isNull()

            pokemonViewModel.getPokemonDetails(pokemonName)

            assertThat(awaitItem()).isEqualTo(pokemonDetailsResponse)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getNextPokemonPage_getNextPokemonPageWithSuccess_shouldAddMorePokemonsToPokemonList() =
        runTest {
            val initialPokemonList = listOf(
                Pokemon(
                    id = "1",
                    name = "Bulbasaur",
                    spriteUrl = "https://pokeapi.co/api/v2/pokemon/1/"
                ),
                Pokemon(
                    id = "2",
                    name = "Charmander",
                    spriteUrl = "https://pokeapi.co/api/v2/pokemon/2/"
                ),
                Pokemon(
                    id = "3",
                    name = "Squirtle",
                    spriteUrl = "https://pokeapi.co/api/v2/pokemon/3/"
                )
            )

            val nextPagePokemonsList = listOf(
                Pokemon(
                    id = "4",
                    name = "Ivysaur",
                    spriteUrl = "https://pokeapi.co/api/v2/pokemon/4/"
                ),
                Pokemon(
                    id = "5",
                    name = "Charmeleon",
                    spriteUrl = "https://pokeapi.co/api/v2/pokemon/5/"
                ),
                Pokemon(
                    id = "6",
                    name = "Wartortle",
                    spriteUrl = "https://pokeapi.co/api/v2/pokemon/6/"
                )
            )

            val expectedListAfterNextPage = initialPokemonList + nextPagePokemonsList

            Mockito.`when`(getPokemonListUseCase(anyInt(), anyInt())).thenReturn(
                flow {
                    emit(Response.Success(initialPokemonList))
                }
            )

            Mockito.`when`(getNextPokemonPageUseCase(anyInt(), anyInt())).thenReturn(
                flow {
                    emit(Response.Success(nextPagePokemonsList))
                }
            )

            val pokemonViewModel = PokemonViewModel(
                getPokemonListUseCase,
                getPokemonDetailsByNameUseCase,
                getNextPokemonPageUseCase
            )

            pokemonViewModel.pokemonList.test {
                assertThat(awaitItem()).isEmpty()

                pokemonViewModel.getPokemons()

                assertThat(awaitItem()).isEqualTo(initialPokemonList)

                pokemonViewModel.getNextPokemonsPage()

                assertThat(awaitItem()).isEqualTo(expectedListAfterNextPage)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
