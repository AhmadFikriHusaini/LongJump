package com.example.longjump.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.longjump.MainViewModel
import com.example.longjump.R
import com.example.longjump.utils.MqttManager

@Composable
fun MainScreen(viewModel: MainViewModel){

    var startSensor by remember {
        mutableStateOf(false)
    }
    val colorButton = remember {
        mutableStateOf(Color.Green)
    }
    val buttonIcon = remember {
        mutableIntStateOf(R.drawable.baseline_play_arrow_24)
    }
    if (viewModel.sensor1.value == "tripped") {
        viewModel.addLogMessage("sensor1")
    }
    if (viewModel.sensor2.value == "tripped") {
        viewModel.addLogMessage("sensor2")
    }
    if (viewModel.sensor3.value == "tripped") {
        viewModel.addLogMessage("sensor3")
    }
    val mqtt = MqttManager(viewModel)

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "LongJump",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                IconButton(onClick = {
                    if (!startSensor){
                        try {
                            mqtt.subscribe()
                            Log.d("MQTT MANAGER", "Connected")
                            viewModel.sensor1.value = "connected"
                            viewModel.sensor2.value = "connected"
                            viewModel.sensor3.value = "connected"
                            startSensor = true
                            colorButton.value = Color.Red
                            buttonIcon.intValue = R.drawable.baseline_stop_24
                        } catch (e: Exception) {
                            Log.e("MQTT MANAGER", "Error: $e")
                        }
                    } else {
                        try {
                            mqtt.unsubscribe()
                            Log.d("MQTT MANAGER", "Disconnected")
                            startSensor = false
                            viewModel.sensor1.value = "disconnected"
                            viewModel.sensor2.value = "disconnected"
                            viewModel.sensor3.value = "disconnected"
                            colorButton.value = Color.Green
                            buttonIcon.intValue = R.drawable.baseline_play_arrow_24
                        } catch (e: Exception){
                            Log.e("MQTT MANAGER", "Error: $e")
                        }
                    }
                }, colors = IconButtonDefaults.iconButtonColors(
                    contentColor = colorButton.value
                )) {
                    Icon(painter = painterResource(id = buttonIcon.intValue), contentDescription = null)
                }
            }
        }
    ) {
        LazyColumn(modifier = Modifier
            .padding(it)
            .fillMaxWidth()) {
            item {
                MQTTItemCard("Sensor 1", viewModel.sensor1.value, viewModel.log1.value)
                MQTTItemCard("Sensor 2", viewModel.sensor2.value, viewModel.log2.value)
                MQTTItemCard("Sensor 3", viewModel.sensor3.value, viewModel.log3.value)
            }
        }
    }
}

@Composable
fun MQTTItemCard(topic: String, message: String, log: String) {
    val sensorIcon = remember {
        mutableIntStateOf(R.drawable.baseline_wifi_protected_setup_24)
    }
    val sensorColor = remember {
        mutableStateOf(Color.Red)
    }
    if (message == "clear") {
        sensorIcon.intValue = R.drawable.baseline_check_24
        sensorColor.value = Color.Green
    }
    if (message == "tripped") {
        sensorIcon.intValue = R.drawable.baseline_cancel_24
        sensorColor.value = Color.Red
    }
    if (message == "connected") {
        sensorIcon.intValue = R.drawable.baseline_wifi_protected_setup_24
        sensorColor.value = Color.Green
    }
    if (message == "disconnected") {
        sensorIcon.intValue = R.drawable.baseline_wifi_protected_setup_24
        sensorColor.value = Color.Red
    }
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        border = CardDefaults.outlinedCardBorder(),
//            elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = topic,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(painter = painterResource(id = sensorIcon.intValue),
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = sensorColor.value
            )
            Text(text = message,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }
        Row(
            modifier = Modifier
                .padding(16.dp, 0.dp, 0.dp, 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Last data: ",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Text(text = log,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }
    }
}

@Preview
@Composable
private fun MainViewPrev() {
    MainScreen(viewModel = MainViewModel())
}