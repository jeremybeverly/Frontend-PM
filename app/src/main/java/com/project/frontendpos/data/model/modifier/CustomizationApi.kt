package com.project.frontendpos.data.model.modifier

import com.google.gson.annotations.SerializedName

data class CustomizationApiResponse(

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: ProductCustomizationResponse
)