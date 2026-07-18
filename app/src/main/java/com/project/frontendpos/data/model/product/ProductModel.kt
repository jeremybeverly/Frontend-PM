package com.project.frontendpos.data.model.product

import com.google.gson.annotations.SerializedName
import com.project.frontendpos.data.remote.RetrofitClient
import kotlinx.serialization.Serializer

data class ModifierGroupInfo(
    val _id: String,
    val name: String? = null,
    val type: String? = null
)
data class IngredientInfo(
    val _id: String,
    val name: String? = null,
    val unit: String? = null
)
data class RecipeItem(
    val ingredient_id: IngredientInfo,
    val quantity_required: Double
)

data class ProductResponse(
    val _id: String,
    val product_name: String,
    val category: String,
    val price: Double,
    val image_url: String? = null,
    val recipe: List<RecipeItem>? = null,
    @SerializedName("modifier_groups")
    val modifier_groups: List<ModifierGroupInfo>,
    val is_available: Boolean,
    val available: Boolean,
    val in_stock: Boolean?,
    val is_deleted: Boolean
){
    val fullImageUrl: String?
        get() = if (!image_url.isNullOrEmpty()) {
            RetrofitClient.resolveImageUrl(image_url)
        } else null
}

data class ProductContainerResponse(
    val success: Boolean? = null,
    val data: List<ProductResponse>
)

data class ProductRequest(
    val name: String,
    val price: Double
)