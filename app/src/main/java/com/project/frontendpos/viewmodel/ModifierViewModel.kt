package com.project.frontendpos.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.data.model.modifier.ProductCustomizationResponse
import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.repository.ModifierRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed interface ModifierUiState {

    object Idle : ModifierUiState

    object Loading : ModifierUiState

    data class Success(
        val data: ProductCustomizationResponse
    ) : ModifierUiState

    data class Error(
        val message: String
    ) : ModifierUiState

}

class ModifierViewModel : ViewModel() {

    private val repository =
        ModifierRepository(
            RetrofitClient.modifierApi
        )

    private val _uiState =
        mutableStateOf<ModifierUiState>(
            ModifierUiState.Idle
        )

    val uiState: State<ModifierUiState> =
        _uiState
    private var customizationJob: Job? = null

    fun loadCustomization(
        productId: String
    ) {

        customizationJob?.cancel()
        customizationJob = viewModelScope.launch {

            _uiState.value =
                ModifierUiState.Loading

            val result = repository.getCustomization(
                SessionManager.formattedToken,
                productId
            )

            if (!isActive) return@launch

            result
                .onSuccess {

                    _uiState.value =
                        ModifierUiState.Success(it)

                }
                .onFailure {

                    _uiState.value =
                        ModifierUiState.Error(
                            it.message ?: "Unknown error"
                        )

                }

        }

    }

    fun reset() {
        customizationJob?.cancel()
        customizationJob = null
        _uiState.value = ModifierUiState.Idle
    }

}
