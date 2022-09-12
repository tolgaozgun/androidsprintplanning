package com.tolgaozgun.sprintplanning.data.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tolgaozgun.sprintplanning.util.FirebaseUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class UserRemoteDataSource(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val ioDispatcher: CoroutineDispatcher,
) {

    companion object {
        @Volatile
        private var INSTANCE: UserRemoteDataSource? = null

        fun getInstance(): UserRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            UserRemoteDataSource(FirebaseUtil().getFirestore(), FirebaseStorage.getInstance(), Dispatchers.IO)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    suspend fun retrieveAvatar(url: String): Bitmap? {
        var image: Bitmap? = null
        withContext(Dispatchers.IO){
            try{
                val url: URL = URL(url)
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            }catch (e: Exception){
                Log.d("LOAD_AVATAR", "Unable to load avatar: $e url: $url")
            }
        }
        return image
    }
}