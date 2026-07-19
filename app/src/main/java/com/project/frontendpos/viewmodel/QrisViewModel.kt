package com.project.frontendpos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.data.model.transaction.QrisResponse
import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.repository.TransactionRepository
import kotlinx.coroutines.launch

sealed interface QrisUiState {
    object Idle : QrisUiState
    object Loading : QrisUiState
    data class Success(
        val response: QrisResponse
    ) : QrisUiState
    data class Error(
        val message: String
    ) : QrisUiState
}

class QrisViewModel : ViewModel() {
    private val repository =
        TransactionRepository(
            RetrofitClient.transactionApi
        )

    private val _uiState =
        mutableStateOf<QrisUiState>(
            QrisUiState.Idle
        )
    val uiState: State<QrisUiState> = _uiState

    fun loadQr(
        transactionId: String
    ) {
        viewModelScope.launch {
            _uiState.value =
                QrisUiState.Loading
            repository
                .getTransactionQris(
                    SessionManager.formattedToken,
                    transactionId
                )
                .onSuccess {
                    _uiState.value =
                        QrisUiState.Success(it)
                }
                .onFailure {
                    _uiState.value =
                        QrisUiState.Error(
                            it.message ?: "Gagal memuat QRIS"
                        )
                }
        }
    }

    fun reset() {
        _uiState.value =
            QrisUiState.Idle

    }

}