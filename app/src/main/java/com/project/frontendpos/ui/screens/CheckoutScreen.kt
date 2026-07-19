package com.project.frontendpos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.project.frontendpos.data.model.cart.CartItem
import com.project.frontendpos.viewmodel.CartViewModel
import com.project.frontendpos.viewmodel.CheckoutUiState
import com.project.frontendpos.viewmodel.CheckoutViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel = viewModel()
) {
    val cart by cartViewModel.cart.collectAsState()
    val state = checkoutViewModel.uiState.value
    var paymentMethod by remember { mutableStateOf("cash") }
    val rupiahFormatter = remember { rupiahFormatter() }

    LaunchedEffect(state) {
        if (state is CheckoutUiState.Success) {
            cartViewModel.clear()
            checkoutViewModel.resetState()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Checkout", fontWeight = FontWeight.Bold)
                        Text(
                            text = "${cart.items.sumOf { it.quantity }} item",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        bottomBar = {
            CheckoutBottomBar(
                subtotal = cartViewModel.subtotal,
                tax = cartViewModel.tax,
                total = cartViewModel.total,
                paymentMethod = paymentMethod,
                onPaymentMethodChange = { paymentMethod = it },
                isLoading = state is CheckoutUiState.Loading,
                isCheckoutEnabled = cart.items.isNotEmpty(),
                errorMessage = (state as? CheckoutUiState.Error)?.message,
                formatCurrency = rupiahFormatter::format,
                onCancel = {
                    cartViewModel.clear()
                    navController.popBackStack()
                },
                onCheckout = { checkoutViewModel.checkout(cart.items, paymentMethod) }
            )
        }
    ) { innerPadding ->
        if (cart.items.isEmpty()) {
            EmptyCheckout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cart.items, key = { item ->
                    "${item.product._id}-${item.note}-${item.modifiers.joinToString { it.id }}"
                }) { item ->
                    CheckoutItemCard(
                        item = item,
                        formatCurrency = rupiahFormatter::format,
                        onIncrease = { cartViewModel.increase(item) },
                        onDecrease = { cartViewModel.decrease(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckoutBottomBar(
    subtotal: Double,
    tax: Double,
    total: Double,
    paymentMethod: String,
    onPaymentMethodChange: (String) -> Unit,
    isLoading: Boolean,
    isCheckoutEnabled: Boolean,
    errorMessage: String?,
    formatCurrency: (Double) -> String,
    onCancel: () -> Unit,
    onCheckout: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(formatCurrency(subtotal), style = MaterialTheme.typography.bodyMedium)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pajak 11%", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(formatCurrency(tax), style = MaterialTheme.typography.bodyMedium)
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    formatCurrency(total),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            PaymentMethodSelector(paymentMethod, onPaymentMethodChange)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    enabled = !isLoading,
                    modifier = Modifier.weight(0.35f).height(48.dp)
                ) {
                    Text("Batal")
                }
                Button(
                    onClick = onCheckout,
                    enabled = isCheckoutEnabled && !isLoading,
                    modifier = Modifier.weight(0.65f).height(48.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Bayar", fontWeight = FontWeight.Bold)
                    }
                }
            }

            errorMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentMethodSelector(
    paymentMethod: String,
    onPaymentMethodChange: (String) -> Unit
) {
    val options = listOf("cash" to "Tunai", "qris" to "QRIS")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Metode pembayaran",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, (value, label) ->
                SegmentedButton(
                    selected = paymentMethod == value,
                    onClick = { onPaymentMethodChange(value) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    label = { Text(label) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CheckoutItemCard(
    item: CartItem,
    formatCurrency: (Double) -> String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            ProductThumbnail(
                imageUrl = item.product.fullImageUrl,
                productName = item.product.product_name
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.product_name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = formatCurrency(item.product.price),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                if (item.modifiers.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = item.modifiers.joinToString(separator = " · ") { it.modifierName },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (item.note.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.EditNote,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = item.note,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            QuantitySelector(
                quantity = item.quantity,
                onIncrease = onIncrease,
                onDecrease = onDecrease
            )
        }
    }
}

@Composable
private fun ProductThumbnail(imageUrl: String?, productName: String) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Image,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = productName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun QuantitySelector(quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FilledTonalIconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Add, contentDescription = "Tambah", modifier = Modifier.size(18.dp))
        }
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        FilledTonalIconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Remove, contentDescription = "Kurangi", modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun EmptyCheckout(modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text("Keranjang masih kosong", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                "Tambahkan produk dari katalog untuk mulai checkout.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun rupiahFormatter(): NumberFormat = NumberFormat.getCurrencyInstance(
    Locale.Builder().setLanguage("id").setRegion("ID").build()
).apply {
    maximumFractionDigits = 0
    minimumFractionDigits = 0
}
