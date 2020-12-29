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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.td2_jin.databinding.ActivityUserInfoBinding
import com.example.td2_jin.network.Api
import com.example.td2_jin.network.UserInfo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    companion object {
        const val EDIT_USER_INFO_CODE = 777
        const val USERINFO_KEY = "Userinfo"
    }

    private val userWebService = Api.INSTANCE.userWebService
    //private val userInfoViewModel: UserInfoViewModel by viewModels()

    private var userInfo : UserInfo? = null

    private lateinit var binding: ActivityUserInfoBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_user_info)

        userInfo = intent.getSerializableExtra(USERINFO_KEY) as? UserInfo
        binding.userInfo = userInfo

       binding.takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }

        binding.uploadImageButton.setOnClickListener {
            pickInGallery.launch("image/*")
        }

        binding.imageButtonValidate.setOnClickListener {
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