package com.codecue.fakechat

import android.content.Context
import android.net.Uri

object ImageLengthFinder {

    fun getLength(context: Context, imageUri : Uri) : Long?{
        return try {
            val fileDescriptor = context.applicationContext.contentResolver.openAssetFileDescriptor(imageUri, "r")
            val fileSize = fileDescriptor!!.length

            /**Returning size in KBs*/
            fileSize / 1024
        }catch (e:Exception){
            null
        }
    }
}