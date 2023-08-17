package com.example.proyectosensores

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Text
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

@Preview(showBackground = true, showSystemUi = true)

@Composable
private fun LayoutPreview() {
    getDataPressure()
}

@Composable
fun getDataPressure() {
    var pressure by remember { mutableStateOf(0f) }

    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    val dataClient = Wearable.getDataClient(context)

    val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_PRESSURE) {
                pressure = event.values[0]
                // Enviar los datos al teléfono
                val putDataMapRequest = PutDataMapRequest.create("/pressure-data")
                putDataMapRequest.dataMap.putFloat("pressure", pressure)

                val request = putDataMapRequest.asPutDataRequest()
                val task = dataClient.putDataItem(request)
            }
        }
    }

    DisposableEffect(sensorManager) {
        sensorManager.registerListener(
            sensorEventListener,
            pressureSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    Column(modifier = Modifier.background(Color.Black)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Row() {
                Text(text = "Pres ión:")
                Text(text = "${pressure}")
            }
        }
    }

}