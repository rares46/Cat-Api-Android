package com.example.catapi.data.remote

import com.example.catapi.data.model.CatImage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheCatApiService {

    // For the list screen
    @GET("v1/images/search")
    suspend fun searchImages(
        @Query("limit") limit: Int = 20,
        @Query("has_breeds") hasBreeds: Boolean = true
    ): List<CatImage>

    // For the detail screen
    @GET("v1/images/{image_id}")
    suspend fun getImageDetails(
        @Path("image_id") imageId: String
    ): CatImage
}