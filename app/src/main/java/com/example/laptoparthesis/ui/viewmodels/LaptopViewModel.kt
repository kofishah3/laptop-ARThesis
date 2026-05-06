package com.example.laptoparthesis.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptoparthesis.data.api.ProductSearchItem
import com.example.laptoparthesis.data.repository.TechSpecsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaptopViewModel @Inject constructor(
    private val repository: TechSpecsRepository
) : ViewModel() {

    private val _laptops = MutableStateFlow<List<ProductSearchItem>>(emptyList())
    val laptops = _laptops.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        searchLaptops("laptop")
    }

    fun searchLaptops(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.searchLaptops(query)
                .onSuccess {
                    _laptops.value = it
                }
                .onFailure {
                    _error.value = it.message ?: "Unknown error occurred"
                }
            _isLoading.value = false
        }
    }
}
