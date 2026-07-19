package com.project.frontendpos.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ShiftTimer(
    startTime: String
) {

    var timer by remember {
        mutableStateOf("00:00:00")
    }

    LaunchedEffect(startTime) {

        val formatter =
            DateTimeFormatter.ISO_DATE_TIME

        val started =
            LocalDateTime.parse(
                startTime,
                formatter
            )

        while (true) {

            val duration =
                Duration.between(
                    started,
                    LocalDateTime.now()
                )

            val h = duration.toHours()
            val m = duration.toMinutes() % 60
            val s = duration.seconds % 60

            timer =
                "%02d:%02d:%02d".format(
                    h,
                    m,
                    s
                )

            delay(1000)

        }

    }

    Text(
        text = timer,
        style = MaterialTheme.typography.displaySmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
    )

}