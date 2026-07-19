package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.frontendpos.data.model.cart.CartItem
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartItemCard(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {

    val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(item.product.product_name)
            Text(
                rupiah.format(item.product.price)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onDecrease) {
                Icon(Icons.Default.Remove, null)
            }

            Text(item.quantity.toString())

            IconButton(onClick = onIncrease) {
                Icon(Icons.Default.Add, null)
            }
        }
    }
}