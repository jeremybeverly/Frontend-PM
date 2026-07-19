package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrderSummary(
    itemCount: Int,
    productSubtotal: Double,
    modifierSubtotal: Double,
    tax: Double,
    total: Double,
    enabled: Boolean,
    onCheckout: () -> Unit
) {

    val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        HorizontalDivider()

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Ringkasan Pesanan",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        SummaryRow(
            title = "Jumlah Item",
            value = "$itemCount item"
        )

        SummaryRow(
            title = "Subtotal Produk",
            value = rupiah.format(productSubtotal)
        )

        SummaryRow(
            title = "Modifier",
            value = rupiah.format(modifierSubtotal)
        )

        SummaryRow(
            title = "Pajak (11%)",
            value = rupiah.format(tax)
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )

        SummaryRow(
            title = "Total",
            value = rupiah.format(total),
            isTotal = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            onClick = onCheckout
        ) {
            Text("Bayar ${rupiah.format(total)}")
        }
    }
}

@Composable
private fun SummaryRow(
    title: String,
    value: String,
    isTotal: Boolean = false
) {

    val style =
        if (isTotal)
            MaterialTheme.typography.titleMedium
        else
            MaterialTheme.typography.bodyLarge
    val weight =
        if (isTotal)
            FontWeight.Bold
        else
            FontWeight.Normal
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = style,
            fontWeight = weight
        )

        Text(
            text = value,
            style = style,
            fontWeight = weight
        )
    }
}