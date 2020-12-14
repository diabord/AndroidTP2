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
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.td2_jin.R
import com.example.td2_jin.network.Api
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    companion object {
        const val CHANGE_PROFILE_PICTURE_REQUEST_CODE = 777
        const val IMAGE_KEY = "IMAGE"
    }

    private val userWebService = Api.userWebService

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        findViewById<Button>(R.id.take_picture_button).setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }

        findViewById<Button>(R.id.upload_image_button).setOnClickListener {
            pickInGallery.launch("image/*")
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

    // create a temp file and get a uri for it
    private val photoUri = getContentUri("temp")

    // register
    /*private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success){
                val tmpFile = File.createTempFile("avatar", "jpeg")
                tmpFile.outputStream().use {
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
                handleImage(photoUri)
            }
            else Toast.makeText(this, "Si vous refusez, on peux pas prendre de photo ! 😢", Toast.LENGTH_LONG).show()
        }*/
    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { picture ->
            val tmpFile = File.createTempFile("avatar", "jpeg")
            tmpFile.outputStream().use {
                picture.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            handleImage(tmpFile.toUri())
        }

    // use
    //private fun openCamera() = takePicture.launch(photoUri)
    private fun openCamera() = takePicture.launch()

    // convert
    private fun convert(uri: Uri) =
        MultipartBody.Part.create(uri.toFile().asRequestBody("image/jpeg".toMediaType()))

    //handle image
    private fun handleImage(photoUri: Uri) {
        //println("HEEEEEEEEEEEEEEHOOOOOOOOOOOOOOOOOOOOOOOO : " + convert(photoUri))
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