package com.example.laptoparthesis.data.api

data class CsvLaptop(
    val index: Int,
    val brand: String,
    val model: String,
    val price: Long,
    val rating: Int,
    val processorBrand: String,
    val processorTier: String,
    val numCores: Int,
    val numThreads: Int,
    val ramMemory: Int,
    val primaryStorageType: String,
    val primaryStorageCapacity: Int,
    val secondaryStorageType: String,
    val secondaryStorageCapacity: Int,
    val gpuBrand: String,
    val gpuType: String,
    val isTouchScreen: Boolean,
    val displaySize: Double,
    val resolutionWidth: Int,
    val resolutionHeight: Int,
    val os: String,
    val yearOfWarranty: String
)
