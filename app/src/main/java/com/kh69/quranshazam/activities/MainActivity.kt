package com.kh69.quranshazam.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.kh69.quranshazam.R
import com.kh69.quranshazam.extensions.getPermissionString
import com.kh69.quranshazam.extensions.hasPermission
import com.kh69.quranshazam.extensions.toast
import com.kh69.quranshazam.helpers.PERMISSION_RECORD_AUDIO

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handlePermission(PERMISSION_RECORD_AUDIO) {
            if (it) {
//                logic when permission allowed
            } else {
                toast(R.string.no_audio_permissions)
                finish()
            }
        }

    }

}