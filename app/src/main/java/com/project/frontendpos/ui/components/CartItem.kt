package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.frontendpos.data.model.cart.CartItem
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartItemCard(
    item: CartItem,
    onIncrease: (() -> Unit)? = null,
    onDecrease: (() -> Unit)? = null
) {

    val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = item.product.product_name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            item.modifiers.forEach {
                Text(
                    text = "• ${it.modifierName}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (item.note.isNotBlank()) {
                Text(
                    text = "Note: ${item.note}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = rupiah.format(item.product.price),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (onIncrease != null && onDecrease != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDecrease
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }
                Text(
                    text = item.quantity.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(
                    onClick = onIncrease
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
            }
        }

        else {
            Text(
                text = "x${item.quantity}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}