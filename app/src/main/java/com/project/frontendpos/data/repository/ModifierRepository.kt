package com.project.frontendpos.data.repository

import com.project.frontendpos.data.model.modifier.ProductCustomizationResponse
import com.project.frontendpos.data.remote.api.ModifierService

class ModifierRepository(
    private val api: ModifierService
) {

    suspend fun getCustomization(
        token: String,
        productId: String
    ): Result<ProductCustomizationResponse> {

        return runCatching {

            api.getCustomization(
                token,
                productId
            ).data

        }

    }

}