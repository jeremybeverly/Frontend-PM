package com.project.frontendpos.data.remote.api

import com.project.frontendpos.data.model.transaction.TransactionDetailResponse
import com.project.frontendpos.data.model.transaction.TransactionHistoryResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionHistoryService {

    @GET("api/transactions")
    suspend fun getTransactions(
        @Header("Authorization")
        token: String,
        @Query("invoice_number")
        invoiceNumber: String? = null

    ): TransactionHistoryResponse

    @GET("api/transactions/{id}")
    suspend fun getTransactionDetail(

        @Header("Authorization")
        token: String,

        @Path("id")
        id: String

    ): TransactionDetailResponse

}