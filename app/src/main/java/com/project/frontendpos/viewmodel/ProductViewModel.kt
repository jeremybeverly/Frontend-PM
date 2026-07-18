package com.project.frontendpos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.data.model.product.ProductResponse
import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed interface ProductUiState {
    object Loading : ProductUiState
    data class Success(val products: List<ProductResponse>) : ProductUiState
    data class Error(val message: String) : ProductUiState
}

class ProductViewModel : ViewModel() {
    private val repository =
        ProductRepository(RetrofitClient.productApi)
    private val _uiState =
        mutableStateOf<ProductUiState>(ProductUiState.Loading)
    val uiState: State<ProductUiState> = _uiState
    var selectedCategory = mutableStateOf("")
        private set
    var searchQuery = mutableStateOf("")
        private set
    private var searchJob: Job? = null

    init {
        fetchProducts()
    }

    fun onCategorySelected(category: String) {
        selectedCategory.value = category
        fetchProducts()
    }

    fun onSearchChanged(query: String) {
        searchQuery.value = query

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(300)
            fetchProducts()
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            val token = SessionManager.formattedToken
            repository.getProducts(
                token = token,
                category = selectedCategory.value.ifBlank { null },
                search = searchQuery.value.ifBlank { null }
            )
                .onSuccess {
                    _uiState.value =
                        ProductUiState.Success(it)

                }
                .onFailure {
                    _uiState.value =
                        ProductUiState.Error(
                            it.localizedMessage
                                ?: "Gagal memuat produk"
                        )
                }
        }
    }
}