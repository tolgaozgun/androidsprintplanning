package com.tolgaozgun.sprintplanning.viewmodels.mainmenu

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.renderscript.ScriptGroup
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.repository.LobbyRepository
import com.tolgaozgun.sprintplanning.repository.UserRepository
import com.tolgaozgun.sprintplanning.util.LocalUtil
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

class ProfileViewModel(
    private val context: Context,
    private val lobbyRepository: LobbyRepository,
    private val userRepository: UserRepository,
    fragmentManager: FragmentManager
)   : TransactionViewModel(fragmentManager = fragmentManager) {

    var user: MutableLiveData<User> = MutableLiveData<User>()

    fun saveChanges(name: String?, avatar: String?): Boolean{
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("local_user", Context.MODE_PRIVATE)

        with(sharedPreferences.edit()){
            if(name != null){
                putString("name", name)
            }
            // TODO: Avatar
            apply()
        }

        viewModelScope.launch {
            lobbyRepository.addLocalUser(context)
        }

        return true
    }

    fun getUsername(context: Context): String{
        var user: User = LocalUtil.loadLocalUser(context)
        return user.name
    }

    fun loadUser() {
        viewModelScope.launch {
            user.postValue(LocalUtil.loadLocalUserWithAvatar(context, userRepository, true))
        }
    }

    private fun getMimeType(uri: Uri): String? {
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        Log.d("URL", "Bak: ${MimeTypeMap.getFileExtensionFromUrl(uri.toString())}")
        return context.contentResolver.getType(uri)
//        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
//        BitmapFactory.decodeStream(inputStream, null, options)
//        inputStream?.close()
//        return options.outMimeType
    }

    fun uploadImage(file: Uri){
        val progressDialog: ProgressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading file..")
        progressDialog.setCancelable(false)
        progressDialog.show()
        Log.d("URL", "Extension: ${getMimeType(file)}")
        viewModelScope.launch {
            val url: String? = lobbyRepository.uploadFile(context, file)
            when(url){
                is String -> {
                    progressDialog.dismiss()
                    Log.d("FILE_UPLOAD", "URL FINAL: $url")
                    LocalUtil.setAvatarUrl(context, url)
                    lobbyRepository.updateUser(context)
                }
                else -> {
                    Log.d("FILE_UPLOAD", "Fail else")
                    progressDialog.dismiss()
                }
            }
        }
    }



}