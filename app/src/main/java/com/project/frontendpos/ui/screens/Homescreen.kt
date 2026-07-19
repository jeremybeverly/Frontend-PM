package com.project.frontendpos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.frontendpos.data.model.product.ProductResponse
import com.project.frontendpos.ui.components.*
import com.project.frontendpos.ui.navigation.CheckoutRoute
import com.project.frontendpos.viewmodel.CartViewModel
import com.project.frontendpos.viewmodel.ProductUiState
import com.project.frontendpos.viewmodel.ProductViewModel
import com.project.frontendpos.viewmodel.ModifierViewModel
import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.ui.navigation.LoginRoute
import com.project.frontendpos.ui.navigation.ShiftRoute
import com.project.frontendpos.viewmodel.ShiftViewModel
import com.project.frontendpos.viewmodel.ShiftUiState
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    modifierViewModel: ModifierViewModel,
    shiftViewModel: ShiftViewModel
) {
    val productState = productViewModel.uiState.value
    val cart by cartViewModel.cart.collectAsState()

    val shiftState = shiftViewModel.uiState.value
    val activeShift = (shiftState as? ShiftUiState.Active)?.shift

    val itemCount = cart.items.sumOf { it.quantity }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<ProductResponse?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val categories = listOf("", "coffee", "non-coffee", "pastry", "others")

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Katalog Produk",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {
                        SessionManager.clearSession()
                        cartViewModel.clear()
                        navController.navigate(LoginRoute) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ProductSearchBar(
                query = productViewModel.searchQuery.value,
                onQueryChange = { productViewModel.onSearchChanged(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    CategoryChip(
                        title = if (category.isBlank()) "Semua" else category,
                        selected = category == productViewModel.selectedCategory.value,
                        onSelected = { productViewModel.onCategorySelected(category) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                when (productState) {
                    ProductUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is ProductUiState.Error -> {
                        Text(productState.message, modifier = Modifier.align(Alignment.Center))
                    }
                    is ProductUiState.Success -> {
                        val validProducts = productState.products.filter { it.product_name.isNotBlank() }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(validProducts) { product ->
                                ProductCard(
                                    product = product,
                                    quantity = cartViewModel.getQuantity(product),
                                    onClick = {
                                        selectedProduct = product
                                        modifierViewModel.loadCustomization(product._id)
                                        showBottomSheet = true
                                    }
                                )
                            }
                        }
                    }
                }


                if (cart.items.isNotEmpty()) {
                    Button(
                        onClick = {
                            if (activeShift != null && activeShift.status == "active") {
                                navController.navigate(CheckoutRoute)
                            } else {
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Anda harus memulai shift kerja terlebih dahulu sebelum transaksi.",
                                        actionLabel = "Buka Shift",
                                        duration = SnackbarDuration.Long
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        navController.navigate(ShiftRoute(fromCart = true))
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                            .fillMaxWidth(0.9f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1C355E)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$itemCount Item dalam Keranjang",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (showBottomSheet && selectedProduct != null) {
                ModifierBottomSheet(
                    modifierViewModel = modifierViewModel,
                    onDismiss = {
                        showBottomSheet = false
                        selectedProduct = null
                        modifierViewModel.reset()
                    },
                    onAddToCart = { modifiers, notes, qty ->
                        cartViewModel.addProduct(
                            product = selectedProduct!!,
                            modifiers = modifiers,
                            note = notes,
                            qty = qty
                        )
                        showBottomSheet = false
                        selectedProduct = null
                        modifierViewModel.reset()
                    }
                )
            }
        }
    }
}