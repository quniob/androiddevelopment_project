package com.example.androiddevelopment_project.data.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.androiddevelopment_project.GetMovieByIdQuery
import com.example.androiddevelopment_project.GetPopularMoviesQuery

class MovieDataSource(private val apolloClient: ApolloClient) {
    
    private val popularMovieIds = listOf(
        "tt0111161",
        "tt0068646",
        "tt0468569",
        "tt0071562",
        "tt0050083",
        "tt0108052",
        "tt0167260",
        "tt0110912",
        "tt0060196",
        "tt0137523",
        "tt0109830",
        "tt0120737",
        "tt0133093",
        "tt0099685",
        "tt0073486",
        "tt0080684",
        "tt0038650",
        "tt0034583",
        "tt0047478",
        "tt0114369"
    )
    
    suspend fun getMovieById(id: String): GetMovieByIdQuery.Data? {
        return try {
            val response = apolloClient.query(GetMovieByIdQuery(id = id)).execute()
            if (response.hasErrors()) {
                throw Exception(response.errors?.firstOrNull()?.message ?: "Unknown error")
            }
            response.data
        } catch (e: Exception) {
            throw e
        }
    }
    
    suspend fun getPopularMovies(): List<GetPopularMoviesQuery.Title> {
        return try {
            val response = apolloClient.query(GetPopularMoviesQuery(ids = popularMovieIds)).execute()
            if (response.hasErrors()) {
                throw Exception(response.errors?.firstOrNull()?.message ?: "Unknown error")
            }
            response.data?.titles ?: emptyList()
        } catch (e: Exception) {
            throw e
        }
    }
    
    suspend fun getMoviesByIds(ids: List<String>): List<GetPopularMoviesQuery.Title> {
        return try {
            val response = apolloClient.query(GetPopularMoviesQuery(ids = ids)).execute()
            if (response.hasErrors()) {
                throw Exception(response.errors?.firstOrNull()?.message ?: "Unknown error")
            }
            response.data?.titles ?: emptyList()
        } catch (e: Exception) {
            throw e
        }
    }
} 