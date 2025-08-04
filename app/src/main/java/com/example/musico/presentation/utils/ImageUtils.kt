package com.example.musico.presentation.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap

@Composable
fun getDefaultAlbumArtBitmap(@DrawableRes resId: Int): Bitmap {
    val context = LocalContext.current
    val drawable = ContextCompat.getDrawable(context, resId)!!

    val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)

    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}
