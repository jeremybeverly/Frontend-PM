package com.project.frontendpos.data.repository

import com.project.frontendpos.data.model.cashflow.CashflowRequest
import com.project.frontendpos.data.remote.api.CashflowService

class CashflowRepository(
    private val api: CashflowService
) {

    suspend fun getCashflows(
        token: String
    ) = runCatching {

        api.getCashflows(token)

    }

    suspend fun createCashflow(
        token: String,
        flowType: String,
        amount: Double,
        reason: String
    ) = runCatching {

        api.createCashflow(
            token,
            CashflowRequest(
                flowType = flowType,
                amount = amount,
                reason = reason
            )
        )

    }

}