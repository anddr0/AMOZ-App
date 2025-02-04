package com.example.amoz.ui.components
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import com.example.amoz.R
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.amoz.api.enums.ImagePlaceholder
import com.example.amoz.api.enums.getPlaceholderInt

@Composable
fun ImageWithText(
    image: String = "",
    placeholder: ImagePlaceholder = ImagePlaceholder.HUMAN,
    contentDescription: String? = null,
    size: Dp = 160.dp,
    shape: RoundedCornerShape = CircleShape,
    onImagePicked: (Uri?) -> Unit = {},
    onRemoveImage: () -> Unit = {}
) {
    val isImagePicked = remember { mutableStateOf(false) }
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            onImagePicked(uri)
            isImagePicked.value = true
            imageUri.value = uri
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    val painter = rememberAsyncImagePainter(
        model = imageUri.value ?: image,
        placeholder = painterResource(placeholder.getPlaceholderInt())
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(size)
            .clip(shape)
            .clickable {
                if (!isImagePicked.value) {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }
            .then(
                if (!isImagePicked.value) {
                    Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .border(2.dp, MaterialTheme.colorScheme.onSurface, shape)
                } else Modifier
            )
    ) {
        if (!isImagePicked.value) {
            Text(
                text = stringResource(id = R.string.add_image),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        } else {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape),
                contentScale = ContentScale.Crop
            )

            FloatingActionButton (
                onClick = {
                    isImagePicked.value = false
                    imageUri.value = null
                    onRemoveImage()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                content = { Icon(Icons.Default.Delete, "Delete image") },
            )
        }
    }
}
