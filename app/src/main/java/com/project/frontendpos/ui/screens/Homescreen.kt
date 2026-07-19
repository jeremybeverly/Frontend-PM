package com.project.frontendpos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.frontendpos.data.model.product.ProductResponse
import com.project.frontendpos.ui.components.*
import com.project.frontendpos.viewmodel.CartViewModel
import com.project.frontendpos.viewmodel.ProductUiState
import com.project.frontendpos.viewmodel.ProductViewModel
import com.project.frontendpos.viewmodel.ModifierViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    modifierViewModel: ModifierViewModel
){
    val productState = productViewModel.uiState.value
    val cart by cartViewModel.cart.collectAsState()
    val itemCount = cart.items.sumOf { it.quantity }
    val productSubtotal = cart.items.sumOf { it.product.price * it.quantity }
    val modifierSubtotal = cart.items.sumOf { item ->
        item.modifiers.sumOf { it.extraPrice } * item.quantity
    }

    var selectedProduct by remember {
        mutableStateOf<ProductResponse?>(null)
    }

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val categories = listOf(
        "",
        "coffee",
        "non-coffee",
        "pastry",
        "others"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Katalog Produk",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = {
                    // TODO logout
                }
            ) {
                Icon(Icons.Default.Logout, null)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        //searchbar
        ProductSearchBar(
            query = productViewModel.searchQuery.value,
            onQueryChange = {
                productViewModel.onSearchChanged(it)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        //categories
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(categories){ category ->
                CategoryChip(
                    title = if (category.isBlank()) "Semua" else category,
                    selected = category == productViewModel.selectedCategory.value,
                    onSelected = {
                        productViewModel.onCategorySelected(category)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //products
        when (productState) {
            ProductUiState.Loading -> {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProductUiState.Error -> {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(productState.message)
                }
            }

            is ProductUiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productState.products) { product ->
                        ProductCard(
                            product = product,
                            quantity = cartViewModel.getQuantity(product),
                            onClick = {
                                selectedProduct = product
                                showBottomSheet = true
                                modifierViewModel.loadCustomization(
                                    product._id
                                )
                            }

                        )
                    }
                }
            }
        }

        //cart
        Text(
            text = "Pesanan",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.heightIn(max = 180.dp)
        ) {
            items(cart.items) { item ->
                CartItemCard(
                    item = item,
                    onIncrease = {
                        cartViewModel.increase(item)
                    },
                    onDecrease = {
                        cartViewModel.decrease(item)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        OrderSummary(
            itemCount = itemCount,
            productSubtotal = productSubtotal,
            modifierSubtotal = modifierSubtotal,
            tax = cartViewModel.tax,
            total = cartViewModel.total,
            enabled = cart.items.isNotEmpty(),
            onCheckout = {
                navController.navigate("checkout")
            }
        )

        if(showBottomSheet && selectedProduct != null){
            ModifierBottomSheet(
                modifierViewModel = modifierViewModel,
                onDismiss = {
                    showBottomSheet = false
                },
                onAddToCart = {
                        modifiers,
                        notes ->
                    cartViewModel.addProduct(
                        selectedProduct!!,
                        modifiers,
                        notes
                    )
                    showBottomSheet = false
                }
            )
        }
    }
}
