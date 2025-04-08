package com.example.androiddevelopment_project.data.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class ImdbApolloClient {
    companion object {
        private const val BASE_URL = "https://graph.imdbapi.dev/v1"
        
        fun create(): ApolloClient {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
                
            return ApolloClient.Builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build()
        }
    }
} 