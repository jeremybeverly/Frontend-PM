package com.project.frontendpos.data.repository

import com.project.frontendpos.data.model.shift.EndShiftRequest
import com.project.frontendpos.data.model.shift.ShiftContainerResponse
import com.project.frontendpos.data.model.shift.StartShiftRequest
import com.project.frontendpos.data.remote.api.ShiftService

class ShiftRepository(private val api: ShiftService) {
    suspend fun getActiveShift(
        token: String
    ): Result<ShiftContainerResponse> {

        return runCatching {
            api.getActiveShift(token)
        }
    }

    suspend fun startShift(
        token: String,
        startingCash: Double
    ): Result<ShiftContainerResponse> {

        return runCatching {

            api.startShift(
                token,
                StartShiftRequest(startingCash)
            )
        }
    }

    suspend fun endShift(
        token: String,
        actualCash: Double
    ): Result<ShiftContainerResponse> {
        return runCatching {
            api.endShift(
                token,
                EndShiftRequest(actualCash)
            )
        }
    }
}