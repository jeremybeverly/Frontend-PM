package com.project.frontendpos.data.model.modifier

import com.google.gson.annotations.SerializedName
import com.project.frontendpos.data.model.product.ProductCustomizationProduct

data class ProductCustomizationResponse(

    @SerializedName("product")
    val product: ProductCustomizationProduct,

    @SerializedName("modifierGroups")
    val modifierGroups: List<ModifierGroupResponse>
)