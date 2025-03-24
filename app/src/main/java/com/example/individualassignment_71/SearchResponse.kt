package com.example.individualassignment_71

import com.squareup.moshi.Json

data class SearchResponse(
    val meals: List<Meal>?
)

data class Meal(
    @Json(name = "strMeal") val name: String,
    @Json(name = "strCategory") val category: String,
    @Json(name = "strArea") val area: String,
    @Json(name = "strMealThumb") val imageUrl: String
)