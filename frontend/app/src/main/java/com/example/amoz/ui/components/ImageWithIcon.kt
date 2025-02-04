package com.example.amoz.ui.components

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.amoz.api.enums.ImagePlaceholder
import com.example.amoz.api.enums.getPlaceholder
import com.example.amoz.api.enums.getPlaceholderInt

@Composable
fun ImageWithIcon(
    image: Any? = null, // Accepts URI.toString() or ImageBitmap
    placeholder: ImagePlaceholder = ImagePlaceholder.HUMAN,
    contentDescription: String? = null,
    size: Dp = 160.dp,
    shape: RoundedCornerShape = CircleShape,
    iconImage: ImageVector = Icons.Default.Edit,
    iconContentDescription: String? = null,
    isEditing: Boolean = true,
    // in order to turn Uri to multipart, use Uri.toMultipartBodyPart(context) function
    onImagePicked: ((Uri?) -> Unit)? = null
) {
    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            onImagePicked?.invoke(uri)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer, shape)
    ) {
        when (image) {
            is String -> {
                // Display image from URI string
                val painter = rememberAsyncImagePainter(
                    model = image.takeIf { it.isNotEmpty() },
                    placeholder = painterResource(placeholder.getPlaceholderInt())
                )
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .size(size)
                        .clip(shape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, shape)
                        .clickable {
                            if (isEditing) {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                        },
                    contentScale = ContentScale.Crop
                )
            }
            is ImageBitmap -> {
                // Display ImageBitmap directly
                Image(
                    bitmap = image,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .size(size)
                        .clip(shape)
                        .clickable {
                            if (isEditing) {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                        },
                    contentScale = ContentScale.Crop
                )
            }
            else -> {
                // Fallback to placeholder if no image provided
                Image(
                    bitmap = placeholder.getPlaceholder(context),
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .size(size)
                        .clip(shape)
                        .clickable {
                            if (isEditing) {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }

        if (isEditing) {
            IconButton(
                onClick = { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .size(size * 0.3f),
            ) {
                Icon(
                    modifier = Modifier.size(size * 0.2f),
                    imageVector = iconImage,
                    contentDescription = iconContentDescription,
                )
            }
        }
    }
}

