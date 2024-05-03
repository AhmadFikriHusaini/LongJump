package com.example.longjump

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

//data class MqttMessages(val topic: String, val message: String)

class MainViewModel: ViewModel(){

    val sensor1 = mutableStateOf("Connected")
    val sensor2 = mutableStateOf("Connected")
    val sensor3 = mutableStateOf("Connected")

    val log1 = mutableStateOf("N/a")
    val log2 = mutableStateOf("N/a")
    val log3 = mutableStateOf("N/a")

    fun addMessage(topic: String, message: String){
        when(topic){
            "iotapp/sensor1" -> sensor1.value = message
            "iotapp/sensor2" -> sensor2.value = message
            "iotapp/sensor3" -> sensor3.value = message
        }
    }
    fun addLogMessage(topic: String){
        when(topic){
            "sensor1" -> log1.value = sensor1.value
            "sensor2" -> log2.value = sensor1.value
            "sensor3" -> log3.value = sensor1.value
        }
    }

}