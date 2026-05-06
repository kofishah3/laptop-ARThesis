package com.example.laptoparthesis.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.laptoparthesis.ui.viewmodels.ComponentViewModel
import com.example.laptoparthesis.ui.components.ItemCard
import com.example.laptoparthesis.ui.components.SectionHeader
import com.example.laptoparthesis.ui.theme.LaptopARThesisTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ComponentListScreen(
    onComponentClick: (String) -> Unit,
    viewModel: ComponentViewModel = hiltViewModel()
) {
    val components by viewModel.components.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        SectionHeader(text = "Hardware Components")
        
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(components) { component ->
                    ItemCard(
                        title = component.product?.name ?: "Unknown Component",
                        subtitle = component.product?.brand ?: "Unknown Brand",
                        trailingText = component.product?.category,
                        onClick = { component.product?.id?.let { onComponentClick(it) } }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComponentListScreenPreview() {
    LaptopARThesisTheme {
        ComponentListScreen(onComponentClick = {})
    }
}
