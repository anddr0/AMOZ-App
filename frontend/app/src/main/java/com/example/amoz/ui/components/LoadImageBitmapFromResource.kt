package com.example.amoz.ui.components

import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

@Composable
fun loadImageBitmapFromResource(resId: Int): ImageBitmap {
    val context = LocalContext.current
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.resources, resId)
        ImageDecoder.decodeBitmap(source).asImageBitmap()
    } else {
        val drawable = context.resources.getDrawable(resId, context.theme)
        (drawable as BitmapDrawable).bitmap.asImageBitmap()
    }
}
