package com.apadmi.mockzilla.lib.internal.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

import com.apadmi.mockzilla.lib.internal.PlatformConfig

import java.io.ByteArrayOutputStream

private const val fallbackIconSize: Int = 96

private fun Drawable.toBitmap(fallbackSize: Int = fallbackIconSize): Bitmap {
    if (this is BitmapDrawable) {
        return this.bitmap
    }

    val bitmap = Bitmap.createBitmap(
        intrinsicWidth.takeIf { it > 0 } ?: fallbackSize,
        intrinsicHeight.takeIf { it > 0 } ?: fallbackSize,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}
internal actual fun fetchAppIconBytes(platformConfig: PlatformConfig): ByteArray? {
    val context = platformConfig.context ?: return null
    val drawable = context.packageManager.getApplicationIcon(context.packageName)
    val stream = ByteArrayOutputStream()
    drawable.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
