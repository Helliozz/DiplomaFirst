package com.example.cameraapp.UI

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.cameraapp.R
import com.example.cameraapp.data.Constants.APP_CODE_CAMERA_SELECT_PHOTO
import com.example.cameraapp.data.Constants.APP_CODE_CAMERA_TAKE_PHOTO
import com.example.cameraapp.data.Constants.APP_PREFERENCES_PUSHES
import com.example.cameraapp.data.Constants.APP_PREFERENCES_STAY
import com.example.cameraapp.databinding.ActivityMainBinding
import com.example.cameraapp.utils.Utils.updateLiveData
import com.example.cameraapp.viewmodels.MainActivityViewModel
import com.example.cameraapp.viewmodels.MqttViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private val mqttClient: MqttViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#05080D")))

        viewModel.editPreferences(APP_PREFERENCES_STAY, viewModel.getPreference(APP_PREFERENCES_STAY, true))
        viewModel.editPreferences(APP_PREFERENCES_PUSHES, viewModel.getPreference(APP_PREFERENCES_PUSHES, true))

        mqttClient.mqttInitialize(this)
        mqttClient.mqttConnect()
    }


    fun signOut() {
        if (viewModel.isUserSingedIn()) {
            viewModel.signOut()
            //viewModel.editPreferences(APP_PREFERENCES_STAY, false)
            binding.container.findNavController().navigate(R.id.loginFragment)
        } else {
            Log.d("TAG", "Refusing to sign out.")
        }
    }

    fun launchGallery() {
        val intent = Intent()
        intent.setAction(Intent.ACTION_VIEW)
        intent.setType("image/*")
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun startPhotoSelectingSequence() {
        val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(cameraIntent, APP_CODE_CAMERA_TAKE_PHOTO)
    }

    fun startPhotoTakingSequence() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, APP_CODE_CAMERA_SELECT_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == APP_CODE_CAMERA_TAKE_PHOTO || requestCode == APP_CODE_CAMERA_SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                when(requestCode) {
                    APP_CODE_CAMERA_SELECT_PHOTO -> {
                        val targetUri = data?.data
                        if (targetUri != null) {
                            val imageBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(targetUri))
                            viewModel.insertPhoto(imageBitmap)
                        }
                    }
                    APP_CODE_CAMERA_TAKE_PHOTO -> {
                        val imageBitmap = data?.extras?.get("data") as Bitmap?
                        if (imageBitmap != null) {
                            viewModel.insertPhoto(imageBitmap)
                        }
                    }
                    else -> throw(IllegalStateException("Wrong request code."))
                }
                if (supportFragmentManager.primaryNavigationFragment!!.childFragmentManager.fragments.last()::class.java != GalleryFragment::class.java) {
                    binding.container.findNavController().navigate(R.id.galleryFragment)
                }
            }
        }
    }
}