package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.project.frontendpos.viewmodel.CashflowUiState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.frontendpos.data.model.shift.ShiftResponse
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveShiftContent(
    shift: ShiftResponse,
    cashflowState: CashflowUiState,
    onCashIn: (Double, String) -> Unit,
    onCashOut: (Double, String) -> Unit,
    onEndShift: (Double) -> Unit
) {
    val rupiah =
        NumberFormat.getCurrencyInstance(
            Locale("in", "ID")
        )
    var showEndShiftSheet by remember {
        mutableStateOf(false)
    }

    var actualCash by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Shift Anda",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Sedang Berlangsung",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ShiftTimer(
                    shift.startTime
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        CashflowCard(
            title = "Cash Masuk",
            buttonText = "Tambah",
            cashflowState = cashflowState
        ) { amount, reason ->
            onCashIn(amount, reason)
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        CashflowCard(
            title = "Cash Keluar",
            buttonText = "Tambah",
            cashflowState = cashflowState
        ) { amount, reason ->
            onCashOut(amount, reason)
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                showEndShiftSheet = true
            }
        ) {
            Text("Selesai Shift")
        }

        if (showEndShiftSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showEndShiftSheet = false
                }

            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    Text(
                        text = "Akhiri Shift",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )

                    Text(
                        text = "Masukkan jumlah uang kas fisik sebelum menutup shift."
                    )

                    Spacer(
                        Modifier.height(24.dp)
                    )

                    OutlinedTextField(

                        value = actualCash,

                        onValueChange = {

                            actualCash = it.filter {
                                    c ->
                                c.isDigit() || c == '.'
                            }

                        },

                        modifier = Modifier.fillMaxWidth(),

                        label = {
                            Text("Actual Cash")
                        }

                    )

                    Spacer(
                        Modifier.height(24.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                showEndShiftSheet = false
                            }
                        ) {
                            Text("Batal")
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            enabled = actualCash.toDoubleOrNull() != null,
                            onClick = {
                                actualCash.toDoubleOrNull()?.let {
                                    onEndShift(it)
                                    actualCash = ""
                                    showEndShiftSheet = false
                                }
                            }
                        ) {

                            Text("Konfirmasi")
                        }
                    }

                    Spacer(
                        Modifier.height(24.dp)
                    )
                }
            }
        }
    }
}