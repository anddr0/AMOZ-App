package com.example.amoz.ui.screens.bottom_screens.orders.orders_list

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.enums.Status
import java.math.BigDecimal
import java.time.LocalDateTime

@Composable
fun FilterChipsOrders(
    modifier: Modifier = Modifier,
    status: Status?,
    onStatusClick: () -> Unit,
    priceFrom: BigDecimal?,
    onPriceFromClick: () -> Unit,
    priceTo: BigDecimal?,
    onPriceToClick: () -> Unit,
    timeOfSendingFrom: LocalDateTime?,
    onTimeOfSendingFromClick: () -> Unit,
    timeOfSendingTo: LocalDateTime?,
    onTimeOfSendingToClick: () -> Unit,
    timeOfCreationFrom: LocalDateTime?,
    onTimeOfCreationFromClick: () -> Unit,
    timeOfCreationTo: LocalDateTime?,
    onTimeOfCreationToClick: () -> Unit,
) {
    Row(
        modifier = modifier.then(
            Modifier.horizontalScroll(rememberScrollState())
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        status?.let {
            FilterChip(
                onClick = onStatusClick,
                label = { Text("Status: ${it.getName()}") },
                selected = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }
        priceFrom?.let {
            FilterChip(
                onClick = onPriceFromClick,
                label = { Text(stringResource(id = R.string.price_from) + ": $priceFrom") },
                selected = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }
        priceTo?.let {
            FilterChip(
                onClick = onPriceToClick,
                label = { Text(stringResource(id = R.string.price_to) + ": $priceTo") },
                selected = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }
        timeOfSendingFrom?.let {
            FilterChip(
                onClick = onTimeOfSendingFromClick,
                label = { Text(stringResource(id = R.string.orders_sent_from) + ": $timeOfSendingFrom") },
                selected = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }
        timeOfSendingTo?.let {
            FilterChip(
                onClick = onTimeOfSendingToClick,
                label = { Text(stringResource(id = R.string.orders_sent_to) + ": $timeOfSendingTo") },
                selected = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }
        timeOfCreationFrom?.let {
            FilterChip(
                onClick = onTimeOfCreationFromClick,
                label = { Text(stringResource(id = R.string.orders_created_from) + ": $timeOfCreationFrom") },
                selected = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }
        timeOfCreationTo?.let {
            FilterChip(
                onClick = onTimeOfCreationToClick,
                label = { Text(stringResource(id = R.string.orders_created_to) + ": $timeOfCreationTo") },
                selected = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }
    }
}