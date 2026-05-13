package com.example.laptoparthesis.data.repository

import com.example.laptoparthesis.data.*
import com.example.laptoparthesis.data.api.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TechSpecsRepository @Inject constructor(
    private val apiService: BestBuyService,
    private val csvLaptopService: CsvLaptopService
) {
    private val apiKey = "YOUR_BEST_BUY_API_KEY" // Placeholder for Best Buy API Key

    fun getAllLaptopsFromCsv(): List<CsvLaptop> {
        return csvLaptopService.getAllLaptops()
    }

    fun searchLaptopsFromCsv(query: String): List<CsvLaptop> {
        return csvLaptopService.searchLaptops(query)
    }

    suspend fun getLaptopByBarcode(barcode: String): Result<LaptopModel> {
        return try {
            val laptop = MockData.laptops.find { it.barcode == barcode }
            if (laptop != null) {
                Result.success(laptop)
            } else {
                Result.failure(Exception("Product not found for barcode: $barcode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchLaptops(query: String, useCsv: Boolean = true): Result<List<ProductSearchItem>> {
        if (useCsv) {
            val csvResults = csvLaptopService.searchLaptops(query)
            val mappedResults = csvResults.map { laptop ->
                ProductSearchItem(
                    product = ProductInfo(
                        id = "csv-${laptop.index}",
                        name = laptop.model,
                        brand = laptop.brand,
                        category = "Laptops",
                        thumbnail = null
                    )
                )
            }
            return Result.success(mappedResults)
        }
        return try {
            val queryEncoded = java.net.URLEncoder.encode(query, "UTF-8").replace("+", "%20")
            val bbQuery = if (query.equals("laptop", ignoreCase = true)) {
                "categoryPath.name=Laptops"
            } else {
                "search=$queryEncoded&categoryPath.name=Laptops"
            }
            
            val response = apiService.searchProducts(
                query = bbQuery,
                apiKey = apiKey
            )
            val mappedItems = response.products.map { product ->
                ProductSearchItem(
                    product = ProductInfo(
                        id = product.sku.toString(),
                        name = product.name,
                        brand = product.manufacturer,
                        category = product.categoryPath?.lastOrNull()?.name ?: "Laptops",
                        thumbnail = product.thumbnailImage
                    )
                )
            }
            Result.success(mappedItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchComponents(query: String): Result<List<ProductSearchItem>> {
        return try {
            val queryEncoded = java.net.URLEncoder.encode(query, "UTF-8").replace("+", "%20")
            val bbQuery = "search=$queryEncoded"
            val response = apiService.searchProducts(
                query = bbQuery,
                apiKey = apiKey
            )
            val mappedItems = response.products.map { product ->
                ProductSearchItem(
                    product = ProductInfo(
                        id = product.sku.toString(),
                        name = product.name,
                        brand = product.manufacturer,
                        category = product.categoryPath?.lastOrNull()?.name ?: "Components",
                        thumbnail = product.thumbnailImage
                    )
                )
            }
            Result.success(mappedItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductDetails(id: String): Result<ProductDetails> {
        return try {
            // Check if it's a CSV laptop
            if (id.startsWith("csv-")) {
                val index = id.removePrefix("csv-").toIntOrNull()
                val csvLaptop = csvLaptopService.getAllLaptops().find { it.index == index }
                if (csvLaptop != null) {
                    return Result.success(
                        ProductDetails(
                            product = ProductInfo(
                                id = id,
                                name = csvLaptop.model,
                                brand = csvLaptop.brand,
                                category = "Laptops",
                                thumbnail = null
                            ),
                            body = mapOf(
                                "Price" to csvLaptop.price.toString(),
                                "Rating" to csvLaptop.rating.toString(),
                                "Processor" to "${csvLaptop.processorBrand} ${csvLaptop.processorTier}",
                                "Cores" to csvLaptop.numCores.toString(),
                                "Threads" to csvLaptop.numThreads.toString(),
                                "RAM" to "${csvLaptop.ramMemory}GB",
                                "Primary Storage" to "${csvLaptop.primaryStorageCapacity}GB ${csvLaptop.primaryStorageType}",
                                "Secondary Storage" to if (csvLaptop.secondaryStorageType != "No secondary storage") 
                                    "${csvLaptop.secondaryStorageCapacity}GB ${csvLaptop.secondaryStorageType}" else "None",
                                "GPU" to "${csvLaptop.gpuBrand} ${csvLaptop.gpuType}",
                                "Display" to "${csvLaptop.displaySize}\"",
                                "Resolution" to "${csvLaptop.resolutionWidth}x${csvLaptop.resolutionHeight}",
                                "OS" to csvLaptop.os,
                                "Warranty" to csvLaptop.yearOfWarranty
                            )
                        )
                    )
                }
            }

            // First check MockData
            val mockLaptop = MockData.laptops.find { it.id == id }
            if (mockLaptop != null) {
                return Result.success(
                    ProductDetails(
                        product = ProductInfo(
                            id = mockLaptop.id,
                            name = mockLaptop.name,
                            brand = mockLaptop.brand,
                            category = mockLaptop.category,
                            thumbnail = null
                        ),
                        body = mockLaptop.specs
                    )
                )
            }

            // Fallback to BestBuy API (though user doesn't want it, keeping for structure if needed)
            val sku = id.toLongOrNull() ?: return Result.failure(Exception("Product not found in mock data and invalid SKU for API"))
            val product = apiService.getProductDetails(
                sku = sku,
                apiKey = apiKey
            )
            
            val detailsMap = product.details?.associate { it.name to it.value } ?: emptyMap()
            
            val mappedDetails = ProductDetails(
                product = ProductInfo(
                    id = product.sku.toString(),
                    name = product.name,
                    brand = product.manufacturer,
                    category = product.categoryPath?.lastOrNull()?.name,
                    thumbnail = product.thumbnailImage
                ),
                body = detailsMap
            )
            Result.success(mappedDetails)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
