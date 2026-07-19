package com.project.frontendpos.data.model.shift

import com.google.gson.annotations.SerializedName

data class ShiftResponse(
    @SerializedName("_id")
    val id: String,

    @SerializedName("cashier_id")
    val cashierId: String,

    @SerializedName("starting_cash")
    val startingCash: Double,

    @SerializedName("total_cash_sales")
    val totalCashSales: Double,

    @SerializedName("total_qris_sales")
    val totalQrisSales: Double,

    @SerializedName("total_cash_in")
    val totalCashIn: Double,

    @SerializedName("total_cash_out")
    val totalCashOut: Double,

    @SerializedName("expected_cash")
    val expectedCash: Double?,

    @SerializedName("actual_cash")
    val actualCash: Double?,
    val variance: Double?,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String?,
    val status: String
)

data class ShiftContainerResponse(
    val message: String,
    val data: ShiftResponse?
)

data class StartShiftRequest(
    @SerializedName("starting_cash")
    val startingCash: Double
)

data class EndShiftRequest(
    @SerializedName("actual_cash")
    val actualCash: Double

)