package com.project.frontendpos.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.frontendpos.viewmodel.QrisUiState
import com.project.frontendpos.viewmodel.QrisViewModel
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrPaymentScreen(
    transactionId: String,
    navController: NavController,
    qrisViewModel: QrisViewModel
) {
    val state by qrisViewModel.uiState
    val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    LaunchedEffect(transactionId) {
        qrisViewModel.loadQr(transactionId)
    }

    LaunchedEffect(state) {
        if (state is QrisUiState.Success) {
            val qrData = (state as QrisUiState.Success).response.data

            if (qrData.status.equals("success", ignoreCase = true)) {
                delay(1500)
                qrisViewModel.reset()
                navController.popBackStack()
            } else {
                qrisViewModel.loadQr(transactionId)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pembayaran QRIS", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val current = state) {
                QrisUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is QrisUiState.Success -> {
                    val qrString = current.response.data.qr_image
                    val pureBase64 = qrString.substringAfter(",")
                    val bytes = Base64.decode(pureBase64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Invoice: ${current.response.data.invoice_number}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(Modifier.height(8.dp))
                        
                        Text(
                            text = rupiah.format(current.response.data.total_amount),
                            style = MaterialTheme.typography.displaySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold
                        )
                        
                        Spacer(Modifier.height(32.dp))

                        Surface(
                            tonalElevation = 4.dp,
                            shadowElevation = 8.dp,
                            shape = MaterialTheme.shapes.extraLarge,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "QRIS Code",
                                modifier = Modifier
                                    .size(280.dp)
                                    .padding(20.dp)
                            )
                        }

                        Spacer(Modifier.height(40.dp))

                        Text(
                            text = "Tunjukkan Kode QR ke Pelanggan",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(Modifier.height(8.dp))
                        
                        LinearProgressIndicator(
                            modifier = Modifier.width(100.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                        
                        Spacer(Modifier.height(8.dp))
                        
                        Text(
                            text = "Menunggu pembayaran masuk...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                is QrisUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = current.message,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { qrisViewModel.loadQr(transactionId) }) {
                            Text("Coba Lagi")
                        }
                    }
                }
                else -> {}
            }
        }
    }
}
