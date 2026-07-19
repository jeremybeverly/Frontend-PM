package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.frontendpos.data.model.modifier.ModifierGroupResponse
import com.project.frontendpos.data.model.modifier.ModifierResponse

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModifierGroupSection(
    group: ModifierGroupResponse,
    selected: SnapshotStateMap<String, MutableList<ModifierResponse>>
) {
    Text(
        text = buildString {
            append(group.groupName)
            if (group.isRequired) {
                append(" *")
            }
        },
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
    )

    Spacer(modifier = Modifier.height(12.dp))

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        group.modifiers.forEach { modifier ->
            ModifierOptionItem(
                modifier = modifier,
                group = group,
                selected = selected
            )
        }
    }
}