package com.example.actividad7

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.actividad7.R

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var tvModo : TextView
    private lateinit var tvEstado : TextView
    private lateinit var tvValores : TextView
    private lateinit var layout: View
    //crear atributo que permita el manejo de sensores
    private lateinit var sensorManager : SensorManager
    private var sensorLuz : Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvModo = findViewById(R.id.tvModo)
        tvEstado = findViewById(R.id.tvEstado)
        tvValores = findViewById(R.id.tvValores)
        layout = findViewById(R.id.main)

        //Indicar que el sensor ocupara los servicios del sistema
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        //Hacer uso del sensor de luz
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        //Verificar si el sensor de luz esta disponible
        if (sensorLuz == null) {
            tvEstado.text = "No hay sensor de luz :("
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //Metodos obligatorios
    //Metodo para ver los cambios en el sensor
    override fun onSensorChanged(event: SensorEvent?) {
        //Validar que el sensor sea el correcto
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val luz = event.values[0]
            tvValores.text = "Luz: $luz lx"

            if (luz < 50) {
                layout.setBackgroundColor(Color.BLACK)
                tvModo.text = "Modo: Oscuro"
                tvModo.setTextColor(Color.WHITE)
                tvEstado.setTextColor(Color.WHITE)
                tvValores.setTextColor(Color.WHITE)
            } else {
                layout.setBackgroundColor(Color.WHITE)
                tvModo.text = "Modo: Claro"
                tvModo.setTextColor(Color.BLACK)
                tvEstado.setTextColor(Color.BLACK)
                tvValores.setTextColor(Color.BLACK)
            }
        }
    }

    //Metodo para hacer cambios en los valores matematicos
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    //Metodo para apagar el componente (Sensor)
    override fun onResume() {
        super.onResume()

        sensorLuz?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }
}