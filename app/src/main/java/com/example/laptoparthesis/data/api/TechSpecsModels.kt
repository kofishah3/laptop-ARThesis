package com.example.laptoparthesis.data.api

import com.google.gson.annotations.SerializedName

data class ProductSearchItem(
    @SerializedName("Product") val product: ProductInfo? = null,
    @SerializedName("Release Date") val releaseDate: String? = null
)

data class ProductInfo(
    @SerializedName("id") val id: String? = null,
    @SerializedName("Model") val name: String? = null,
    @SerializedName("Brand") val brand: String? = null,
    @SerializedName("Category") val category: String? = null,
    @SerializedName("Version") val version: String? = null,
    @SerializedName("Thumbnail") val thumbnail: String? = null
)

data class ProductDetails(
    @SerializedName("Product") val product: ProductInfo? = null,
    @SerializedName("body") val body: Map<String, String>? = null,
    @SerializedName("display") val display: Map<String, String>? = null,
    @SerializedName("inside") val inside: Map<String, String>? = null,
    @SerializedName("back_camera") val backCamera: Map<String, String>? = null,
    @SerializedName("front_camera") val frontCamera: Map<String, String>? = null,
    @SerializedName("battery") val battery: Map<String, String>? = null,
    @SerializedName("wireless") val wireless: Map<String, String>? = null
)

