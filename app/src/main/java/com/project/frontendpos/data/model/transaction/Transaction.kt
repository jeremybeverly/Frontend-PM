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

data class TransactionDetailResponse(
    val data: TransactionDetailData
)

data class TransactionDetailData(
    val transaction: TransactionHistory,
    val details: List<TransactionItemDetail>
)

data class TransactionItemDetail(
    val product_name: String,
    val quantity: Int,
    val unit_price: Double,
    val subtotal: Double,
    val note: String?,
    val selected_modifiers: List<SelectedModifier>
)

data class SelectedModifier(
    val modifier_name: String,
    val extra_price: Double
)