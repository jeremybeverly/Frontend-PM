package com.project.frontendpos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.frontendpos.ui.components.ActiveShiftContent
import com.project.frontendpos.ui.components.NoActiveShiftContent
import com.project.frontendpos.viewmodel.CashflowViewModel
import com.project.frontendpos.viewmodel.ShiftUiState
import com.project.frontendpos.viewmodel.ShiftViewModel
import com.project.frontendpos.viewmodel.ShiftSummaryViewModel

@Composable
fun ShiftScreen(
    shiftViewModel: ShiftViewModel = viewModel(),
    cashflowViewModel: CashflowViewModel = viewModel(),
    shiftSummaryViewModel: ShiftSummaryViewModel = viewModel(),
    onShiftStarted: () -> Unit = {}
) {
    val state = shiftViewModel.uiState.value
    val summaryState = shiftSummaryViewModel.uiState.value
    val cashflowState = cashflowViewModel.uiState.value

    val snackbarHostState = remember { SnackbarHostState() }
    var isStarting by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current


    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                shiftViewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(state) {
        if (isStarting && state is ShiftUiState.Active) {
            onShiftStarted()
            isStarting = false
        }
    }

    LaunchedEffect(state) {
        if (state is ShiftUiState.Active) {
            shiftSummaryViewModel.loadSummary()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            when (state) {
                ShiftUiState.Idle -> {}
                ShiftUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                ShiftUiState.NoActiveShift -> {
                    NoActiveShiftContent(
                        onStartShift = {
                            isStarting = true
                            shiftViewModel.startShift(it)
                        }
                    )
                }

                is ShiftUiState.Active -> {
                    ActiveShiftContent(
                        shift = state.shift,
                        summaryState = summaryState,
                        cashflowState = cashflowState,
                        onCashIn = { amount, reason ->
                            cashflowViewModel.addCashIn(amount, reason) {
                                shiftSummaryViewModel.loadSummary()
                            }
                        },
                        onCashOut = { amount, reason ->
                            cashflowViewModel.addCashOut(amount, reason) {
                                shiftSummaryViewModel.loadSummary()
                            }
                        },
                        onEndShift = {
                            shiftViewModel.endShift(it)
                        },
                        onExtendShift = {
                            shiftViewModel.extendShift {
                                shiftSummaryViewModel.loadSummary()
                            }
                        }
                    )
                }

                is ShiftUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.message)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { shiftViewModel.refresh() }) {
                            Text("Coba Lagi")
                        }
                    }
                }
            }
        }
    }
}