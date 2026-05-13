package com.example.laptoparthesis.ui.screens.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptoparthesis.data.LaptopModel
import com.example.laptoparthesis.data.repository.TechSpecsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarcodeScannerViewModel @Inject constructor(
    private val repository: TechSpecsRepository
) : ViewModel() {

    private val _scanResult = MutableStateFlow<LaptopModel?>(null)
    val scanResult = _scanResult.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isScanning = MutableStateFlow(true)
    val isScanning = _isScanning.asStateFlow()

    fun onBarcodeScanned(barcode: String) {
        if (!_isScanning.value) return
        
        _isScanning.value = false
        viewModelScope.launch {
            repository.getLaptopByBarcode(barcode)
                .onSuccess { laptop ->
                    _scanResult.value = laptop
                    _error.value = null
                }
                .onFailure { e ->
                    _error.value = e.message ?: "Unknown error"
                    // Allow scanning again after a short delay if error
                    _isScanning.value = true
                }
        }
    }

    fun resetScanner() {
        _scanResult.value = null
        _error.value = null
        _isScanning.value = true
    }
}
