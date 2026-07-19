package com.project.frontendpos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.data.model.shift.ShiftSummaryResponse
import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.repository.ShiftRepository
import kotlinx.coroutines.launch

sealed interface ShiftSummaryUiState {
    object Idle : ShiftSummaryUiState
    object Loading : ShiftSummaryUiState
    data class Success(
        val response: ShiftSummaryResponse
    ) : ShiftSummaryUiState
    data class Error(
        val message: String
    ) : ShiftSummaryUiState
}

class ShiftSummaryViewModel : ViewModel() {
    private val repository =
        ShiftRepository(
            RetrofitClient.shiftApi
        )

    private val _uiState =
        mutableStateOf<ShiftSummaryUiState>(
            ShiftSummaryUiState.Idle
        )

    val uiState: State<ShiftSummaryUiState> = _uiState

    fun loadSummary() {
        viewModelScope.launch {
            _uiState.value =
                ShiftSummaryUiState.Loading
            repository
                .getShiftSummary(
                    SessionManager.formattedToken
                )
                .onSuccess {
                    _uiState.value =
                        ShiftSummaryUiState.Success(it)
                }
                .onFailure {
                    _uiState.value =
                        ShiftSummaryUiState.Error(
                            it.message ?: "Gagal memuat ringkasan shift"
                        )
                }
        }
    }
}