package com.example.mqtt

import android.content.Context
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttRepository(context: Context,
                     serverURI: String,
                     clientID: String = "") {
    private var mqttClient = MqttAndroidClient(context, serverURI, clientID)

    private val defaultCbConnect = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        }
    }
    private val defaultCbClient = object : MqttCallback {
        override fun messageArrived(topic: String?, message: MqttMessage?) {
        }

        override fun connectionLost(cause: Throwable?) {
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
        }
    }
    private val defaultCbSubscribe = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        }
    }
    private val defaultCbUnsubscribe = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        }
    }
    private val defaultCbPublish = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        }
    }
    private val defaultCbDisconnect = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        }
    }

    fun connect(username:   String,
                password:   String,
                cbConnect:  IMqttActionListener  = defaultCbConnect,
                cbClient:   MqttCallback         = defaultCbClient) {
        mqttClient.setCallback(cbClient)
        val options = MqttConnectOptions()
        options.userName = username
        options.password = password.toCharArray()

        try {
            mqttClient.connect(options, null, cbConnect)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic:        String,
                  qos:          Int                 = 1,
                  cbSubscribe:  IMqttActionListener = defaultCbSubscribe) {
        try {
            mqttClient.subscribe(topic, qos, null, cbSubscribe)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun unsubscribe(topic:          String,
                    cbUnsubscribe:  IMqttActionListener = defaultCbUnsubscribe) {
        try {
            mqttClient.unsubscribe(topic, null, cbUnsubscribe)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic:      String,
                msg:        String,
                qos:        Int                 = 1,
                retained:   Boolean             = false,
                cbPublish:  IMqttActionListener = defaultCbPublish) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, cbPublish)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect(cbDisconnect: IMqttActionListener = defaultCbDisconnect ) {
        try {
            mqttClient.disconnect(null, cbDisconnect)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
    fun isConnected(): Boolean {
        return mqttClient.isConnected
    }
}

