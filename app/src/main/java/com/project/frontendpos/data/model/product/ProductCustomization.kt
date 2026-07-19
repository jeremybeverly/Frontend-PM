package com.project.frontendpos.data.model.modifier

import com.google.gson.annotations.SerializedName
import com.project.frontendpos.data.model.product.ProductResponse

data class ProductCustomizationResponse(

    @SerializedName("product")
    val product: ProductResponse,

    @SerializedName("modifierGroups")
    val modifierGroups: List<ModifierGroupResponse>

)