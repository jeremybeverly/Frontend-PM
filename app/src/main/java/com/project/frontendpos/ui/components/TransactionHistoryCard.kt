package com.project.frontendpos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.frontendpos.data.model.transaction.TransactionHistory
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionHistoryCard(
    transaction: TransactionHistory,
    onClick: () -> Unit = {}
) {

    val rupiah =
        NumberFormat.getCurrencyInstance(
            Locale("in", "ID")
        )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                onClick()
            }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                Icons.Default.Receipt,
                contentDescription = null
            )

            Spacer(
                Modifier.width(16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    transaction.invoice_number,
                    fontWeight = FontWeight.Bold
                )

                Text(transaction.payment_method)

                Text(transaction.status)

            }

            Text(
                rupiah.format(
                    transaction.total_amount
                )
            )

        }

    }

}