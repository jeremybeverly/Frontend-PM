package com.project.frontendpos.data.remote.api

import com.project.frontendpos.data.model.shift.EndShiftRequest
import com.project.frontendpos.data.model.shift.ShiftContainerResponse
import com.project.frontendpos.data.model.shift.StartShiftRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ShiftService {
    @GET("api/shift/")
    suspend fun getActiveShift(
        @Header("Authorization")
        token: String
    ): ShiftContainerResponse

    @POST("api/shift/start")
    suspend fun startShift(
        @Header("Authorization")
        token: String,
        @Body
        request: StartShiftRequest
    ): ShiftContainerResponse

    @POST("api/shift/end")
    suspend fun endShift(
        @Header("Authorization")
        token: String,
        @Body
        request: EndShiftRequest
    ): ShiftContainerResponse
}