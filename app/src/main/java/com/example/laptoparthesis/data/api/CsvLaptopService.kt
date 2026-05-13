package com.example.laptoparthesis.data.api

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvLaptopService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val laptops = mutableListOf<CsvLaptop>()

    init {
        loadLaptops()
    }

    private fun loadLaptops() {
        try {
            val inputStream = context.assets.open("datasets/laptops.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            // Skip header
            reader.readLine()
            
            var line: String? = reader.readLine()
            while (line != null) {
                val tokens = parseCsvLine(line)
                if (tokens.size >= 22) {
                    try {
                        laptops.add(
                            CsvLaptop(
                                index = tokens[0].toInt(),
                                brand = tokens[1],
                                model = tokens[2],
                                price = tokens[3].toLong(),
                                rating = tokens[4].toInt(),
                                processorBrand = tokens[5],
                                processorTier = tokens[6],
                                numCores = tokens[7].toInt(),
                                numThreads = tokens[8].toInt(),
                                ramMemory = tokens[9].toInt(),
                                primaryStorageType = tokens[10],
                                primaryStorageCapacity = tokens[11].toInt(),
                                secondaryStorageType = tokens[12],
                                secondaryStorageCapacity = tokens[13].toInt(),
                                gpuBrand = tokens[14],
                                gpuType = tokens[15],
                                isTouchScreen = tokens[16].toBoolean(),
                                displaySize = tokens[17].toDouble(),
                                resolutionWidth = tokens[18].toInt(),
                                resolutionHeight = tokens[19].toInt(),
                                os = tokens[20],
                                yearOfWarranty = tokens[21]
                            )
                        )
                    } catch (e: Exception) {
                        // Skip malformed lines
                    }
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        var cur = StringBuilder()
        var inQuotes = false
        for (ch in line) {
            if (ch == '\"') {
                inQuotes = !inQuotes
            } else if (ch == ',' && !inQuotes) {
                result.add(cur.toString().trim())
                cur = StringBuilder()
            } else {
                cur.append(ch)
            }
        }
        result.add(cur.toString().trim())
        return result
    }

    fun getAllLaptops(): List<CsvLaptop> = laptops

    fun searchLaptops(query: String): List<CsvLaptop> {
        return laptops.filter { 
            it.model.contains(query, ignoreCase = true) || 
            it.brand.contains(query, ignoreCase = true) 
        }
    }
}
