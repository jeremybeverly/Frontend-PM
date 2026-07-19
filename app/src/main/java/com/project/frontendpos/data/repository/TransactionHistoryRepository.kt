package com.project.frontendpos.data.repository

import com.project.frontendpos.data.remote.api.TransactionHistoryService

class TransactionHistoryRepository(
    private val api: TransactionHistoryService
) {
    suspend fun getTransactions(
        token: String
    ) = runCatching {
        api.getTransactions(token)
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