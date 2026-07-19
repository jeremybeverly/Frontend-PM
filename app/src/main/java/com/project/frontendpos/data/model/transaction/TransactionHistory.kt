package com.project.frontendpos.data.model.transaction

import com.google.gson.annotations.SerializedName

data class TransactionHistoryResponse(
    val data: List<TransactionHistory>
)

data class TransactionHistory(
    @SerializedName("_id")
    val id: String,
    val invoice_number: String,
    val payment_method: String,
    val subtotal: Double,
    val tax_amount: Double,
    val total_amount: Double,
    val status: String,
    @SerializedName("created_at")
    val createdAt: String
)