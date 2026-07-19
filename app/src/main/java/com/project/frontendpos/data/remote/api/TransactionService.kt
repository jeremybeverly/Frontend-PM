package com.project.frontendpos.data.remote.api

import com.project.frontendpos.data.model.transaction.TransactionRequest
import com.project.frontendpos.data.model.transaction.TransactionResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TransactionService {
    @POST("api/transactions")
    suspend fun createTransaction(
        @Header("Authorization") token: String,
        @Body request: TransactionRequest
    ): TransactionResponse
}