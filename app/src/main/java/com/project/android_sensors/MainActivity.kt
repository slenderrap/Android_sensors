package com.project.android_sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.android_sensors.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometre: Sensor
    private lateinit var binding: ActivityMainBinding
    private var vegades: Int =0
    private var tapCounterX = 0
    private var tapCounterY = 0
    private var tapCounterZ = 0
    private val threshold = 10.0
    private val timeWindow = 300L
    private val cooldDown = 500L
    private var lastTapTimeX = 0L
    private var lastTapTimeY = 0L
    private var lastTapTimeZ = 0L
    private var lastDoubleTap = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Utilitzem bindings per facilitar accés als elements gràfics
        // recorda activar-los a build.gradle.kts afegint:
        // viewBinding { enable = true }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // escoltar variacions dels sensors
        sensorManager = getSystemService(
            Context.SENSOR_SERVICE) as SensorManager
        accelerometre = sensorManager.getDefaultSensor(
            Sensor.TYPE_LINEAR_ACCELERATION) as Sensor
        sensorManager.registerListener(this, accelerometre,
            SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(event: SensorEvent) {
        // emprem les dades del sensor
        val x = abs(event.values[0])
        val y = abs(event.values[1])
        val z = abs(event.values[2])
        // 1g = 9,8 m/s² , què és un valor força alt.
        // Al fer *10 ens acostem als 100, que és el valor màxim per defecte de la ProgressBar
        binding.xProgressBar.progress = abs(x*10.0).toInt()
        binding.yProgressBar.progress = abs(y*10.0).toInt()
        binding.zProgressBar.progress = abs(z*10.0).toInt()

        detectDoubleTap(x,y,z)
    }

    private fun detectDoubleTap(x: Float, y: Float, z: Float) {
        val currentTime = System.currentTimeMillis()


        if (x>threshold && currentTime - lastDoubleTap > cooldDown){
            if (currentTime - lastTapTimeX < timeWindow) {
                Toast.makeText(this,"S'ha fet un double tap a l'eix X", Toast.LENGTH_SHORT).show()
                tapCounterX = binding.contadorXTextViewNum.text.toString().toInt() + 1
                binding.contadorXTextViewNum.text = tapCounterX.toString()
                vegades = binding.contadorTextViewNum.text.toString().toInt() + 1
                binding.contadorTextViewNum.text = vegades.toString()
                lastDoubleTap = currentTime
            }else{
                lastTapTimeX = currentTime
            }
        }
        if (y>threshold && currentTime - lastDoubleTap > cooldDown){
            if (currentTime - lastTapTimeY < timeWindow) {
                Toast.makeText(this,"S'ha fet un double tap a l'eix Y", Toast.LENGTH_SHORT).show()
                tapCounterY = binding.contadorYTextViewNum.text.toString().toInt() + 1
                binding.contadorYTextViewNum.text = tapCounterY.toString()
                vegades = binding.contadorTextViewNum.text.toString().toInt() +1
                binding.contadorTextViewNum.text = vegades.toString()
                lastDoubleTap =currentTime
            }else{
                lastTapTimeY = currentTime
            }
        }
        if (z>threshold && currentTime - lastDoubleTap > cooldDown) {
            if (currentTime - lastTapTimeZ < timeWindow) {
                Toast.makeText(this,"S'ha fet un double tap a l'eix Z", Toast.LENGTH_SHORT).show()
                tapCounterZ = binding.contadorZTextViewNum.text.toString().toInt() + 1
                binding.contadorZTextViewNum.text = tapCounterZ.toString()
                vegades = binding.contadorTextViewNum.text.toString().toInt() + 1
                binding.contadorTextViewNum.text = vegades.toString()
                lastDoubleTap = currentTime
            } else {
                lastTapTimeZ = currentTime
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // TODO("Not yet implemented")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("tapCounterX",tapCounterX)
        outState.putInt("tapCounterY",tapCounterY)
        outState.putInt("tapCounterZ",tapCounterZ)
        outState.putInt("vegades",vegades)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        tapCounterX = savedInstanceState.getInt("tapCounterX", 0)
        tapCounterY = savedInstanceState.getInt("tapCounterY", 0)
        tapCounterZ = savedInstanceState.getInt("tapCounterZ", 0)
        vegades = savedInstanceState.getInt("vegades",0)
    }
    
}