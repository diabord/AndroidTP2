package com.example.td2_jin.userinfo

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Files.getContentUri
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.td2_jin.R
import com.example.td2_jin.network.Api
import com.example.td2_jin.network.UserInfo
import com.example.td2_jin.task.TaskActivity
import com.example.td2_jin.tasklist.Task
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    companion object {
        const val EDIT_USER_INFO_CODE = 777
        const val IMAGE_KEY = "IMAGE"
        const val USERINFO_KEY = "Userinfo"
    }

    private val userWebService = Api.INSTANCE.userWebService
    //private val userInfoViewModel: UserInfoViewModel by viewModels()

    private var userInfo : UserInfo? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        userInfo = intent.getSerializableExtra(USERINFO_KEY) as? UserInfo

        findViewById<Button>(R.id.take_picture_button).setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }

        findViewById<Button>(R.id.upload_image_button).setOnClickListener {
            pickInGallery.launch("image/*")
        }

        findViewById<ImageButton>(R.id.imageButtonValidate).setOnClickListener {
            updateUserInfo()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) openCamera()
            else showExplanationDialog()
        }

    private fun requestCameraPermission() =
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestCameraPermission()
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }

    private fun updateUserInfo(){
        userInfo?.firstName = findViewById<EditText>(R.id.Prenom).text.toString()
        userInfo?.lastName = findViewById<EditText>(R.id.Nom).text.toString()
        userInfo?.email = findViewById<EditText>(R.id.Email).text.toString()
        intent.putExtra(UserInfoActivity.USERINFO_KEY, userInfo)
        setResult(1,intent)
        finish()

    }

    // create a temp file and get a uri for it
    private val photoUri = getContentUri("temp")

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        handleImage(tmpFile.toUri())
    }

    // use
    private fun openCamera() = takePicture.launch()

    // convert
    private fun convert(uri: Uri) =
            MultipartBody.Part.createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
            )

    //handle image
    private fun handleImage(photoUri: Uri) {
        lifecycleScope.launch {
            userWebService.updateAvatar(convert(photoUri))
        }
    }

    // register
    private val pickInGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            handleImage(uri)
        }
}