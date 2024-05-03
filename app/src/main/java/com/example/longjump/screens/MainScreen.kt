package com.example.longjump.screens

import android.Manifest
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.core.app.ActivityCompat
import com.example.longjump.MainActivity
import com.example.longjump.MainViewModel
import com.example.longjump.R
import com.example.longjump.utils.MqttManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(viewModel: MainViewModel, context: Context){

    val vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator

    if (viewModel.sensor1.value == "tripped") {
        viewModel.addLogMessage("sensor1")

        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    }
    if (viewModel.sensor2.value == "tripped") {
        viewModel.addLogMessage("sensor2")

        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    }
    if (viewModel.sensor3.value == "tripped") {
        viewModel.addLogMessage("sensor3")

        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    }
    val mqtt = MqttManager(viewModel, context)

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
            }
        }
    ) {
        LazyColumn(modifier = Modifier
            .padding(it)
            .fillMaxWidth()) {
            item {
                MQTTItemCard("Sensor 1", viewModel.sensor1.value, viewModel.log1.value, context)
                MQTTItemCard("Sensor 2", viewModel.sensor2.value, viewModel.log2.value, context)
                MQTTItemCard("Sensor 3", viewModel.sensor3.value, viewModel.log3.value, context)
            }
        }
    }
}

@Composable
fun MQTTItemCard(topic: String, message: String, log: String, context: Context) {

    val sensorIcon = remember {
        mutableIntStateOf(R.drawable.baseline_wifi_protected_setup_24)
    }
    val sensorColor = remember {
        mutableStateOf(Color.Red)
    }

    when (message) {
        "clear" -> {
            sensorIcon.intValue = R.drawable.baseline_check_24
            sensorColor.value = Color.Green
        }
        "tripped" -> {
            sensorIcon.intValue = R.drawable.baseline_cancel_24
            sensorColor.value = Color.Red
//            vibrate.vibrate(500)
        }
        "Connected" -> {
            sensorIcon.intValue = R.drawable.baseline_wifi_protected_setup_24
            sensorColor.value = Color.Green
        }
        "Disconnected" -> {
            sensorIcon.intValue = R.drawable.baseline_wifi_protected_setup_24
            sensorColor.value = Color.Red
        }
    }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        border = CardDefaults.outlinedCardBorder(),
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

//@Preview
//@Composable
//private fun MainViewPrev() {
//    MainScreen(viewModel = MainViewModel())
//}