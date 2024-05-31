package com.example.cameraapp.UI

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cameraapp.data.Constants.APP_PREFERENCES_PUSHES
import com.example.cameraapp.data.Constants.APP_PREFERENCES_STAY
import com.example.cameraapp.data.Constants.MQTT_CLIENT_ID
import com.example.cameraapp.data.Constants.MQTT_SERVER_PORT
import com.example.cameraapp.data.Constants.MQTT_SERVER_URI
import com.example.cameraapp.databinding.ActivityMainBinding
import com.example.cameraapp.viewmodels.MainActivityViewModel
import com.example.mqtt.MqttRepository
import com.example.mqtt.MqttViewModel
import dagger.hilt.android.AndroidEntryPoint

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
}