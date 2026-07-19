package com.project.frontendpos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.frontendpos.ui.components.TransactionDetailBottomSheet
import com.project.frontendpos.ui.components.TransactionHistoryCard
import com.project.frontendpos.viewmodel.HistoryUiState
import com.project.frontendpos.viewmodel.TransactionDetailViewModel
import com.project.frontendpos.viewmodel.TransactionHistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    transactionHistoryViewModel: TransactionHistoryViewModel,
    transactionDetailViewModel: TransactionDetailViewModel
) {

    val state by transactionHistoryViewModel.uiState

    var expanded by remember {
        mutableStateOf(false)
    }

    var sortType by remember {
        mutableStateOf(SortType.NEWEST)
    }

    var selectedTransactionId by remember {
        mutableStateOf<String?>(null)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val detailState by transactionDetailViewModel.uiState

        TransactionDetailBottomSheet(
            state = detailState,
            onDismiss = {
                selectedTransactionId = null
                transactionDetailViewModel.reset()
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Riwayat Transaksi",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { transactionHistoryViewModel.loadHistory() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                transactionHistoryViewModel.loadHistory(it)
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null
                )
            },
            placeholder = {
                Text("Cari nomor invoice...")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            OutlinedTextField(
                value = when (sortType) {
                    SortType.NEWEST -> "Terbaru"
                    SortType.OLDEST -> "Terlama"
                    SortType.HIGHEST_TOTAL -> "Nominal Tertinggi"
                    SortType.LOWEST_TOTAL -> "Nominal Terendah"
                },
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Urutkan")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                DropdownMenuItem(
                    text = { Text("Terbaru") },
                    onClick = {
                        sortType = SortType.NEWEST
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("Terlama") },
                    onClick = {
                        sortType = SortType.OLDEST
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("Nominal Tertinggi") },
                    onClick = {
                        sortType = SortType.HIGHEST_TOTAL
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("Nominal Terendah") },
                    onClick = {
                        sortType = SortType.LOWEST_TOTAL
                        expanded = false
                    }
                )

            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val currentState = state) {

            HistoryUiState.Loading -> {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

            }

            is HistoryUiState.Success -> {

                val sortedTransactions = when (sortType) {

                    SortType.NEWEST ->
                        currentState.transactions.sortedByDescending {
                            it.createdAt
                        }

                    SortType.OLDEST ->
                        currentState.transactions.sortedBy {
                            it.createdAt
                        }

                    SortType.HIGHEST_TOTAL ->
                        currentState.transactions.sortedByDescending {
                            it.total_amount
                        }

                    SortType.LOWEST_TOTAL ->
                        currentState.transactions.sortedBy {
                            it.total_amount
                        }

                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(sortedTransactions) { transaction ->

                        TransactionHistoryCard(
                            transaction = transaction,
                            onClick = {

                                selectedTransactionId = transaction.id

                                transactionDetailViewModel.loadTransaction(
                                    transaction.id
                                )

                            }
                        )

                    }

                }

            }

            is HistoryUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(currentState.message, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { transactionHistoryViewModel.loadHistory() }) {
                        Text("Coba Lagi")
                    }
                }
            }

            HistoryUiState.Idle -> {}

        }

    }

}