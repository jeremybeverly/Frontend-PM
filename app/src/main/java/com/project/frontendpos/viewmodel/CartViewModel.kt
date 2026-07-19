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
        note: String,
        qty: Int = 1 // Add this quantity parameter
    ) {
        val currentItems = _cart.value.items.toMutableList()
        val index = currentItems.indexOfFirst {
            it.product._id == product._id &&
                    it.note == note &&
                    it.modifiers.map { m -> m.id }.sorted() ==
                    modifiers.map { m -> m.id }.sorted()
        }
        if (index != -1) {
            val existing = currentItems[index]
            currentItems[index] = existing.copy(
                quantity = existing.quantity + qty // Use custom qty
            )
        } else {
            currentItems.add(
                CartItem(
                    product = product,
                    modifiers = modifiers,
                    note = note,
                    quantity = qty // Use custom qty
                )
            )
        }
        _cart.value = CartState(currentItems)
    }

    fun getQuantity(product: ProductResponse): Int{
        return cart.value.items
            .filter {
                it.product._id == product._id
            }
            .sumOf {
                it.quantity
            }
    }

    fun increase(item: CartItem) {
        val updatedItems = _cart.value.items.map {
            if (
                it.product._id == item.product._id &&
                it.note == item.note &&
                it.modifiers.map { m -> m.id }.sorted() ==
                item.modifiers.map { m -> m.id }.sorted()
            ) {
                it.copy(
                    quantity = it.quantity + 1
                )
            } else {
                it
            }
        }
        _cart.value = CartState(updatedItems)
    }

    fun decrease(item: CartItem) {
        if (item.quantity == 1) {
            remove(item)
            return
        }
        val updatedItems = _cart.value.items.map {
            if (
                it.product._id == item.product._id &&
                it.note == item.note &&
                it.modifiers.map { m -> m.id }.sorted() ==
                item.modifiers.map { m -> m.id }.sorted()
            ) {
                it.copy(
                    quantity = it.quantity - 1
                )
            } else {
                it
            }
        }
        _cart.value = CartState(updatedItems)
    }

    fun remove(item: CartItem) {
        _cart.value = CartState(
            _cart.value.items.filterNot {
                it.product._id == item.product._id &&
                        it.note == item.note &&
                        it.modifiers.map { m -> m.id }.sorted() ==
                        item.modifiers.map { m -> m.id }.sorted()
            }
        )
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
