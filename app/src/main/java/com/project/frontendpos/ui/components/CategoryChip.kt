package com.project.frontendpos.ui.components

import android.icu.text.CaseMap
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CategoryChip(
    title: String,
    selected: Boolean,
    onSelected: () -> Unit
){
    FilterChip(
        selected = selected,
        onClick = onSelected,
        label = {
            Text(title)
        }
    )
}