package com.example.longjump.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class OtherUtils(context: Context) {
    fun hasVibratePermission(context: Context): Boolean{
        return ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED
    }
}