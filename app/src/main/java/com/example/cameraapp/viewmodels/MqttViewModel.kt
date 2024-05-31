package com.example.mqtt

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cameraapp.data.Constants.MQTT_CLIENT_ID
import com.example.cameraapp.data.Constants.MQTT_SERVER_PORT
import com.example.cameraapp.data.Constants.MQTT_SERVER_URI
import com.example.cameraapp.data.Constants.MQTT_TOPIC_LIST
import com.example.cameraapp.data.Constants.MQTT_USER_NAME
import com.example.cameraapp.data.Constants.MQTT_USER_PASSWORD
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttViewModel(): ViewModel() {
    private lateinit var mqttServer: MqttRepository
    var power: MutableLiveData<Boolean> = MutableLiveData(false)

    fun mqttInitialize(context: Context) {
        mqttServer = MqttRepository(context, "${MQTT_SERVER_URI}:${MQTT_SERVER_PORT}", MQTT_CLIENT_ID)
    }

    fun mqttConnect() {
        mqttServer.connect(MQTT_USER_NAME, MQTT_USER_PASSWORD,
            object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    MQTT_TOPIC_LIST.forEach {
                        mqttSubscribe(it)
                    }
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    updateLiveData(power, false)
                    Log.d("MQTT_DEBUGGER", "MQTT connection was failed.")
                    exception?.printStackTrace()

                    try {
                        mqttConnect()
                    } catch (e: Exception) {
                        Log.d("MQTT_DEBUGGER", "MQTT connection function call failed.")
                        e.printStackTrace()
                    }
                }
            },
            object : MqttCallback {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
//                    if(topic == MQTT_TOPIC_TEMPERATURE) {
//                        power.value = true
//                        updateLiveData(tempText, "$message°C")
//                    }
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.d("MQTT_DEBUGGER", "MQTT connection was lost.")
                    cause?.printStackTrace()
                    try {
                        mqttConnect()
                    } catch (e: Exception) {
                        Log.d("MQTT_DEBUGGER", "MQTT connection function call failed.")
                        e.printStackTrace()
                    }
                }
                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })
    }

    private fun <T> updateLiveData(liveData: MutableLiveData<T>, message: T) {
        liveData.value = message
        liveData.postValue(message)
    }


    fun mqttSubscribe(topic: String) {
        if (mqttServer.isConnected()) {
            mqttServer.subscribe(topic,
                1,
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {}
                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {}
                })
        }
    }

    fun mqttPublish(topic: String, message: String) {
        if (mqttServer.isConnected()) {
            mqttServer.publish(topic,
                message,
                1,
                false,
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {}
                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {}
                })
        }
    }

    fun mqttUnsubscribe(topic: String) {
        if (mqttServer.isConnected()) {
            mqttServer.unsubscribe( topic,
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {}
                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {}
                })
        }
    }

    fun mqttDisconnect() {
        if (mqttServer.isConnected()) {
            mqttServer.disconnect(object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {}
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {}
            })
        }
    }

    fun updateConnectionText(power: Boolean = this.power.value!!, connectionText: TextView) {
        if (power) {
            connectionText.text = "Подключено"
        } else {
            connectionText.text = "Отключено"
        }
    }
}