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
class ComponentViewModel @Inject constructor(
    private val repository: TechSpecsRepository
) : ViewModel() {

    private val _components = MutableStateFlow<List<ProductSearchItem>>(emptyList())
    val components = _components.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        searchComponents("hardware")
    }

    fun searchComponents(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.searchComponents(query)
                .onSuccess {
                    _components.value = it
                }
                .onFailure {
                    _error.value = it.message ?: "Unknown error occurred"
                }
            _isLoading.value = false
        }
    }
}
