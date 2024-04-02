package com.example.longjump

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

//data class MqttMessages(val topic: String, val message: String)

class MainViewModel: ViewModel(){

    val sensor1 = mutableStateOf("No Connected")
    val sensor2 = mutableStateOf("No Connected")
    val sensor3 = mutableStateOf("No Connected")


    fun addMessage(topic: String, message: String){
        when(topic){
            "iotapp/sensor1" -> sensor1.value = message
            "iotapp/sensor2" -> sensor2.value = message
            "iotapp/sensor3" -> sensor3.value = message
        }
    }

}