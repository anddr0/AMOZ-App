package com.example.amoz.extensions

import android.net.Uri
import java.io.InputStream
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.UUID


fun Uri.toMultipartBodyPart(context: Context, partName: String = "file"): MultipartBody.Part {
    val inputStream: InputStream? = context.contentResolver.openInputStream(this)
    val tempFile = File(context.cacheDir, UUID.randomUUID().toString())

    inputStream?.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }

    val requestFile = tempFile
        .asRequestBody(context.contentResolver.getType(this)?.toMediaTypeOrNull())

    return MultipartBody.Part.createFormData(partName, tempFile.name, requestFile)
}

fun Uri.toImageBitmap(context: Context): ImageBitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            ImageDecoder.decodeBitmap(source).asImageBitmap()
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, this).asImageBitmap()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


