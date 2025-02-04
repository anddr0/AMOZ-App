package com.example.amoz.ui.components.filters

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.amoz.ui.components.text_fields.DateTextField
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun DateFilter(
    dateFrom: Any?, // Can be LocalDate or LocalDateTime
    dateTo: Any?, // Can be LocalDate or LocalDateTime
    labelDateFrom: String = "",
    labelDateTo: String = "",
    onDateFromChange: (Any?) -> Unit, // Callback to handle LocalDate or LocalDateTime
    onDateToChange: (Any?) -> Unit, // Callback to handle LocalDate or LocalDateTime
    showTime: Boolean = false, // Whether to display time alongside the date
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        DateTextField(
            modifier = Modifier.weight(1f),
            label = labelDateFrom,
            date = dateFrom,
            onDateChange = {
                val updatedDate = when (it) {
                    is LocalDate -> LocalDateTime.of(it, LocalTime.MIDNIGHT)
                    is LocalDateTime -> it
                    else -> null
                }
                onDateFromChange(updatedDate)
            },
            trailingIcon = Icons.Default.DateRange,
            showTime = showTime,
            formatAsDateOnly = !showTime,
        )
        Spacer(modifier = Modifier.width(10.dp))
        DateTextField(
            modifier = Modifier.weight(1f),
            label = labelDateTo,
            date = dateTo,
            onDateChange = {
                val updatedDate = when (it) {
                    is LocalDate -> LocalDateTime.of(it, LocalTime.MIDNIGHT)
                    is LocalDateTime -> it
                    else -> null
                }
                onDateToChange(updatedDate)
            },
            trailingIcon = Icons.Default.DateRange,
            showTime = showTime,
            formatAsDateOnly = !showTime,
        )
    }
}