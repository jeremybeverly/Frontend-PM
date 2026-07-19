package com.project.frontendpos.data.repository

import com.project.frontendpos.data.local.SessionManager
import com.project.frontendpos.data.model.transaction.TransactionRequest
import com.project.frontendpos.data.remote.api.TransactionService

class TransactionRepository(
    private val api: TransactionService
) {
    suspend fun checkout(
        request: TransactionRequest
    ) = runCatching {
        api.createTransaction(
            SessionManager.formattedToken,
            request
        )
    }

    suspend fun getTransactionQris(
        token: String,
        transactionId: String
    ) = runCatching {
        api.getTransactionQris(
            token,
            transactionId
        )
    }
}