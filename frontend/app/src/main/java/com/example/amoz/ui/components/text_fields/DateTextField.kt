package com.example.amoz.ui.components.text_fields

import android.app.TimePickerDialog
import androidx.compose.material3.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import com.example.amoz.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTextField(
    modifier: Modifier = Modifier,
    label: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    date: Any?, // Can be LocalDate or LocalDateTime
    onDateChange: (Any?) -> Unit, // Callback to return the updated date
    showTime: Boolean = false, // Whether to display time alongside the date
    formatAsDateOnly: Boolean = false // Force formatting as date even for LocalDateTime
) {
    val formatter = when {
        formatAsDateOnly -> DateTimeFormatter.ofPattern("yyyy-MM-dd")
        showTime -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        else -> DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    var dateText by remember { mutableStateOf(date?.let { formatDate(it, formatter) } ?: "") }
    var isDatePickerVisible by remember { mutableStateOf(false) }
    var isTimePickerVisible by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val dateState = rememberDatePickerState()

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = dateText,
        onValueChange = { input ->
            try {
                val parsedDate = when {
                    showTime -> LocalDateTime.parse(input, formatter)
                    else -> LocalDate.parse(input, formatter)
                }
                dateText = input
                onDateChange(parsedDate)
            } catch (e: Exception) {
                dateText = input // Allow partial input without crashing
                onDateChange(null) // Notify parent that the input is invalid
            }
        },
        placeholder = { Text(if (showTime) "yyyy-MM-dd HH:mm" else "yyyy-MM-dd") },
        label = {
            Text(
                text = label,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                )
            }
        },
        trailingIcon = trailingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.clickable { isDatePickerVisible = true }
                )
            }
        },
        maxLines = 1,
        singleLine = true,
        readOnly = true,
    )

    if (isDatePickerVisible) {
        DatePickerDialog(
            onDismissRequest = { isDatePickerVisible = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = dateState.selectedDateMillis
                        selectedDate = selectedMillis?.let {
                            LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000))
                        }
                        if (showTime) {
                            isTimePickerVisible = true
                        } else {
                            dateText = selectedDate?.format(formatter) ?: ""
                            onDateChange(selectedDate ?: date)
                            isDatePickerVisible = false
                        }
                    }
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    if (isTimePickerVisible) {
        TimePickerDialog(
            LocalContext.current,
            { _, hour, minute ->
                selectedDate?.let {
                    val selectedDateTime = LocalDateTime.of(it, LocalTime.of(hour, minute))
                    dateText = selectedDateTime.format(formatter)
                    onDateChange(selectedDateTime ?: date)
                }
                isTimePickerVisible = false
                isDatePickerVisible = false
            },
            0,
            0,
            true
        ).show()
    }
}

private fun formatDate(date: Any, formatter: DateTimeFormatter): String {
    return when (date) {
        is LocalDate -> date.format(formatter)
        is LocalDateTime -> date.format(formatter)
        else -> throw IllegalArgumentException("Unsupported date type")
    }
}


