package com.example.td2_jin.userinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import com.example.td2_jin.R
import com.example.td2_jin.tasklist.Task
import java.util.*

class UserInfoActivity : AppCompatActivity() {
    companion object {
        const val CHANGE_PROFILE_PICTURE_REQUEST_CODE = 777
        const val IMAGE_KEY = "IMAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
    }
}