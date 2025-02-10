package com.wcsm.agiledex.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.agiledex.domain.model.Pokemon
import com.wcsm.agiledex.domain.model.Response
import com.wcsm.agiledex.domain.repository.PokemonRepository
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
class GetPokemonListUseCaseTest {

    @Mock
    private lateinit var pokemonRepository: PokemonRepository

    private lateinit var getPokemonListUseCase: GetPokemonListUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        getPokemonListUseCase = GetPokemonListUseCase(pokemonRepository)
    }

    @Test
    fun getPokemonListUseCase_getPokemonsFromRepository_returnPokemonList() = runTest {
        val pokemonListResponse = listOf(
            Pokemon(id = "1", name = "Bulbasaur", spriteUrl = "https://pokeapi.co/api/v2/pokemon/1/"),
            Pokemon(id = "2", name = "Charmander", spriteUrl = "https://pokeapi.co/api/v2/pokemon/2/"),
            Pokemon(id = "3", name = "Squirtle", spriteUrl = "https://pokeapi.co/api/v2/pokemon/3/")
        )

        Mockito.`when`(pokemonRepository.getPokemons(anyInt(), anyInt())).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(pokemonListResponse))
            }
        )

        getPokemonListUseCase(0, 30).test {
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            val secondItemEmitted = awaitItem()
            assertThat(secondItemEmitted).isInstanceOf(Response.Success::class.java)

            val pokemonList = (secondItemEmitted as Response.Success).data
            assertThat(pokemonList).hasSize(3)
            assertThat(pokemonList.map { it.name }).containsExactly("Bulbasaur", "Charmander", "Squirtle")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getPokemonListUseCase_getPokemonsFromRepositoryWithInvalidOffset_returnEmptyPokemonList() = runTest {
        val pokemonListResponse: List<Pokemon> = emptyList()

        Mockito.`when`(pokemonRepository.getPokemons(anyInt(), anyInt())).thenReturn(
            flow {
                emit(Response.Loading)
                emit(Response.Success(pokemonListResponse))
            }
        )

        getPokemonListUseCase(9999, 30).test {
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            val secondItemEmitted = awaitItem()
            assertThat(secondItemEmitted).isInstanceOf(Response.Success::class.java)

            val pokemonList = (secondItemEmitted as Response.Success).data
            assertThat(pokemonList).isEmpty()

            cancelAndIgnoreRemainingEvents()
        }
    }

}