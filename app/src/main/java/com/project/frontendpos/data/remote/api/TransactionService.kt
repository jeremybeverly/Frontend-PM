package com.project.frontendpos.data.remote.api

import com.project.frontendpos.data.model.transaction.QrisResponse
import com.project.frontendpos.data.model.transaction.TransactionRequest
import com.project.frontendpos.data.model.transaction.TransactionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface TransactionService {
    @POST("api/transactions")
    suspend fun createTransaction(
        @Header("Authorization") token: String,
        @Body request: TransactionRequest
    ): TransactionResponse

    @GET("api/transactions/{id}/qris")
    suspend fun getTransactionQris(
        @Header("Authorization")
        token: String,
        @Path("id")
        transactionId: String
    ): QrisResponse
}