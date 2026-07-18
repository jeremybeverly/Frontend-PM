package com.project.frontendpos.data.model

data class RecipeItem(
    val ingredient_id: String,
    val quantity_required: Double
)

data class ProductResponse(
    val _id: String,
    val product_name: String,
    val category: String,
    val price: Double,
    val image_url: String? = null,
    val recipe: List<RecipeItem>? = null,
    val modifier_groups: List<String> = emptyList(),
    val is_available: Boolean,
    val is_deleted: Boolean
)

data class ProductRequest(
    val name: String,
    val price: Double
)