package com.example.amoz.ui.components.person

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.components.loadImageBitmapFromResource
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun PersonImage(
    imageState: MutableStateFlow<ResultState<ImageBitmap?>>?
) {
    imageState?.let {
        ResultStateView(
            state = imageState,
            modifier = Modifier.size(56.dp),
            successView = { image ->
                Image(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    bitmap = image ?: loadImageBitmapFromResource(R.drawable.human_placeholder),
                    contentDescription = null
                )
            },
            failureView = {
                Image(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    bitmap = loadImageBitmapFromResource(R.drawable.human_placeholder),
                    contentDescription = null
                )
            }
        )
    }
}