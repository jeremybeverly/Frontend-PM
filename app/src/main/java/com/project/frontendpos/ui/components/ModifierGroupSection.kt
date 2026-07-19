package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.frontendpos.data.model.modifier.ModifierGroupResponse
import com.project.frontendpos.data.model.modifier.ModifierResponse

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

            append(
                if (group.selectionType == "single")
                    " (Pilih Satu)"
                else
                    " (Max ${group.maxSelect})"
            )
        },
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(modifier = Modifier.height(8.dp))

    Column {
        group.modifiers.forEach { modifier ->
            ModifierOptionItem(
                modifier = modifier,
                group = group,
                selected = selected
            )
        }
    }
}