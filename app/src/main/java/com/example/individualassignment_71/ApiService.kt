package com.example.individualassignment_71

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

interface ApiService {
    @GET("/api/json/v1/1/search.php")
    suspend fun getRecipes(
        @Query("s") search: String,
    ): SearchResponse
}

object ApiClient {
    private const val BASE_URL = "https://www.themealdb.com"
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()) // Explicitly add KotlinJsonAdapterFactory
        .build() // Build the Moshi instance


    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}