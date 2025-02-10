package com.wcsm.agiledex.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.Response
import com.wcsm.agiledex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
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
class GetNextPokemonPageUseCaseTest {
    @Mock
    private lateinit var pokemonRepository: PokemonRepository

    private lateinit var getNextPokemonPageUseCase: GetNextPokemonPageUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        getNextPokemonPageUseCase = GetNextPokemonPageUseCase(pokemonRepository)
    }

    @Test
    fun getNextPokemonPageUseCase_getNextPokemonPageUseCaseWithValidOffset_returnNextPokemonPage() = runTest {
        val actualPokemonList = listOf(
            Pokemon(id = "1", name = "Bulbasaur", spriteUrl = "https://pokeapi.co/api/v2/pokemon/1/"),
            Pokemon(id = "2", name = "Charmander", spriteUrl = "https://pokeapi.co/api/v2/pokemon/2/"),
            Pokemon(id = "3", name = "Squirtle", spriteUrl = "https://pokeapi.co/api/v2/pokemon/3/")
        )

        val nextPagePokemonsResponse = listOf(
            Pokemon(id = "4", name = "Ivysaur", spriteUrl = "https://pokeapi.co/api/v2/pokemon/4/"),
            Pokemon(id = "5", name = "Charmeleon", spriteUrl = "https://pokeapi.co/api/v2/pokemon/5/"),
            Pokemon(id = "6", name = "Wartortle", spriteUrl = "https://pokeapi.co/api/v2/pokemon/6/")
        )

        Mockito.`when`(pokemonRepository.getPokemons(anyInt(), anyInt())).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(nextPagePokemonsResponse))
            }
        )

        getNextPokemonPageUseCase(30, 30).test {
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            val secondItemEmitted = awaitItem()
            assertThat(secondItemEmitted).isInstanceOf(Response.Success::class.java)

            val nextPokemonList = (secondItemEmitted as Response.Success).data
            assertThat(nextPokemonList).hasSize(3)
            assertThat(nextPokemonList.map { it.name }).containsExactly("Ivysaur", "Charmeleon", "Wartortle")

            val allPokemonList = actualPokemonList + nextPokemonList
            assertThat(allPokemonList).hasSize(6)
            assertThat(allPokemonList.map {it.name}).containsExactly("Bulbasaur", "Charmander", "Squirtle", "Ivysaur", "Charmeleon", "Wartortle")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getNextPokemonPageUseCase_getNextPokemonPageUseCaseWithInvalidOffset_returnEmptyList() = runTest {
        val actualPokemonList = listOf(
            Pokemon(id = "1", name = "Bulbasaur", spriteUrl = "https://pokeapi.co/api/v2/pokemon/1/"),
            Pokemon(id = "2", name = "Charmander", spriteUrl = "https://pokeapi.co/api/v2/pokemon/2/"),
            Pokemon(id = "3", name = "Squirtle", spriteUrl = "https://pokeapi.co/api/v2/pokemon/3/")
        )

        Mockito.`when`(pokemonRepository.getPokemons(anyInt(), anyInt())).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(emptyList()))
            }
        )

        getNextPokemonPageUseCase(500, 30).test {
            val firstItemEmitted = awaitItem()
            assertThat(firstItemEmitted).isInstanceOf(Response.Success::class.java)

            val nextPokemonList = (firstItemEmitted as Response.Success).data
            assertThat(nextPokemonList).isEmpty()

            val allPokemonList = actualPokemonList + nextPokemonList
            assertThat(allPokemonList).hasSize(3)
            assertThat(allPokemonList.map {it.name}).containsExactly("Bulbasaur", "Charmander", "Squirtle")

            cancelAndIgnoreRemainingEvents()
        }
    }
}