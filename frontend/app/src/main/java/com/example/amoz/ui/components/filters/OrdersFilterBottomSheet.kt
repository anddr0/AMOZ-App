package com.example.amoz.ui.components.filters

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.api.enums.Status
import com.example.amoz.ui.components.CloseOutlinedButton
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.dropdown_menus.StatusDropdownMenu
import com.example.amoz.view_models.OrdersViewModel
import com.example.amoz.view_models.ProductsViewModel
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersFilterBottomSheet (
    onDismissRequest: () -> Unit,
    onCancelFilters: () -> Unit,
    filterParams: OrdersViewModel.OrderFilterParams,
    onApplyFilters: (OrdersViewModel.OrderFilterParams) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var filterParamsState by remember { mutableStateOf(filterParams) }


    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // -------------------- Sorting --------------------
            OrdersListSorting (
                sortingType = filterParamsState.sortingType,
                onSortingTypeChange = {
                    filterParamsState = filterParamsState.copy(sortingType = it)
                }
            )
            // -------------------- Status filter --------------------
            StatusDropdownMenu(
                selectedStatus = filterParamsState.status,
                onStatusChange = {
                    filterParamsState = filterParamsState.copy(status = it)
                },
                noStatusSelectedPossible = true,
            )
            // -------------------- Price filter --------------------
            PriceFilter(
                priceFrom = filterParamsState.priceFrom,
                priceTo = filterParamsState.priceTo,
                onPriceFromChange = {
                    filterParamsState = filterParamsState.copy(priceFrom = it)
                },
                onPriceToChange = {
                    filterParamsState = filterParamsState.copy(priceTo = it)
                },
            )

            // -------------------- Sending date filter --------------------

            DateFilter(
                dateFrom = filterParamsState.timeOfSendingFrom,
                dateTo = filterParamsState.timeOfSendingTo,
                onDateFromChange = {
                    Log.d("OrdersFilterBottomSheet", "onDateFromChange: $it")
                    filterParamsState = filterParamsState.copy(timeOfSendingFrom = it as? LocalDateTime)
                },
                onDateToChange = {
                    Log.d("OrdersFilterBottomSheet", "onDateToChange: $it")
                    filterParamsState = filterParamsState.copy(timeOfSendingTo = it as? LocalDateTime)
                },
                labelDateFrom = stringResource(id = R.string.sending_date_from),
                labelDateTo = stringResource(id = R.string.sending_date_to)

            )

            // -------------------- Creation date filter --------------------

            DateFilter(
                dateFrom = filterParamsState.timeOfCreationFrom,
                dateTo = filterParamsState.timeOfCreationTo,
                onDateFromChange = {
                    filterParamsState = filterParamsState.copy(timeOfCreationFrom = it as? LocalDateTime)
                },
                onDateToChange = {
                    filterParamsState = filterParamsState.copy(timeOfCreationTo = it as? LocalDateTime)
                },
                labelDateFrom = stringResource(id = R.string.creation_date_from),
                labelDateTo = stringResource(id = R.string.creation_date_to)
            )

            // -------------------- Cancel and apply --------------------
            CloseOutlinedButton(
                onClick = {
                    onCancelFilters()
                    onDismissRequest()
                },
                text = stringResource(id = R.string.clear_filters)
            )
            PrimaryFilledButton(
                onClick = {
                    onDismissRequest()
                    onApplyFilters(filterParamsState)
                },
                text = stringResource(id = R.string.apply_filters)
            )

        }
    }
}