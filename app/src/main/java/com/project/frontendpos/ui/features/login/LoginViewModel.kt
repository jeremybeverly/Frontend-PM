package com.project.frontendpos.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.model.LoginRequest

import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.remote.SessionManager
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
    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState = _uiState.asStateFlow()

    fun login(username: String, password: String, onSuccess: () -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginState.Error("Fields cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginState.Loading
            try {
                // Passes matching data payload mapping object
                val response = RetrofitClient.authApi.login(LoginRequest(username, password))
                SessionManager.userToken = response.token
                _uiState.value = LoginState.Success
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = LoginState.Error(e.localizedMessage ?: "Invalid login credentials")
            }
        }
    }
}