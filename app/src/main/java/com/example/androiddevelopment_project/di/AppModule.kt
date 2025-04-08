package com.example.androiddevelopment_project.di

import com.example.androiddevelopment_project.data.network.ImdbApolloClient
import com.example.androiddevelopment_project.data.network.MovieDataSource
import com.example.androiddevelopment_project.data.repository.MovieRepositoryImpl
import com.example.androiddevelopment_project.domain.repository.MovieRepository
import com.example.androiddevelopment_project.domain.usecase.GetMovieByIdUseCase
import com.example.androiddevelopment_project.domain.usecase.GetPopularMoviesUseCase
import com.example.androiddevelopment_project.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ImdbApolloClient.create() }
    single { MovieDataSource(apolloClient = get()) }
    
    single<MovieRepository> { MovieRepositoryImpl(movieDataSource = get()) }
    
    factory { GetMovieByIdUseCase(movieRepository = get()) }
    factory { GetPopularMoviesUseCase(movieRepository = get()) }
    
    viewModel { MovieViewModel(
        getMovieByIdUseCase = get(),
        getPopularMoviesUseCase = get()
    ) }
} 