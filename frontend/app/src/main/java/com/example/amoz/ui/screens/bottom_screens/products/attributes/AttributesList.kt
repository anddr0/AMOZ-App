package com.example.amoz.ui.screens.bottom_screens.products.attributes

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.amoz.R
import com.example.amoz.api.requests.AttributeCreateRequest
import com.example.amoz.ui.components.EmptyLayout
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.PrimaryOutlinedButton
import com.example.amoz.view_models.ProductsViewModel


@Composable
fun ProductAttributes(
    viewModel: ProductsViewModel,
    productAttributes: List<AttributeCreateRequest>,
    onAttributesChange: (List<AttributeCreateRequest>) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchAllAttributes()
    }

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, SolidColor(MaterialTheme.colorScheme.outline), RoundedCornerShape(10.dp))
            .clickable { showDialog = true },
        leadingContent = {
            Icon(Icons.Outlined.CheckBox, contentDescription = null)
        },
        headlineContent = {
            Text(stringResource(R.string.product_attributes))
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )

    if (showDialog) {
        AttributesDialog(
            viewModel = viewModel,
            productAttributes = productAttributes,
            onAttributesChange = onAttributesChange,
            onDismissRequest = {showDialog = false}
        )
    }
}

@Composable
fun AttributesDialog(
    viewModel: ProductsViewModel,
    productAttributes: List<AttributeCreateRequest>,
    onAttributesChange: (List<AttributeCreateRequest>) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.product_attributes),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = { onDismissRequest() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close dialog"
                        )
                    }
                }

                AttributesList(
                    viewModel = viewModel,
                    productAttributes = productAttributes,
                    onAttributesChange = onAttributesChange,
                    onDismissRequest = onDismissRequest
                )
            }
        }
    }
}


@Composable
fun AttributesList(
    viewModel: ProductsViewModel,
    productAttributes: List<AttributeCreateRequest>,
    onAttributesChange: (List<AttributeCreateRequest>) -> Unit,
    onDismissRequest: () -> Unit
) {
    var productAttributesState by remember { mutableStateOf(productAttributes) }

    // -------------------- Product attributes --------------------
    productAttributesState.forEachIndexed { index, productAttribute ->
        AttributeNameValueItem(
            viewModel = viewModel,
            indexInList = index,
            attributeName = productAttribute.attributeName,
            attributeValue = productAttribute.value,
            onDelete = { indexToDelete ->
                productAttributesState = productAttributesState.toMutableList().apply {
                    removeAt(indexToDelete)
                }
                onAttributesChange(productAttributesState)
            },
            onNameChange = { newName ->
                productAttributesState = productAttributesState.toMutableList().apply {
                    set(
                        index, productAttribute.copy(attributeName = newName)
                    )
                }
                onAttributesChange(productAttributesState)
            },
            onValueChange = { newValue ->
                productAttributesState = productAttributesState.toMutableList().apply {
                    set(
                        index, productAttribute.copy(value = newValue)
                    )
                }
                onAttributesChange(productAttributesState)
            }
        )

    }
    if (productAttributesState.isEmpty()) {
        EmptyLayout { }
    }

    // -------------------- Add a feature btns --------------------
    PrimaryOutlinedButton(
        onClick = {
            productAttributesState = productAttributesState.toMutableList().apply {
                add(
                    AttributeCreateRequest(
                        attributeName = "",
                        value = null
                    )
                )
            }
            onAttributesChange(productAttributesState)
        },
        text = stringResource(id = R.string.product_attributes_add_attributes_name),
        leadingIcon = Icons.Outlined.Add
    )
    PrimaryOutlinedButton(
        onClick = {
            productAttributesState = productAttributesState.toMutableList().apply {
                add(
                    AttributeCreateRequest(
                        attributeName = "",
                        value = ""
                    )
                )
            }
            onAttributesChange(productAttributesState)
        },
        text = stringResource(id = R.string.product_attributes_add_attributes_name_value),
        leadingIcon = Icons.Outlined.Add
    )
    // -------------------- Done btn --------------------
    PrimaryFilledButton(onClick = onDismissRequest, text = stringResource(R.string.save))
}