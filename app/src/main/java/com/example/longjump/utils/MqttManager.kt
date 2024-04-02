package com.example.longjump.utils

import android.util.Log
import com.example.longjump.MainViewModel
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttManager(val viewModel: MainViewModel) {
    private var client: MqttAsyncClient? = null

    init {
        connect()
    }

    fun connect() {
        try {
            if (client == null) {
                val serverUri = "tcp://test.mosquitto.org:1883"
                val clientId = ""
                client = MqttAsyncClient(serverUri, clientId, MemoryPersistence())
                val options = MqttConnectOptions()
                options.isAutomaticReconnect = true
                client?.connect(options, null, object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        // Successfully connected
                        Log.d("MQTTManager", "Client connected successfully")
                    }
                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.e("MQTTManager", "Error connecting to MQTT broker: $exception")
                    }
                })
            } else {
                Log.d("MQTTManager", "Client already initialized")
            }
        } catch (e: Exception) {
            Log.e("MQTTManager", "Error initializing MQTT client: $e")
        }
    }

    fun subscribe(){
        client?.subscribe("iotapp/#", 0)
        client?.setCallback(object : MqttCallbackExtended {
            override fun connectionLost(cause: Throwable?) {
                Log.d("MQTTManager", "Connection lost $cause")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d("MQTTManager", "Message arrived")
                topic?.let {
                    viewModel.addMessage(it, message?.toString() ?: "No message yet")
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d("MQTTManager", "Delivery complete")
            }

            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                Log.d("MQTTManager", "Connection complete")
            }
        })
    }

    fun unsubscribe(){
        client?.unsubscribe("iotapp/#")
    }

    fun disconnect() {
        try {
            client?.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MQTTManager", "Client disconnected successfully")
                    client = null // Reset client after disconnecting
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e("MQTTManager", "Error disconnecting MQTT client: $exception")
                }
            })
        } catch (e: Exception) {
            Log.e("MQTTManager", "Error disconnecting MQTT client: $e")
        }
    }
}