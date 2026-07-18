package com.project.frontendpos.ui.features.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProductScreen(viewModel: ProductViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    var nameInput by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Add New Product", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = priceInput,
            onValueChange = { priceInput = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val price = priceInput.toDoubleOrNull() ?: 0.0
                if (nameInput.isNotBlank() && price > 0) {
                    viewModel.addProduct(nameInput, price)
                    nameInput = ""
                    priceInput = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Product")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Product List", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            when (val current = state) {
                is ProductState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is ProductState.Error -> Text("Error: ${current.message}", color = Color.Red, modifier = Modifier.align(Alignment.Center))
                is ProductState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(current.list) { product ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Row(
                                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(product.name, style = MaterialTheme.typography.bodyLarge)
                                        Text("$${product.price}", style = MaterialTheme.typography.bodyMedium)
                                    }
                                    IconButton(onClick = { viewModel.deleteProduct(product._id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}