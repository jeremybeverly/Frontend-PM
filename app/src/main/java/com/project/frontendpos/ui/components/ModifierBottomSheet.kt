package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.frontendpos.data.model.modifier.ModifierGroupResponse
import com.project.frontendpos.data.model.modifier.ModifierResponse
import com.project.frontendpos.viewmodel.ModifierUiState
import com.project.frontendpos.viewmodel.ModifierViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifierBottomSheet(
    modifierViewModel: ModifierViewModel,
    onDismiss: () -> Unit,
    onAddToCart: (
        List<ModifierResponse>,
        String,
        Int // <-- Quantity parameter added
    ) -> Unit
) {
    val state = modifierViewModel.uiState.value

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val selectedModifiers = remember {
        mutableStateMapOf<String, MutableList<ModifierResponse>>()
    }

    var notes by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface // Ensures a clean background
    ) {
        when (state) {
            ModifierUiState.Idle -> {}
            ModifierUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                        .height(200.dp), // Prevent height jumping
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ModifierUiState.Error -> {
                Box(modifier = Modifier.padding(24.dp)) {
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
                    onNotesChange = { notes = it },
                    onAddToCart = { qty -> // <-- Receive qty from the child view
                        scope.launch {
                            sheetState.hide()
                            onAddToCart(
                                selectedModifiers.values.flatten(),
                                notes,
                                qty // <-- Pass qty to the parent screen
                            )
                        }
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
    onAddToCart: (qty: Int) -> Unit
) {

    val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
        maximumFractionDigits = 0
    }


    var quantity by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = state.data.product.productName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = rupiah.format(state.data.product.price),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(24.dp))

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
            label = { Text("Catatan Tambahan") },
            placeholder = { Text("Contoh: Less ice, extra sugar...") },
            minLines = 3,
            shape = RoundedCornerShape(12.dp)
        )

        val allRequiredSelected =
            state.data.modifierGroups
                .filter { it.isRequired }
                .all { group -> !selectedModifiers[group.id].isNullOrEmpty() }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (quantity > 1) quantity-- },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Kurang")
            }
            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            IconButton(
                onClick = { quantity++ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = allRequiredSelected,
            onClick = { onAddToCart(quantity) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1C355E)
            )
        ) {
            Text(
                text = "Tambah ke Keranjang",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (!allRequiredSelected) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "* Tolong lengkapi semua opsi di atas.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}