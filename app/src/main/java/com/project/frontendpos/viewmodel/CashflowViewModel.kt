package com.project.frontendpos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.data.model.cashflow.CashflowResponse
import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.repository.CashflowRepository
import kotlinx.coroutines.launch

sealed interface CashflowUiState {
    object Idle : CashflowUiState
    object Loading : CashflowUiState
    data class Success(val cashflows: List<CashflowResponse>) : CashflowUiState
    data class Error(val message: String) : CashflowUiState
}

class CashflowViewModel : ViewModel() {
    private val repository = CashflowRepository(RetrofitClient.cashflowApi)

    private val _uiState = mutableStateOf<CashflowUiState>(CashflowUiState.Idle)
    val uiState: State<CashflowUiState> = _uiState

    init {
        loadCashflows()
    }

    fun loadCashflows() {
        viewModelScope.launch {
            _uiState.value = CashflowUiState.Loading
            repository.getCashflows(SessionManager.formattedToken)
                .onSuccess { response ->
                    _uiState.value = CashflowUiState.Success(response.data)
                }
                .onFailure {
                    _uiState.value = CashflowUiState.Error(it.message ?: "Failed to load cashflows")
                }
        }
    }

    fun addCashIn(amount: Double, reason: String, onSuccess: () -> Unit = {}) {
        createCashflow("cash_in", amount, reason, onSuccess)
    }

    fun addCashOut(amount: Double, reason: String, onSuccess: () -> Unit = {}) {
        createCashflow("cash_out", amount, reason, onSuccess)
    }

    private fun createCashflow(
        flowType: String,
        amount: Double,
        reason: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.createCashflow(SessionManager.formattedToken, flowType, amount, reason)
                .onSuccess {
                    onSuccess()
                }
                .onFailure {
                    _uiState.value = CashflowUiState.Error(it.message ?: "Failed to save cashflow")
                }
        }
    }

    fun refresh() {
        loadCashflows()
    }
}