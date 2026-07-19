package com.project.frontendpos.data.remote.api

import com.project.frontendpos.data.model.modifier.CustomizationApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ModifierService {

    @GET("api/mobile/products/{id}/customization")
    suspend fun getCustomization(

        @Header("Authorization")
        token: String,

        @Path("id")
        productId: String

    ): CustomizationApiResponse

}