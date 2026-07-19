package com.project.frontendpos.data.remote.api

import com.project.frontendpos.data.model.cashflow.CashflowContainerResponse
import com.project.frontendpos.data.model.cashflow.CashflowCreateResponse
import com.project.frontendpos.data.model.cashflow.CashflowRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CashflowService {

    @GET("api/cashflow")
    suspend fun getCashflows(
        @Header("Authorization")
        token: String
    ): CashflowContainerResponse

    @POST("api/cashflow")
    suspend fun createCashflow(
        @Header("Authorization")
        token: String,

        @Body
        request: CashflowRequest
    ): CashflowCreateResponse

}