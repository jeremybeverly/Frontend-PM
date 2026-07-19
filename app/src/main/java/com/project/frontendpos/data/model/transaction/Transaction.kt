package com.project.frontendpos.data.model.transaction

data class TransactionItemRequest(
    val product_id: String,
    val quantity: Int,
    val modifiers: List<String>,
    val note: String
)

data class TransactionRequest(
    val payment_method: String,
    val items: List<TransactionItemRequest>
)

data class TransactionResponse(
    val message: String
)