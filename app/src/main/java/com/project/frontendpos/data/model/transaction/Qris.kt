package com.project.frontendpos.data.model.transaction

data class QrisResponse(
    val message: String,
    val data: QrisData
)

data class QrisData(
    val invoice_number: String,
    val total_amount: Double,
    val status: String,
    val pay_url: String,
    val qr_image: String
)