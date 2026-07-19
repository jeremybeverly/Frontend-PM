package com.project.frontendpos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.data.model.shift.ShiftResponse
import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.repository.ShiftRepository
import kotlinx.coroutines.launch

sealed interface ShiftUiState {
    object Idle : ShiftUiState
    object Loading : ShiftUiState
    object NoActiveShift : ShiftUiState
    data class Active(
        val shift: ShiftResponse
    ) : ShiftUiState
    data class Error(
        val message: String
    ) : ShiftUiState
}

class ShiftViewModel : ViewModel() {
    private val repository =
        ShiftRepository(
            RetrofitClient.shiftApi
        )

    private val _uiState =
        mutableStateOf<ShiftUiState>(
            ShiftUiState.Idle
        )

    val uiState: State<ShiftUiState> = _uiState
    init {
        loadShift()
    }

    fun loadShift() {
        viewModelScope.launch {
            _uiState.value = ShiftUiState.Loading
            repository.getActiveShift(
                SessionManager.formattedToken
            )
                .onSuccess { response ->
                    if (response.data == null) {
                        _uiState.value =
                            ShiftUiState.NoActiveShift
                    } else {

                        _uiState.value =
                            ShiftUiState.Active(
                                response.data
                            )
                    }
                }
                .onFailure {

                    _uiState.value =
                        ShiftUiState.Error(
                            it.message ?: "Unknown error"
                        )
                }
        }
    }

    fun startShift(
        startingCash: Double
    ) {
        viewModelScope.launch {
            _uiState.value = ShiftUiState.Loading
            repository.startShift(
                SessionManager.formattedToken,
                startingCash
            )
                .onSuccess {
                    loadShift()
                }
                .onFailure {

                    _uiState.value =
                        ShiftUiState.Error(
                            it.message ?: "Gagal Memulai Shift"
                        )
                }
        }
    }

    fun endShift(
        actualCash: Double
    ) {
        viewModelScope.launch {
            _uiState.value = ShiftUiState.Loading
            repository.endShift(
                SessionManager.formattedToken,
                actualCash
            )
                .onSuccess {

                    _uiState.value =
                        ShiftUiState.NoActiveShift
                }
                .onFailure {

                    _uiState.value =
                        ShiftUiState.Error(
                            it.message ?: "Gagal Mengakhiri Shift"
                        )
                }
        }
    }

    fun extendShift(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = ShiftUiState.Loading
            repository.extendShift(SessionManager.formattedToken)
                .onSuccess {
                    loadShift()
                    onSuccess()
                }
                .onFailure {
                    _uiState.value = ShiftUiState.Error(it.message ?: "Gagal memperpanjang shift")
                }
        }
    }

    fun refresh() {
        loadShift()
    }
}