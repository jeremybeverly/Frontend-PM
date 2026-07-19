package com.project.frontendpos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.frontendpos.ui.components.ActiveShiftContent
import com.project.frontendpos.ui.components.NoActiveShiftContent
import com.project.frontendpos.viewmodel.CashflowViewModel
import com.project.frontendpos.viewmodel.ShiftUiState
import com.project.frontendpos.viewmodel.ShiftViewModel

@Composable
fun ShiftScreen(
    shiftViewModel: ShiftViewModel = viewModel(),
    cashflowViewModel: CashflowViewModel = viewModel()
) {
    val state = shiftViewModel.uiState.value
    val cashflowState =
        cashflowViewModel.uiState.value
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            ShiftUiState.Idle -> {}
            ShiftUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }

            ShiftUiState.NoActiveShift -> {

                NoActiveShiftContent(
                    onStartShift = {
                        shiftViewModel.startShift(it)
                    }
                )
            }

            is ShiftUiState.Active -> {
                ActiveShiftContent(
                    shift = state.shift,
                    cashflowState = cashflowState,
                    onCashIn = { amount, reason ->
                        cashflowViewModel.addCashIn(
                            amount,
                            reason
                        )
                    },

                    onCashOut = { amount, reason ->
                        cashflowViewModel.addCashOut(
                            amount,
                            reason
                        )
                    },
                    onEndShift = {
                        shiftViewModel.endShift(it)
                    }
                )
            }

            is ShiftUiState.Error -> {

                Column(
                    modifier = Modifier
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(state.message)
                    Spacer(
                        Modifier.height(16.dp)
                    )

                    Button(
                        onClick = {
                            shiftViewModel.refresh()
                        }
                    ) {
                        Text("Coba Lagi")
                    }
                }
            }
        }
    }
}