package com.project.frontendpos.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.data.model.modifier.ProductCustomizationResponse
import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.repository.ModifierRepository
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

    fun loadCustomization(
        productId: String
    ) {

        viewModelScope.launch {

            _uiState.value =
                ModifierUiState.Loading

            repository.getCustomization(
                SessionManager.formattedToken,
                productId
            )
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

}