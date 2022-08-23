package com.tolgaozgun.sprintplanning.viewmodels.lobby.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tolgaozgun.sprintplanning.util.ImageUtil
import com.tolgaozgun.sprintplanning.viewmodels.TransactionViewModel
import android.net.Uri

class ShareLobbyViewModel(
    private var context: Context,
    fragmentManager: FragmentManager
) : TransactionViewModel(fragmentManager = fragmentManager)  {

    fun copyCodeToClipboard(code: String){
        val clipboardManager = ContextCompat.getSystemService(
            context,
            ClipboardManager::class.java
        ) as ClipboardManager
        val clipData = ClipData.newPlainText("text", code)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_LONG).show()
    }

    fun shareQRCode(code: String){
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Join the lobby using code $code")
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(intent, "send"))
    }

    fun shareQRImage(code: String, image: Bitmap){
        val uri: Uri = ImageUtil.getImageUri(context, image, "Join my lobby!", "Join the lobby using code $code")
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Join the lobby using code $code")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = "image/jpeg"
        }
        context.startActivity(Intent.createChooser(intent, "send"))
    }

}