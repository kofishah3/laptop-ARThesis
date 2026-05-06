package com.example.laptoparthesis.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptoparthesis.data.api.ProductDetails
import com.example.laptoparthesis.data.repository.TechSpecsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: TechSpecsRepository
) : ViewModel() {

    private val _productDetails = MutableStateFlow<ProductDetails?>(null)
    val productDetails = _productDetails.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun getProductDetails(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getProductDetails(id)
                .onSuccess {
                    _productDetails.value = it
                }
                .onFailure {
                    _error.value = it.message ?: "Unknown error occurred"
                }
            _isLoading.value = false
        }
    }
}
