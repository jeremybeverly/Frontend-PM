package com.project.frontendpos.data.model.cashflow

import com.google.gson.annotations.SerializedName

data class CashflowRequest(
    @SerializedName("flow_type")
    val flowType: String,

    val amount: Double,
    val reason: String
)

data class CashflowResponse(
    @SerializedName("_id")
    val id: String,

    @SerializedName("flow_type")
    val flowType: String,

    val amount: Double,
    val reason: String,

    @SerializedName("created_at")
    val createdAt: String
)

data class CashflowContainerResponse(
    val message: String,
    val data: List<CashflowResponse>
)

data class CashflowCreateResponse(
    val message: String,
    val data: CashflowResponse
)