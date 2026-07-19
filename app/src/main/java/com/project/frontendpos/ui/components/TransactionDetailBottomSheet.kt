package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.frontendpos.viewmodel.TransactionDetailUiState
import java.text.NumberFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailBottomSheet(
    state: TransactionDetailUiState,
    onDismiss: () -> Unit
) {

    if (state !is TransactionDetailUiState.Success) return

    val rupiah =
        NumberFormat.getCurrencyInstance(
            Locale("in", "ID")
        )

    val transaction = state.response.data.transaction
    val details = state.response.data.details

    val formattedDate = try {
        OffsetDateTime
            .parse(transaction.createdAt)
            .format(
                DateTimeFormatter.ofPattern(
                    "dd MMM yyyy • HH:mm",
                    Locale("id", "ID")
                )
            )
    } catch (e: Exception) {
        transaction.createdAt
    }

    val paymentLabel =
        when (transaction.payment_method.lowercase()) {
            "cash" -> "Cash"
            "qris" -> "QRIS"
            else -> transaction.payment_method
        }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
        ) {

            HorizontalDivider()
            Spacer(Modifier.height(20.dp))
            Text(
                "Invoice",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                transaction.invoice_number,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))
            Text(
                formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector =
                        if (transaction.payment_method.equals("cash", true))
                            Icons.Default.Payments
                        else
                            Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))

                Text(
                    paymentLabel,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.heightIn(max = 320.dp)
            ) {
                items(details) { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "${item.product_name} x${item.quantity}",
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                rupiah.format(item.subtotal),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        item.selected_modifiers.forEach {
                            Text(
                                text = "• ${it.modifier_name}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (!item.note.isNullOrBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Catatan",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                item.note,
                                style = MaterialTheme.typography.bodySmall
                            )

                        }

                    }

                    HorizontalDivider()

                }

            }

            Spacer(Modifier.height(20.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text("Subtotal")

                Text(
                    rupiah.format(transaction.subtotal)
                )

            }

            Spacer(Modifier.height(6.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pajak")
                Text(
                    rupiah.format(transaction.tax_amount)
                )
            }

            Spacer(Modifier.height(12.dp))

            HorizontalDivider(
                thickness = 2.dp
            )

            Spacer(Modifier.height(12.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    "TOTAL",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    rupiah.format(transaction.total_amount),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

            }

            Spacer(Modifier.height(24.dp))

        }

    }

}