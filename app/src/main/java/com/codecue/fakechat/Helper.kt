package com.codecue.fakechat

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


object DbBitmapUtility {

    // convert from bitmap to byte array
    fun getBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        if (bitmap.byteCount > 1048576) {   // 1 MB
            bitmap.compress(CompressFormat.PNG, 50, stream)
        } else {
            bitmap.compress(CompressFormat.PNG, 100, stream)
        }

        return stream.toByteArray()
    }

    // convert from byte array to bitmap
    fun getImage(image: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }
}