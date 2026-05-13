package com.example.laptoparthesis.data.api

data class BestBuyResponse(
    val products: List<BestBuyProduct>
)

data class BestBuyProduct(
    val sku: Long,
    val name: String,
    val manufacturer: String?,
    val thumbnailImage: String?,
    val regularPrice: Double?,
    val modelNumber: String?,
    val details: List<BestBuyDetail>?,
    val categoryPath: List<BestBuyCategory>?
)

data class BestBuyDetail(
    val name: String,
    val value: String
)

data class BestBuyCategory(
    val name: String,
    val id: String
)
