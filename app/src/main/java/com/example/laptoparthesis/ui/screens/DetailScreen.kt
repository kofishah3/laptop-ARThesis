package com.example.laptoparthesis.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.laptoparthesis.ui.viewmodels.DetailViewModel
import com.example.laptoparthesis.ui.components.SectionHeader
import com.example.laptoparthesis.ui.components.SpecRow
import com.example.laptoparthesis.ui.components.TealButton
import com.example.laptoparthesis.ui.theme.LaptopARThesisTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DetailScreen(
    id: String,
    type: String,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val productDetails by viewModel.productDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    androidx.compose.runtime.LaunchedEffect(id) {
        viewModel.getProductDetails(id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
            }
        } else if (productDetails != null) {
            val product = productDetails!!
            SectionHeader(text = product.product?.name ?: "Unknown Product")
            
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = product.product?.category ?: "Uncategorized",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Brand",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = product.product?.brand ?: "Unknown Brand",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Technical Specifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                val allSpecs = listOf(
                    "Body" to product.body,
                    "Display" to product.display,
                    "Inside" to product.inside,
                    "Back Camera" to product.backCamera,
                    "Front Camera" to product.frontCamera,
                    "Battery" to product.battery,
                    "Wireless" to product.wireless
                ).filter { it.second != null }

                if (allSpecs.isNotEmpty()) {
                    allSpecs.forEach { (category, specs) ->
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                        )
                        specs?.forEach { (label, value) ->
                            SpecRow(label = label, value = value)
                        }
                    }
                } else {
                    Text(
                        text = "No specifications available",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TealButton(
                    text = "View in 3D",
                    onClick = { /* Placeholder */ },
                    modifier = Modifier.weight(1f)
                )
                
                TealButton(
                    text = "AR Mode",
                    onClick = { /* Placeholder */ },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    LaptopARThesisTheme {
        DetailScreen(id = "Example Item", type = "Hardware Category")
    }
}
