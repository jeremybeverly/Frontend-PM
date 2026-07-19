package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.frontendpos.data.model.shift.ShiftResponse
import com.project.frontendpos.viewmodel.CashflowUiState
import com.project.frontendpos.viewmodel.ShiftSummaryUiState
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveShiftContent(
    shift: ShiftResponse,
    summaryState: ShiftSummaryUiState,
    cashflowState: CashflowUiState,
    onCashIn: (Double, String) -> Unit,
    onCashOut: (Double, String) -> Unit,
    onEndShift: (Double) -> Unit,
    onExtendShift: () -> Unit // <-- NEW parameter mapped to our button
) {
    val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
        maximumFractionDigits = 0
    }

    var showEndShiftSheet by remember { mutableStateOf(false) }
    var actualCash by remember { mutableStateOf("") }
    var remainingSeconds by remember { mutableLongStateOf(0L) }

    LaunchedEffect(summaryState) {
        if (summaryState is ShiftSummaryUiState.Success) {
            remainingSeconds = summaryState.response.data.remaining_seconds
            while (remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--
            }
        }
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
        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ShiftTimer(remainingSeconds = remainingSeconds)
            }
        }

        if (summaryState is ShiftSummaryUiState.Success) {
            val summary = summaryState.response.data
            Spacer(Modifier.height(20.dp))
            SummaryCard(title = "Starting Cash", value = rupiah.format(summary.starting_cash))
            Spacer(Modifier.height(10.dp))
            SummaryCard(title = "Cash Sales", value = rupiah.format(summary.cash_sales))
            Spacer(Modifier.height(10.dp))
            SummaryCard(title = "Cash In", value = rupiah.format(summary.cash_in))
            Spacer(Modifier.height(10.dp))
            SummaryCard(title = "Cash Out", value = rupiah.format(summary.cash_out))
            Spacer(Modifier.height(30.dp))
        }

        CashflowCard(
            title = "Cash Masuk",
            buttonText = "Tambah",
            cashflowState = cashflowState,
            onSubmit = onCashIn
        )
        Spacer(Modifier.height(16.dp))

        CashflowCard(
            title = "Cash Keluar",
            buttonText = "Tambah",
            cashflowState = cashflowState,
            onSubmit = onCashOut
        )

        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showEndShiftSheet = true }
        ) {
            Text("Selesai Shift")
        }

        if (remainingSeconds == 0L) {
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onExtendShift
            ) {
                Text("+ Tambah 6 Jam")
            }
        }

        if (showEndShiftSheet) {
            ModalBottomSheet(
                onDismissRequest = { showEndShiftSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        "Akhiri Shift",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = actualCash,
                        onValueChange = { input ->
                            actualCash = input.filter { c -> c.isDigit() || c == '.' }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Actual Cash") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        prefix = { Text("Rp ") }
                    )

                    if (summaryState is ShiftSummaryUiState.Success) {
                        val parsedActualCash = actualCash.toDoubleOrNull()

                        if (parsedActualCash != null) {
                            val variance = parsedActualCash - summaryState.response.data.expected_cash
                            Spacer(Modifier.height(20.dp))
                            SummaryCard(
                                title = "Variance",
                                value = rupiah.format(variance),
                                valueColor = if (variance < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f).height(50.dp),
                            onClick = { showEndShiftSheet = false }
                        ) {
                            Text("Batal")
                        }

                        Button(
                            modifier = Modifier.weight(1f).height(50.dp),
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
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}