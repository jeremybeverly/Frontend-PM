package com.project.frontendpos.data.model.modifier

import com.google.gson.annotations.SerializedName

data class ModifierResponse(
    @SerializedName("_id")
    val id: String,

    @SerializedName("group_id")
    val groupId: String,

    @SerializedName("modifier_name")
    val modifierName: String,

    @SerializedName("extra_price")
    val extraPrice: Double,

    @SerializedName("is_available")
    val isAvailable: Boolean
)

data class ModifierGroupResponse(
    @SerializedName("_id")
    val id: String,

    @SerializedName("group_name")
    val groupName: String,

    @SerializedName("selection_type")
    val selectionType: String,

    @SerializedName("is_required")
    val isRequired: Boolean,

    @SerializedName("max_select")
    val maxSelect: Int,

    @SerializedName("modifiers")
    val modifiers: List<ModifierResponse>
)