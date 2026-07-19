package com.project.frontendpos.data.model.product

import com.google.gson.annotations.SerializedName

data class ProductCustomizationProduct(

    @SerializedName("_id")
    val id: String,

    @SerializedName("product_name")
    val productName: String,

    val category: String,

    val price: Double,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("is_available")
    val isAvailable: Boolean

)