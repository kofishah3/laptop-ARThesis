package com.example.laptoparthesis.data.repository

import com.example.laptoparthesis.data.api.ProductDetails
import com.example.laptoparthesis.data.api.ProductSearchItem
import com.example.laptoparthesis.data.api.TechSpecsService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TechSpecsRepository @Inject constructor(
    private val apiService: TechSpecsService
) {
    private val apiId = "69fa1d4ad242d8b36b8b43a7"
    private val apiKey = "88d3bc7f-c151-447f-b82a-ad94479acde5"

    suspend fun searchLaptops(query: String): Result<List<ProductSearchItem>> {
        return try {
            val response = apiService.searchProducts(
                query = query,
                category = "Laptops",
                apiId = apiId,
                apiKey = apiKey
            )
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchComponents(query: String): Result<List<ProductSearchItem>> {
        return try {
            val response = apiService.searchProducts(
                query = query,
                apiId = apiId,
                apiKey = apiKey
            )
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductDetails(id: String): Result<ProductDetails> {
        return try {
            val response = apiService.getProductDetails(
                productId = id,
                apiId = apiId,
                apiKey = apiKey
            )
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
