package net.vrgsoft

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable


fun Drawable.toBitmap(targetWidth: Int = 1, targetHeight: Int = 1): Bitmap {
    if (this is BitmapDrawable) {
        if (this.bitmap != null) {
            return this.bitmap
        }
    }
    val bitmap = if (this.intrinsicWidth <= 0 || this.intrinsicHeight <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        )
    } else {
        Bitmap.createBitmap(
            targetWidth,
            targetHeight,
            Bitmap.Config.ARGB_8888
        )
    }
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}