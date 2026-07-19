package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.frontendpos.viewmodel.CashflowUiState

@Composable
fun CashflowCard(
    title: String,
    buttonText: String,
    cashflowState: CashflowUiState,
    onSubmit: (Double, String) -> Unit
) {

    var amount by remember {
        mutableStateOf("")
    }

    var reason by remember {
        mutableStateOf("")
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    val amountValue = amount.toDoubleOrNull()

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it.filter { c ->
                        c.isDigit() || c == '.'
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Jumlah")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = reason,
                onValueChange = {
                    reason = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Alasan")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = amountValue != null &&
                        amountValue > 0 &&
                        reason.isNotBlank(),
                onClick = {
                    showDialog = true
                }
            ) {
                Text(buttonText)
            }

        }

    }

    if (showDialog) {

        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },

            title = {
                Text(title)
            },

            text = {
                Column {
                    Text(
                        "Apakah Anda yakin ingin menambahkan transaksi berikut?"
                    )
                }
            },

            confirmButton = {

                Button(
                    onClick = {

                        amountValue?.let {

                            onSubmit(
                                it,
                                reason
                            )

                            amount = ""
                            reason = ""

                            showDialog = false
                        }
                    }
                ) {
                    Text("Konfirmasi")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Batal")
                }
            }
        )
    }
}
