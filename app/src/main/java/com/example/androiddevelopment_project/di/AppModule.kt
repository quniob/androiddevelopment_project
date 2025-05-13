package com.example.androiddevelopment_project.di

import com.example.androiddevelopment_project.data.db.AppDatabase
import com.example.androiddevelopment_project.data.network.ImdbApolloClient
import com.example.androiddevelopment_project.data.network.MovieDataSource
import com.example.androiddevelopment_project.data.preferences.FilterPreferencesManager
import com.example.androiddevelopment_project.data.repository.FavoriteMovieRepositoryImpl
import com.example.androiddevelopment_project.data.repository.MovieRepositoryImpl
import com.example.androiddevelopment_project.domain.repository.FavoriteMovieRepository
import com.example.androiddevelopment_project.domain.repository.MovieRepository
import com.example.androiddevelopment_project.domain.usecase.AddMovieToFavoritesUseCase
import com.example.androiddevelopment_project.domain.usecase.GetFavoriteMovieByIdUseCase
import com.example.androiddevelopment_project.domain.usecase.GetFavoriteMoviesUseCase
import com.example.androiddevelopment_project.domain.usecase.GetMovieByIdUseCase
import com.example.androiddevelopment_project.domain.usecase.GetPopularMoviesUseCase
import com.example.androiddevelopment_project.domain.usecase.IsMovieFavoriteUseCase
import com.example.androiddevelopment_project.domain.usecase.RemoveMovieFromFavoritesUseCase
import com.example.androiddevelopment_project.ui.badge.BadgeStateHolder
import com.example.androiddevelopment_project.viewmodel.FavoriteMovieViewModel
import com.example.androiddevelopment_project.viewmodel.MovieViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ImdbApolloClient.create() }
    single { MovieDataSource(apolloClient = get()) }
    
    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().favoriteMovieDao() }
    
    single { FilterPreferencesManager(androidContext()) }
    
    single { BadgeStateHolder() }
    
    single<MovieRepository> { MovieRepositoryImpl(movieDataSource = get()) }
    single<FavoriteMovieRepository> { FavoriteMovieRepositoryImpl(favoriteMovieDao = get()) }
    
    factory { GetMovieByIdUseCase(movieRepository = get()) }
    factory { GetPopularMoviesUseCase(movieRepository = get()) }
    factory { GetFavoriteMoviesUseCase(favoriteMovieRepository = get()) }
    factory { AddMovieToFavoritesUseCase(favoriteMovieRepository = get()) }
    factory { RemoveMovieFromFavoritesUseCase(favoriteMovieRepository = get()) }
    factory { IsMovieFavoriteUseCase(favoriteMovieRepository = get()) }
    factory { GetFavoriteMovieByIdUseCase(repository = get()) }
    
    viewModel { MovieViewModel(
        getMovieByIdUseCase = get(),
        getPopularMoviesUseCase = get(),
        filterPreferencesManager = get()
    ) }
    
    viewModel { FavoriteMovieViewModel(
        getFavoriteMoviesUseCase = get(),
        addMovieToFavoritesUseCase = get(),
        removeMovieFromFavoritesUseCase = get(),
        isMovieFavoriteUseCase = get(),
        getFavoriteMovieByIdUseCase = get()
    ) }
} 