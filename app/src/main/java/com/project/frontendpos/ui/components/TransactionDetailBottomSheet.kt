package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.frontendpos.viewmodel.TransactionDetailUiState
import java.text.NumberFormat
import java.util.Locale
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

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
    val details = state.response.data.details

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Text(
                text = transaction.invoice_number,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                Modifier.height(8.dp)
            )

            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                "Payment : ${transaction.payment_method.uppercase()}"
            )

            Text(
                "Status : ${transaction.status.uppercase()}"
            )

            Spacer(
                Modifier.height(20.dp)
            )

            Divider()

            Spacer(
                Modifier.height(12.dp)
            )

            LazyColumn(
                modifier = Modifier.heightIn(max = 350.dp)
            ) {

                items(details) { item ->

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {

                        Text(
                            "${item.product_name} x${item.quantity}",
                            fontWeight = FontWeight.SemiBold
                        )

                        item.selected_modifiers.forEach {

                            Text(
                                "+ ${it.modifier_name}"
                            )

                        }

                        if (!item.note.isNullOrBlank()) {

                            Text(
                                "Catatan: ${item.note}"
                            )

                        }

                        Spacer(
                            Modifier.height(4.dp)
                        )

                        Text(
                            rupiah.format(item.subtotal)
                        )

                    }

                    HorizontalDivider()

                }

            }

            Spacer(
                Modifier.height(16.dp)
            )

            Text(
                "Subtotal : ${rupiah.format(transaction.subtotal)}"
            )

            Text(
                "Tax : ${rupiah.format(transaction.tax_amount)}"
            )

            Text(
                "Total : ${rupiah.format(transaction.total_amount)}",
                fontWeight = FontWeight.Bold
            )

            Spacer(
                Modifier.height(24.dp)
            )

        }

    }

}