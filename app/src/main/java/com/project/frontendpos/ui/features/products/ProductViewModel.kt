package com.project.frontendpos.ui.features.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.remote.ProductRequest
import com.project.frontendpos.data.remote.ProductResponse
import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.remote.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ProductState {
    object Loading: ProductState
    data class Success(val list: List<ProductResponse>): ProductState
    data class Error(val message: String): ProductState
}

class ProductViewModel: ViewModel() {
    private val _uiState = MutableStateFlow<ProductState>(ProductState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            _uiState.value = ProductState.Loading
            try {
                val data = RetrofitClient.apiService.getProducts(SessionManager.formattedToken)
                _uiState.value = ProductState.Success(data)
            } catch (e: Exception) {
                _uiState.value = ProductState.Error(e.localizedMessage ?: "Unknown Error")
            }
        }
    }

    fun addProduct(name: String, price: Double) {
        viewModelScope.launch {
            try {
                RetrofitClient.apiService.createProduct(
                    SessionManager.formattedToken,
                    ProductRequest(name, price)
                )
                fetchProducts() // Refresh the list automatically
            } catch (e: Exception) { /* Handle insertion error */ }
        }
    }

    fun deleteProduct(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.deleteProduct(SessionManager.formattedToken, id)
                if (response.isSuccessful) {
                    // Instantly filter out the deleted item from the UI state
                    val current = _uiState.value
                    if (current is ProductState.Success) {
                        _uiState.value = ProductState.Success(current.list.filter { it._id != id })
                    }
                }
            } catch (e: Exception) { /* Handle deletion error */ }
        }
    }
}