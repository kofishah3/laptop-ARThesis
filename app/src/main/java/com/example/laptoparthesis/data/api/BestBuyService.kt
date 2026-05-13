package com.example.laptoparthesis.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BestBuyService {
    @GET("v1/products({query})")
    suspend fun searchProducts(
        @Path(value = "query", encoded = true) query: String,
        @Query("apiKey") apiKey: String,
        @Query("format") format: String = "json",
        @Query("show") show: String = "sku,name,manufacturer,thumbnailImage,regularPrice,modelNumber,details,categoryPath",
        @Query("pageSize") pageSize: Int = 20
    ): BestBuyResponse

    @GET("v1/products/{sku}.json")
    suspend fun getProductDetails(
        @Path("sku") sku: Long,
        @Query("apiKey") apiKey: String,
        @Query("show") show: String = "sku,name,manufacturer,thumbnailImage,regularPrice,modelNumber,details,categoryPath"
    ): BestBuyProduct
}
