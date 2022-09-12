package com.tolgaozgun.sprintplanning.util

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.tolgaozgun.sprintplanning.R
import com.tolgaozgun.sprintplanning.repository.UserRepository
import java.io.ByteArrayOutputStream
import java.net.URL

class ImageUtil {

    companion object{

        fun getDefaultBitmap(context: Context): Bitmap{
            val images: TypedArray = context.resources.obtainTypedArray(R.array.avatar_images)
            val choice: Int = (Math.random() * images.length()).toInt()
            val resource = images.getResourceId(choice, R.drawable.avatar_first)
            return BitmapFactory.decodeResource(context.resources, resource)
        }

        fun getImageUri(context: Context, bitmap: Bitmap, title: String, description: String): Uri {
            val byteArrayOS: ByteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS)
            val path: String = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, title, description)
            return Uri.parse(path)
        }
    }
}