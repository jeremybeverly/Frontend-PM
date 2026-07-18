package com.project.frontendpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginState {
    object Idle : LoginState
    object Loading : LoginState
    object Success : LoginState
    data class Error(val message: String) : LoginState
}

class LoginViewModel : ViewModel() {
    private val repository = AuthRepository(RetrofitClient.authApi)
    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState = _uiState.asStateFlow()

    fun login(username: String, password: String, onSuccess: () -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginState.Error("Username and password are required")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginState.Loading
            repository.login(username, password)
                .onSuccess {
                    _uiState.value = LoginState.Success
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = LoginState.Error(error.localizedMessage ?: "Login failed")
                }
        }
    }
}
