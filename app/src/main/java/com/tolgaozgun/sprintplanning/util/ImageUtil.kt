package com.tolgaozgun.sprintplanning.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

class ImageUtil {

    companion object{
        fun getImageUri(context: Context, bitmap: Bitmap, title: String, description: String): Uri {
            val byteArrayOS: ByteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS)
            val path: String = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, title, description)
            return Uri.parse(path)
        }
    }
}