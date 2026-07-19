package com.project.frontendpos.data.repository

import com.project.frontendpos.data.remote.api.TransactionHistoryService

class TransactionHistoryRepository(
    private val api: TransactionHistoryService
) {
    suspend fun getTransactions(
        token: String,
        invoiceNumber: String? = null
    ) = runCatching {
        api.getTransactions(
            token = token,
            invoiceNumber = invoiceNumber
        )
    }

    suspend fun getTransactionDetail(
        token: String,
        id: String
    ) = runCatching {
        api.getTransactionDetail(
            token,
            id
        )

    }
}