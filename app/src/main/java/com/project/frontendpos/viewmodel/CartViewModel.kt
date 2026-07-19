package com.project.frontendpos.viewmodel

import androidx.lifecycle.ViewModel
import com.project.frontendpos.data.model.cart.CartItem
import com.project.frontendpos.data.model.modifier.ModifierResponse
import com.project.frontendpos.data.model.product.ProductResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CartState(
    val items: List<CartItem> = emptyList()
)

class CartViewModel: ViewModel() {
    private val _cart = MutableStateFlow(CartState())
    val cart = _cart.asStateFlow()

    fun addProduct(
        product: ProductResponse,
        modifiers: List<ModifierResponse>,
        note: String
    ) {
        val currentItems = _cart.value.items.toMutableList()
        val existing = currentItems.find {
            it.product._id == product._id &&
                    it.note == note &&
                    it.modifiers.map { m -> m.id }.sorted() ==
                    modifiers.map { m -> m.id }.sorted()
        }

        if (existing != null) {
            existing.quantity++
        } else {
            currentItems.add(
                CartItem(
                    product = product,
                    modifiers = modifiers,
                    note = note
                )
            )
        }
        _cart.value = CartState(currentItems)
    }

    fun getQuantity(product: ProductResponse): Int{
        return cart.value.items
            .find {
                it.product._id == product._id
            }
            ?.quantity ?: 0
    }

    fun increase(item: CartItem){
        item.quantity++
        _cart.value = _cart.value.copy()
    }

    fun decrease(item: CartItem){
        if(item.quantity > 1){
            item.quantity--
        } else {
            remove(item)
        }
        _cart.value = _cart.value.copy()
    }

    fun remove(item: CartItem){
        _cart.value = CartState(_cart.value.items.filter { it.product._id != item.product._id })
    }

    fun clear(){
        _cart.value = CartState()
    }

    val subtotal: Double
        get() = cart.value.items.sumOf { item ->
            val modifierTotal =
                item.modifiers.sumOf {
                    it.extraPrice
                }
            (item.product.price + modifierTotal) * item.quantity
        }
    val tax: Double
        get() = subtotal * 0.11
    val total: Double
        get() = subtotal + tax
}