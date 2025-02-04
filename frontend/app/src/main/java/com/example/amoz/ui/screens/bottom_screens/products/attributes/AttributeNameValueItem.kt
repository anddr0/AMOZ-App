package com.example.amoz.ui.screens.bottom_screens.products.attributes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amoz.R
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.components.dissmiss_backgrounds.DismissBackground
import com.example.amoz.ui.components.text_fields.AutoCompleteTextField
import com.example.amoz.view_models.ProductsViewModel

@Composable
fun AttributeNameValueItem(
    viewModel: ProductsViewModel,
    indexInList: Int,
    attributeName: String,
    attributeValue: String?,
    readOnly: Boolean = false,
    onDelete: (Int) -> Unit,
    onNameChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    positionalThreshold: Float = .45f
) {
    val defaultAttributeNameText = stringResource(R.string.product_attributes_enter_name)
    val defaultAttributeValueText = stringResource(R.string.product_attributes_choose_value)

    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainerLow,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainerLow,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
    )

    var currentFraction by remember { mutableStateOf(0f) }
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { newValue ->
            when (newValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    if (currentFraction >= positionalThreshold && currentFraction < 1.0f) {
                        onDelete(indexInList)
                        return@rememberSwipeToDismissBoxState false
                    }
                    return@rememberSwipeToDismissBoxState false
                }
                else -> false
            }
        },
        positionalThreshold = { it * positionalThreshold }
    )

    ResultStateView(viewModel.productUiState.collectAsState().value.allAttributesList) { attributes ->
        SwipeToDismissBox(
            state = swipeState,
            enableDismissFromEndToStart = false,
            backgroundContent = {
                currentFraction = swipeState.progress
                DismissBackground(swipeState)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 1.dp,
                        brush = SolidColor(MaterialTheme.colorScheme.outline),
                        shape = RoundedCornerShape(10.dp)
                    ),
            ) {
                // -------------------- Product attribute name --------------------
                AutoCompleteTextField(
                    completionList = attributes.map { it.attributeName },
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = attributeName,
                    onValueChange = { onNameChange(it) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.CheckBoxOutlineBlank,
                            contentDescription = null
                        )
                    },
//                trailingIcon = {
//                    Icon(
//                        imageVector = Icons.Outlined.Edit,
//                        contentDescription = null
//                    )
//                },
                    maxLines = 1,
                    singleLine = true,
                    colors = textFieldColors
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                // -------------------- Product attribute value --------------------
                attributeValue?.let {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = { Text(defaultAttributeValueText) },
                        value = attributeValue,
                        onValueChange = { onValueChange(it) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.CheckBox,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null
                            )
                        },
                        maxLines = 1,
                        readOnly = readOnly,
                        singleLine = true,
                        colors = textFieldColors
                    )
                }
            }
        }
    }
}
