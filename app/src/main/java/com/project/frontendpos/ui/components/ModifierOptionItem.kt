package com.project.frontendpos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    val currentSelection =
        selected[group.id] ?: mutableListOf()

    val checked =
        currentSelection.any {
            it.id == modifier.id
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                if (group.selectionType == "single") {

                    selected[group.id] =
                        mutableListOf(modifier)

                } else {

                    val list =
                        selected.getOrPut(group.id) {
                            mutableListOf()
                        }

                    if (checked) {

                        list.removeAll {
                            it.id == modifier.id
                        }

                    } else {

                        if (list.size < group.maxSelect) {
                            list.add(modifier)
                        }

                    }

                    selected[group.id] = list

                }

            }
            .padding(vertical = 6.dp),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (group.selectionType == "single") {

                RadioButton(
                    selected = checked,
                    onClick = null
                )

            } else {

                Checkbox(
                    checked = checked,
                    onCheckedChange = null
                )

            }

            Text(
                modifier.modifierName,
                style = MaterialTheme.typography.bodyLarge
            )

        }

        if (modifier.extraPrice > 0) {

            Text(
                text = NumberFormat
                    .getCurrencyInstance(Locale("in", "ID"))
                    .format(modifier.extraPrice),
                style = MaterialTheme.typography.bodyMedium
            )

        }

    }

}