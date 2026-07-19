package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.frontendpos.viewmodel.CashflowUiState

@Composable
fun CashflowHistorySection(
    state: CashflowUiState
) {

    Spacer(
        Modifier.height(24.dp)
    )

    Text(
        text = "Riwayat Cashflow",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )

    Spacer(
        Modifier.height(12.dp)
    )

    when (state) {
        CashflowUiState.Loading -> {
            CircularProgressIndicator()
        }

        is CashflowUiState.Success -> {
            if (state.cashflows.isEmpty()) {
                Text(
                    "Belum ada transaksi cashflow"
                )
            } else {

                LazyColumn(
                    modifier = Modifier.height(220.dp)
                ) {
                    items(state.cashflows) {
                        CashflowHistoryCard(it)
                    }
                }
            }
        }

        is CashflowUiState.Error -> {
            Text(state.message)
        }
        else -> {}
    }
}