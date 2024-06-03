package com.example.cameraapp.UI

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.cameraapp.R
import com.example.cameraapp.data.Constants.ACTION_CAMERA_PHOTO_NEXT
import com.example.cameraapp.data.Constants.ACTION_CAMERA_PHOTO_PREVIOUS
import com.example.cameraapp.data.Constants.ACTION_CAMERA_PHOTO_TAKE
import com.example.cameraapp.data.Constants.MQTT_TOPIC_ACTION_CAMERA
import com.example.cameraapp.databinding.FragmentGalleryBinding
import com.example.cameraapp.viewmodels.MainActivityViewModel
import com.example.cameraapp.viewmodels.MqttViewModel


class GalleryFragment : Fragment() {
    private lateinit var binding: FragmentGalleryBinding

    private val viewModel: MainActivityViewModel by activityViewModels()
    private val mqttClient: MqttViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initializeRoomDatabase(requireActivity().application)

        binding.arrowForward.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_galleryFragment_to_screenFragment)
        }

        binding.gmailCom.text = viewModel.getUserEmail()
        binding.goToGallery.setOnClickListener {
            (requireActivity() as MainActivity).startPhotoSelectingSequence()
        }
        binding.signOut.setOnClickListener {
            (requireActivity() as MainActivity).signOut()
        }

        binding.left.setOnClickListener {
            mqttClient.mqttPublish(MQTT_TOPIC_ACTION_CAMERA, ACTION_CAMERA_PHOTO_PREVIOUS)

            if (viewModel.getAllCData().value!!.isNotEmpty()) {
                viewModel.currentPhotoIndex = (viewModel.currentPhotoIndex - 1)
                if (viewModel.currentPhotoIndex < 0) { viewModel.currentPhotoIndex += viewModel.getAllCData().value!!.size }
                loadPhoto()
            }
        }
        binding.right.setOnClickListener {
            mqttClient.mqttPublish(MQTT_TOPIC_ACTION_CAMERA, ACTION_CAMERA_PHOTO_NEXT)

            if (viewModel.getAllCData().value!!.isNotEmpty()) {
                viewModel.currentPhotoIndex = (viewModel.currentPhotoIndex + 1) % viewModel.getAllCData().value!!.size
                loadPhoto()
            }
        }
        binding.camera.setOnClickListener {
            mqttClient.mqttPublish(MQTT_TOPIC_ACTION_CAMERA, ACTION_CAMERA_PHOTO_TAKE)

            (requireActivity() as MainActivity).startPhotoTakingSequence()
        }

        viewModel.getAllCData().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                loadPhoto(it.lastIndex)
            }
        }
    }

    private fun loadPhoto(index: Int = viewModel.currentPhotoIndex) {
        try {
            val bitmap = viewModel.getPhoto(index)
            binding.imageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to load the photo.", Toast.LENGTH_SHORT).show()
            viewModel.deletePhoto(viewModel.getAllCData().value!![index])
            Log.d("APP_DEBUGGER", "Failed to load the photo.")
            e.printStackTrace()
        }
    }
}