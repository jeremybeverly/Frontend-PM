package com.project.frontendpos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.data.model.transaction.TransactionHistory
import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.repository.TransactionHistoryRepository
import kotlinx.coroutines.launch

sealed interface HistoryUiState {

    object Loading : HistoryUiState

    object Idle : HistoryUiState

    data class Success(
        val transactions: List<TransactionHistory>
    ) : HistoryUiState

    data class Error(
        val message: String
    ) : HistoryUiState
}

class TransactionHistoryViewModel : ViewModel() {

    private val repository =
        TransactionHistoryRepository(
            RetrofitClient.historyApi
        )

    private val _uiState =
        mutableStateOf<HistoryUiState>(
            HistoryUiState.Loading
        )

    val uiState: State<HistoryUiState> = _uiState

    init {
        loadHistory()
    }

    fun loadHistory(
        invoiceNumber: String? = null
    ) {
        viewModelScope.launch {
            repository.getTransactions(
                token = SessionManager.formattedToken,
                invoiceNumber = invoiceNumber
            )
                .onSuccess {

                    _uiState.value =
                        HistoryUiState.Success(
                            it.data
                        )
                }
                .onFailure {

                    _uiState.value =
                        HistoryUiState.Error(
                            it.message
                                ?: "Gagal memuat riwayat transaksi"
                        )
                }
        }
    }

    fun refresh() {
        loadHistory()
    }
}