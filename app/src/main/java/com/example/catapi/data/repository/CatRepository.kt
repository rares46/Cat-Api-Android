package com.example.catapi.data.repository

import com.example.catapi.data.model.CatImage
import com.example.catapi.data.remote.RetrofitClient
import java.io.IOException
import retrofit2.HttpException

object CatRepository {

    private val api = RetrofitClient.instance

    private var catCache: List<CatImage> = emptyList()

    // Helper to format error messages
    private fun handleError(e: Throwable): String {
        return when (e) {
            is IOException -> "No network. Please check your internet connection."
            is HttpException -> "Server error: ${e.code()}. Bad URL or server is down."
            else -> "Other error occurred: ${e.message}"
        }
    }

    // Result Success or Failure
    suspend fun getCatImages(): Result<List<CatImage>> {
        return try {
            val images = api.searchImages(limit = 20, hasBreeds = true)
            // Filter out images that didn't return breed info
            val imagesWithBreeds = images.filter { it.breeds.isNotEmpty() }
            catCache = imagesWithBreeds
            Result.success(imagesWithBreeds)
        } catch (e: Exception) {
            catCache = emptyList() // Golire cache la eroare
            Result.failure(Exception(handleError(e)))
        }
    }

    suspend fun getCatDetails(imageId: String): Result<CatImage> {
        // Search in cache first
        val cachedCat = catCache.find { it.id == imageId }
        if (cachedCat != null) {
            return Result.success(cachedCat)
        }

        return try {
            val image = api.getImageDetails(imageId)
            Result.success(image)
        } catch (e: Exception) {
            Result.failure(Exception(handleError(e)))
        }
    }
}