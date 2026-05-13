package com.example.laptoparthesis.data.repository

import com.example.laptoparthesis.data.api.CsvLaptop
import com.example.laptoparthesis.data.api.CsvLaptopService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvLaptopRepository @Inject constructor(
    private val csvLaptopService: CsvLaptopService
) {
    fun getAllLaptops(): List<CsvLaptop> {
        return csvLaptopService.getAllLaptops()
    }

    fun searchLaptops(query: String): List<CsvLaptop> {
        return csvLaptopService.searchLaptops(query)
    }

    fun getLaptopByIndex(index: Int): CsvLaptop? {
        return csvLaptopService.getAllLaptops().find { it.index == index }
    }
}
