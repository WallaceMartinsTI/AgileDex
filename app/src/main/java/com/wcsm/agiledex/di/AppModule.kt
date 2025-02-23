package com.wcsm.agiledex.di

import com.wcsm.agiledex.data.remote.api.PokeAPIService
import com.wcsm.agiledex.domain.repository.PokemonRepository
import com.wcsm.agiledex.data.remote.api.repository.PokemonRepositoryImpl
import com.wcsm.agiledex.domain.usecase.GetNextPokemonPageUseCase
import com.wcsm.agiledex.domain.usecase.GetPokemonDetailsByNameUseCase
import com.wcsm.agiledex.domain.usecase.GetPokemonListUseCase
import com.wcsm.agiledex.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.POKEAPI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providePokeApi(
        retrofit: Retrofit
    ): PokeAPIService {
        return retrofit.create(PokeAPIService::class.java)
    }

    @Provides
    fun providePokemonRepository(
        pokeAPIService: PokeAPIService
    ): PokemonRepository {
        return PokemonRepositoryImpl(pokeAPIService)
    }

    @Provides
    fun provideGetPokemonListUseCase(
        pokemonRepository: PokemonRepository
    ): GetPokemonListUseCase {
        return GetPokemonListUseCase(pokemonRepository)
    }

    @Provides
    fun provideGetPokemonDetailsByNameUseCase(
        pokemonRepository: PokemonRepository
    ): GetPokemonDetailsByNameUseCase {
        return GetPokemonDetailsByNameUseCase(pokemonRepository)
    }

    @Provides
    fun provideGetNextPokemonPageUseCase(
        pokemonRepository: PokemonRepository
    ): GetNextPokemonPageUseCase {
        return GetNextPokemonPageUseCase(pokemonRepository)
    }
}
