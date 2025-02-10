package com.wcsm.agiledex.data.remote.api.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.agiledex.data.remote.api.PokeAPIService
import com.wcsm.agiledex.data.remote.api.dto.pokemonDetails.PokemonDetailsDTO
import com.wcsm.agiledex.data.remote.api.dto.pokemonDetails.Species
import com.wcsm.agiledex.data.remote.api.dto.pokemonDetails.Stat
import com.wcsm.agiledex.data.remote.api.dto.pokemonDetails.StatX
import com.wcsm.agiledex.data.remote.api.dto.pokemonDetails.Type
import com.wcsm.agiledex.data.remote.api.dto.pokemonDetails.TypeX
import com.wcsm.agiledex.data.remote.api.dto.pokemons.PokeAPIResponse
import com.wcsm.agiledex.data.remote.api.dto.pokemons.PokemonDTO
import com.wcsm.agiledex.domain.model.Response
import com.wcsm.agiledex.domain.repository.PokemonRepository
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PokemonRepositoryImplTest {

    @Mock
    private lateinit var pokeAPIService: PokeAPIService

    private lateinit var pokemonRepository: PokemonRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        pokemonRepository = PokemonRepositoryImpl(pokeAPIService)
    }

    @Test
    fun getPokemons_getPokemonsFromAPIWithValidData_returnSuccessWithPokemonList() = runTest {
        Mockito.`when`(pokeAPIService.getPokemons(anyInt(), anyInt())).thenReturn(
            retrofit2.Response.success(
                PokeAPIResponse(
                    count = 1304,
                    next = "",
                    previous = "",
                    results = listOf(
                        PokemonDTO(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/"),
                        PokemonDTO(name = "charmander", url = "https://pokeapi.co/api/v2/pokemon/2/"),
                        PokemonDTO(name = "squirtle", url = "https://pokeapi.co/api/v2/pokemon/3/")
                    )
                )
            )
        )

        pokemonRepository.getPokemons(0, 20).test {
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
    fun getPokemons_getPokemonsFromAPIWithInvalidOffset_returnErrorWithEmptyListErrorMessage() = runTest {
        Mockito.`when`(pokeAPIService.getPokemons(anyInt(), anyInt())).thenReturn(
            retrofit2.Response.success(
                PokeAPIResponse(
                    count = 1304,
                    next = "",
                    previous = "",
                    results = emptyList()
                )
            )
        )

        pokemonRepository.getPokemons(9999, 20).test {
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            val secondItemEmitted = awaitItem()

            assertThat(secondItemEmitted).isInstanceOf(Response.Error::class.java)

            val emptyPokemonListErrorMessage = (secondItemEmitted as Response.Error).message

            assertThat(emptyPokemonListErrorMessage).isEqualTo("Error getting pokemon list: pokemon list returned is empty.")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getPokemons_getPokemonsFromAPIWhenAPIisOffline_returnRequestFailedError() = runTest {
        Mockito.`when`(pokeAPIService.getPokemons(anyInt(), anyInt())).thenReturn(
            retrofit2.Response.error(
                500,
                "{ \"message\": \"Internal Server Error\" }".toResponseBody("application/json".toMediaType())
            )
        )

        pokemonRepository.getPokemons(0, 20).test {
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            val secondItemEmitted = awaitItem()

            assertThat(secondItemEmitted).isInstanceOf(Response.Error::class.java)

            val nullResponseBodyErrorMessage = (secondItemEmitted as Response.Error).message

            assertThat(nullResponseBodyErrorMessage).isEqualTo("Error getting pokemon list: Request failed.")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getPokemons_getPokemonsFromAPIWhenNetworkFailure_throwRuntimeException() = runTest {
        Mockito.`when`(pokeAPIService.getPokemons(anyInt(), anyInt())).thenThrow(
            RuntimeException("Simulated network failure")
        )

        pokemonRepository.getPokemons(0, 20).test {
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            val secondItemEmitted = awaitItem()

            assertThat(secondItemEmitted).isInstanceOf(Response.Error::class.java)

            val nullResponseBodyErrorMessage = (secondItemEmitted as Response.Error).message

            assertThat(nullResponseBodyErrorMessage).isEqualTo("Unknown error while searching for pokemons, please inform the administrator.")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getPokemonDetailsByName_getPokemonDetailsWithValidName_returnSuccessWithPokemonDetails() = runTest {
        Mockito.`when`(pokeAPIService.getPokemonDetails(anyString())).thenReturn(
            retrofit2.Response.success(
                PokemonDetailsDTO(
                    base_experience = 64,
                    height = 7,
                    id = 1,
                    name = "bulbasaur",
                    order = 1,
                    species = Species(
                        name = "bulbasaur",
                        url="https://pokeapi.co/api/v2/pokemon-species/1/"
                    ),
                    stats = listOf(
                        Stat(base_stat=45, effort=0, stat= StatX(name="hp", url="https://pokeapi.co/api/v2/stat/1/")),
                        Stat(base_stat=45, effort=0, stat= StatX(name="hp", url="https://pokeapi.co/api/v2/stat/1/")),
                        Stat(base_stat=45, effort=0, stat= StatX(name="hp", url="https://pokeapi.co/api/v2/stat/1/")),
                        Stat(base_stat=45, effort=0, stat= StatX(name="hp", url="https://pokeapi.co/api/v2/stat/1/")),
                    ),
                   types = listOf(
                       Type(slot=1, type= TypeX(name="grass", url="https://pokeapi.co/api/v2/type/12/")),
                       Type(slot=2, type=TypeX(name="poison", url="https://pokeapi.co/api/v2/type/4/"))
                   ),
                   weight=69
                )
            )
        )

        val pokemonName = "Bulbasaur"
        pokemonRepository.getPokemonDetailsByName(pokemonName).test {
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            val pokemonDetailsResponse = awaitItem()

            assertThat(pokemonDetailsResponse).isInstanceOf(Response.Success::class.java)

            val pokemonDetails = (pokemonDetailsResponse as Response.Success).data

            assertThat(pokemonDetails).isNotNull()

            assertThat(pokemonDetails?.name).isEqualTo(pokemonName)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getPokemonDetailsByName_getPokemonDetailsWithInvalidName_returnRequestFailedError() = runTest {
        Mockito.`when`(pokeAPIService.getPokemonDetails(anyString())).thenReturn(
            retrofit2.Response.error(
                404,
                "{ \"message\": \"\" }".toResponseBody("application/json".toMediaType())
            )
        )

        val pokemonName = "kjkj"
        pokemonRepository.getPokemonDetailsByName(pokemonName).test {
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            val pokemonDetailsResponse = awaitItem()

            assertThat(pokemonDetailsResponse).isInstanceOf(Response.Error::class.java)

            val pokemonDetailsNotFoundErrorMessage = (pokemonDetailsResponse as Response.Error).message

            assertThat(pokemonDetailsNotFoundErrorMessage).isEqualTo("Error getting pokemon details: Request failed.")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getPokemonDetailsByName_getPokemonDetailsFromAPIWhenAPIisOffline_returnRequestFailedError() = runTest {
        Mockito.`when`(pokeAPIService.getPokemonDetails(anyString())).thenReturn(
            retrofit2.Response.error(
                500,
                "{ \"message\": \"Internal Server Error\" }".toResponseBody("application/json".toMediaType())
            )
        )

        val pokemonName = "Bulbasaur"
        pokemonRepository.getPokemonDetailsByName(pokemonName).test {
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            val secondItemEmitted = awaitItem()

            assertThat(secondItemEmitted).isInstanceOf(Response.Error::class.java)

            val nullResponseBodyErrorMessage = (secondItemEmitted as Response.Error).message

            assertThat(nullResponseBodyErrorMessage).isEqualTo("Error getting pokemon details: Request failed.")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getPokemonDetailsByName_getPokemonDetailsFromAPIWhenNetworkFailure_throwRuntimeException() = runTest {
        Mockito.`when`(pokeAPIService.getPokemonDetails(anyString())).thenThrow(
            RuntimeException("Simulated network failure")
        )

        val pokemonName = "Bulbasaur"
        pokemonRepository.getPokemonDetailsByName(pokemonName).test {
            assertThat(awaitItem()).isInstanceOf(Response.Loading::class.java)

            val secondItemEmitted = awaitItem()

            assertThat(secondItemEmitted).isInstanceOf(Response.Error::class.java)

            val nullResponseBodyErrorMessage = (secondItemEmitted as Response.Error).message

            assertThat(nullResponseBodyErrorMessage).isEqualTo("Unknown error while fetching pokemon details, please inform the administrator.")

            cancelAndIgnoreRemainingEvents()
        }
    }
}