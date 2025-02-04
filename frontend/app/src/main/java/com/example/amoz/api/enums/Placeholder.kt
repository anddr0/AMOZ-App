package com.example.amoz.api.enums

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap


enum class ImagePlaceholder {
    HUMAN,
    PRODUCT,
    COMPANY
}

fun ImagePlaceholder.getPlaceholder(context: Context): ImageBitmap {
    val placeholderResId = when (this) {
        ImagePlaceholder.HUMAN -> com.example.amoz.R.drawable.human_placeholder
        ImagePlaceholder.PRODUCT -> com.example.amoz.R.drawable.product_placeholder
        ImagePlaceholder.COMPANY -> com.example.amoz.R.drawable.human_placeholder
    }
    return BitmapFactory.decodeResource(
        context.resources,
        placeholderResId
    ).asImageBitmap()
}

fun ImagePlaceholder.getPlaceholderInt(): Int {
    val placeholderResId = when (this) {
        ImagePlaceholder.HUMAN -> com.example.amoz.R.drawable.human_placeholder
        ImagePlaceholder.PRODUCT -> com.example.amoz.R.drawable.product_placeholder
        ImagePlaceholder.COMPANY -> com.example.amoz.R.drawable.human_placeholder
    }
    return placeholderResId
}