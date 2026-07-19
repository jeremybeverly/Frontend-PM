package com.project.frontendpos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.project.frontendpos.data.model.modifier.ModifierGroupResponse
import com.project.frontendpos.data.model.modifier.ModifierResponse
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ModifierOptionItem(
    modifier: ModifierResponse,
    group: ModifierGroupResponse,
    selected: SnapshotStateMap<String, MutableList<ModifierResponse>>
) {
    val currentSelection = selected[group.id] ?: mutableListOf()
    val checked = currentSelection.any { it.id == modifier.id }

    val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
        maximumFractionDigits = 0
    }

    Row(
        modifier = Modifier
            .clickable {
                if (group.selectionType == "single") {
                    selected[group.id] = mutableListOf(modifier)
                } else {
                    val list = selected[group.id]?.toMutableList() ?: mutableListOf()
                    if (checked) {
                        list.removeAll { it.id == modifier.id }
                    } else {
                        if (list.size < group.maxSelect) {
                            list.add(modifier)
                        }
                    }
                    selected[group.id] = list
                }
            }
            .padding(vertical = 4.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (group.selectionType == "single") {
            RadioButton(
                selected = checked,
                onClick = null,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Checkbox(
                checked = checked,
                onCheckedChange = null,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = modifier.modifierName,
            style = MaterialTheme.typography.bodyLarge
        )

        if (modifier.extraPrice > 0) {
            Text(
                text = " (+${rupiah.format(modifier.extraPrice)})",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}