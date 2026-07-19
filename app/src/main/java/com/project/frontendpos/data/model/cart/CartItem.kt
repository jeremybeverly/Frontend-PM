package com.project.frontendpos.data.model.cart

import com.project.frontendpos.data.model.modifier.ModifierResponse
import com.project.frontendpos.data.model.product.ProductResponse

data class CartItem(
    val product: ProductResponse,
    val modifiers: List<ModifierResponse> = emptyList(),
    val note: String = "",
    var quantity: Int = 1
)