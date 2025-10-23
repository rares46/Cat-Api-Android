package com.example.catapi.data.model

import com.squareup.moshi.Json

// Main object for each cat
data class CatImage(
    val id: String,
    val url: String,
    val breeds: List<Breed>
)

// Details for the second screen
data class Breed(
    val id: String,
    val name: String,
    val origin: String,
    val temperament: String,
    @Json(name = "life_span") val lifeSpan: String,
    val description: String,
    @Json(name = "wikipedia_url") val wikipediaUrl: String?,
    @Json(name = "vetstreet_url") val vetstreetUrl: String?,
    val intelligence: Int,
    @Json(name = "affection_level") val affectionLevel: Int,
    @Json(name = "child_friendly") val childFriendly: Int,
    @Json(name = "social_needs") val socialNeeds: Int
)