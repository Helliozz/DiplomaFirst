package com.example.cameraapp.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.cameraapp.R
import com.example.cameraapp.data.Constants.ACTION_VIDEO_SWITCH_SCREEN
import com.example.cameraapp.data.Constants.ACTION_VIDEO_TURN_LEFT
import com.example.cameraapp.data.Constants.ACTION_VIDEO_TURN_RIGHT
import com.example.cameraapp.data.Constants.MQTT_TOPIC_ACTION_VIDEO
import com.example.cameraapp.databinding.FragmentScreenBinding
import com.example.cameraapp.viewmodels.MainActivityViewModel
import com.example.cameraapp.viewmodels.MqttViewModel


class ScreenFragment : Fragment() {
    private lateinit var binding: FragmentScreenBinding

    private val viewModel: MainActivityViewModel by activityViewModels()
    private val mqttClient: MqttViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBackwards.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_screenFragment_to_galleryFragment)
        }

        binding.gmailCom.text = viewModel.getUserEmail()
        binding.goToGallery.setOnClickListener {
            (activity as MainActivity).startPhotoSelectingSequence()
        }
        binding.signOut.setOnClickListener {
            (activity as MainActivity).signOut()
        }

        binding.left.setOnClickListener {
            mqttClient.mqttPublish(MQTT_TOPIC_ACTION_VIDEO, ACTION_VIDEO_TURN_LEFT)
        }
        binding.right.setOnClickListener {
            mqttClient.mqttPublish(MQTT_TOPIC_ACTION_VIDEO, ACTION_VIDEO_TURN_RIGHT)
        }
        binding.screen.setOnClickListener {
            mqttClient.mqttPublish(MQTT_TOPIC_ACTION_VIDEO, ACTION_VIDEO_SWITCH_SCREEN)
        }
    }
}