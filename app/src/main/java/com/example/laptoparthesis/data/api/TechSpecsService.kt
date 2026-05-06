package com.example.laptoparthesis.data.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TechSpecsService {
    
    @GET("products/search")
    suspend fun searchProducts(
        @Query("query") query: String,
        @Query("category") category: String? = null,
        @Header("x-api-id") apiId: String,
        @Header("x-api-key") apiKey: String
    ): TechSpecsResponse<List<ProductSearchItem>>

    @GET("product/{id}")
    suspend fun getProductDetails(
        @Path("id") productId: String,
        @Header("x-api-id") apiId: String,
        @Header("x-api-key") apiKey: String
    ): TechSpecsResponse<ProductDetails>

    @GET("categories")
    suspend fun getCategories(
        @Header("x-api-id") apiId: String,
        @Header("x-api-key") apiKey: String
    ): TechSpecsResponse<List<String>>

    @GET("brands")
    suspend fun getBrands(
        @Header("x-api-id") apiId: String,
        @Header("x-api-key") apiKey: String
    ): TechSpecsResponse<List<String>>
}
