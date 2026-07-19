package com.project.frontendpos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.data.model.transaction.TransactionDetailResponse
import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.repository.TransactionHistoryRepository
import kotlinx.coroutines.launch

sealed interface TransactionDetailUiState {

    object Idle : TransactionDetailUiState

    object Loading : TransactionDetailUiState

    data class Success(
        val response: TransactionDetailResponse
    ) : TransactionDetailUiState

    data class Error(
        val message: String
    ) : TransactionDetailUiState
}

class TransactionDetailViewModel : ViewModel() {

    private val repository =
        TransactionHistoryRepository(
            RetrofitClient.historyApi
        )

    private val _uiState =
        mutableStateOf<TransactionDetailUiState>(
            TransactionDetailUiState.Idle
        )

    val uiState: State<TransactionDetailUiState> = _uiState

    fun loadTransaction(id: String) {

        viewModelScope.launch {

            _uiState.value =
                TransactionDetailUiState.Loading

            repository.getTransactionDetail(
                SessionManager.formattedToken,
                id
            )
                .onSuccess {

                    _uiState.value =
                        TransactionDetailUiState.Success(it)

                }
                .onFailure {

                    _uiState.value =
                        TransactionDetailUiState.Error(
                            it.message ?: "Gagal memuat transaksi"
                        )

                }

        }

    }

    fun reset() {
        _uiState.value =
            TransactionDetailUiState.Idle
    }

}