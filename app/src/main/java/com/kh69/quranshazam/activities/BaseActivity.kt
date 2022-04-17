package com.kh69.quranshazam.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.kh69.quranshazam.R
import com.kh69.quranshazam.extensions.getPermissionString
import com.kh69.quranshazam.extensions.hasPermission
import com.kh69.quranshazam.extensions.toast
import com.kh69.quranshazam.helpers.PERMISSION_RECORD_AUDIO

abstract class BaseActivity : AppCompatActivity() {

    var actionOnPermission: ((granted: Boolean) -> Unit)? = null
    var isAskingPermissions = false
    private val GENERIC_PERM_HANDLER = 100

    fun handlePermission(permissionId: Int, callback: (granted: Boolean) -> Unit) {
        actionOnPermission = null
        if (hasPermission(permissionId)) {
            callback(true)
        } else {
            isAskingPermissions = true
            actionOnPermission = callback
            ActivityCompat.requestPermissions(
                this,
                arrayOf(getPermissionString(permissionId)),
                GENERIC_PERM_HANDLER
            )
        }
    }
}