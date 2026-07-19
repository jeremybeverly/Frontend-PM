package com.project.frontendpos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.frontendpos.data.model.cart.CartItem
import com.project.frontendpos.data.model.transaction.TransactionItemRequest
import com.project.frontendpos.data.model.transaction.TransactionRequest
import com.project.frontendpos.data.remote.RetrofitClient
import com.project.frontendpos.data.repository.TransactionRepository
import kotlinx.coroutines.launch

sealed interface CheckoutUiState {
    object Idle : CheckoutUiState
    object Loading : CheckoutUiState
    object Success : CheckoutUiState
    data class Error(
        val message: String
    ) : CheckoutUiState
}

class CheckoutViewModel : ViewModel() {

    private val repository = TransactionRepository(
        RetrofitClient.transactionApi
    )

    private val _uiState =
        mutableStateOf<CheckoutUiState>(
            CheckoutUiState.Idle
        )

    val uiState: State<CheckoutUiState> = _uiState

    fun checkout(
        cartItems: List<CartItem>,
        paymentMethod: String
    ) {
        viewModelScope.launch {
            _uiState.value = CheckoutUiState.Loading
            val request = TransactionRequest(
                payment_method = paymentMethod,
                items = cartItems.map { item ->
                    TransactionItemRequest(
                        product_id = item.product._id,
                        quantity = item.quantity,
                        modifiers = item.modifiers.map {
                            it.id
                        },
                        note = item.note
                    )
                }
            )

            repository.checkout(request)
                .onSuccess {

                    _uiState.value =
                        CheckoutUiState.Success
                }
                .onFailure {
                    _uiState.value =
                        CheckoutUiState.Error(
                            it.message ?: "Checkout gagal"
                        )
                }
        }
    }
    fun resetState() {
        _uiState.value = CheckoutUiState.Idle
    }
}