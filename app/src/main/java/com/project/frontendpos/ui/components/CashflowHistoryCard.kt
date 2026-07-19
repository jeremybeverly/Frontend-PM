package com.project.frontendpos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.frontendpos.data.model.cashflow.CashflowResponse
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CashflowHistoryCard(
    cashflow: CashflowResponse
) {

    val rupiah =
        NumberFormat.getCurrencyInstance(
            Locale("in", "ID")
        )

    val isCashIn =
        cashflow.flowType == "cash_in"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector =
                    if (isCashIn)
                        Icons.Default.ArrowDownward
                    else
                        Icons.Default.ArrowUpward,

                contentDescription = null,

                tint =
                    if (isCashIn)
                        Color(0xFF2E7D32)
                    else
                        Color(0xFFC62828)
            )

            Spacer(
                Modifier.width(16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    if (isCashIn)
                        "Uang Tunai Masuk"
                    else
                        "Uang Tunai Keluar",
                    fontWeight = FontWeight.Bold
                )
                Text(cashflow.reason)

            }

            Text(
                rupiah.format(
                    cashflow.amount
                ),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}