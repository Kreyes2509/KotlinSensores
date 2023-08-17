package com.example.proyectosensores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable

class HomeActivity : AppCompatActivity(), DataClient.OnDataChangedListener{

    private lateinit var dataClient: DataClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        dataClient = Wearable.getDataClient(this)

        dataClient.addListener(this)
    }

    override fun onDataChanged(dataEventBuffer: DataEventBuffer) {
        for (event in dataEventBuffer) {
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/pressure-data") {
                val pressureValue = DataMapItem.fromDataItem(event.dataItem).dataMap.getFloat("pressure")
                println(pressureValue)

                val pressureTextView = findViewById<TextView>(R.id.precion)
                pressureTextView.text = "Presión: $pressureValue"
            }
        }
    }
}