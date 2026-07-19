package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.frontendpos.data.model.modifier.ModifierGroupResponse
import com.project.frontendpos.data.model.modifier.ModifierResponse
import com.project.frontendpos.viewmodel.ModifierUiState
import com.project.frontendpos.viewmodel.ModifierViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifierBottomSheet(
    modifierViewModel: ModifierViewModel,
    onDismiss: () -> Unit,
    onAddToCart: (
        List<ModifierResponse>,
        String
    ) -> Unit
) {

    val state = modifierViewModel.uiState.value

    val selectedModifiers = remember {
        mutableStateMapOf<String, MutableList<ModifierResponse>>()
    }


    var notes by remember {
        mutableStateOf("")
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {

        when (state) {

            ModifierUiState.Idle -> {}

            ModifierUiState.Loading -> {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

            }

            is ModifierUiState.Error -> {

                Box(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )

                }

            }

            is ModifierUiState.Success -> {

                CustomizationContent(
                    state = state,
                    selectedModifiers = selectedModifiers,
                    notes = notes,
                    onNotesChange = {
                        notes = it
                    },
                    onAddToCart = {

                        onAddToCart(
                            selectedModifiers
                                .values
                                .flatten(),
                            notes
                        )

                    }
                )

            }
        }
    }
}

@Composable
private fun CustomizationContent(
    state: ModifierUiState.Success,
    selectedModifiers: SnapshotStateMap<String, MutableList<ModifierResponse>>,
    notes: String,
    onNotesChange: (String) -> Unit,
    onAddToCart: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = state.data.product.product_name,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Rp ${state.data.product.price}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        state.data.modifierGroups.forEach { group ->

            ModifierGroupSection(
                group = group,
                selected = selectedModifiers
            )

            Spacer(modifier = Modifier.height(16.dp))

        }

        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Notes")
            },
            minLines = 3
        )

        val allRequiredSelected =
            state.data.modifierGroups
                .filter { it.isRequired }
                .all { group ->
                    !selectedModifiers[group.id].isNullOrEmpty()
                }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = allRequiredSelected,
            onClick = onAddToCart
        ) {

            Text("Tambah ke Keranjang")
        }

        if (!allRequiredSelected) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tolong lengkapi semua opsi.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}