package com.example.sensores

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var square: TextView

    private var brightness:Sensor? = null
    private lateinit var light: TextView

    private var gyro:Sensor? = null
    private lateinit var gyroText: TextView

    private var prox:Sensor? = null
    private lateinit var proxText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        square = findViewById(R.id.acc_square)
        light = findViewById(R.id.light_text)
        gyroText = findViewById(R.id.gyro_text)
        proxText = findViewById(R.id.proximity_text)

        setUpSensorAccel()
        setUpLightSensor()
        setUpGyroSensor()
        setUpProxSensor()
    }

    private fun setUpSensorAccel(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    private fun setUpLightSensor(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    private fun setUpGyroSensor(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    private fun setUpProxSensor(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        prox = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            val sides = event.values[0]
            val upDown = event.values[1]

            square.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }

            val color = if(upDown.toInt() == 0 && sides.toInt() == 0) Color.GREEN else Color.BLUE
            square.setBackgroundColor(color)
            square.text = "Cima/baixo: ${upDown.toInt()}\n Lados:${sides.toInt()}"
        }

        if(event?.sensor?.type == Sensor.TYPE_LIGHT){
            light.text = "Luz: ${event.values[0]}"
        }

        if(event?.sensor?.type == Sensor.TYPE_GYROSCOPE){
            gyroText.text = "X: ${event.values[0].toInt()}\nY: ${event.values[1].toInt()}\nZ: ${event.values[2].toInt()}"
        }

        if(event?.sensor?.type == Sensor.TYPE_PROXIMITY){
            proxText.text = "Proximidade: ${event.values[0]}"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
                this,
                brightness,
                SensorManager.SENSOR_DELAY_FASTEST)

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also{
            sensorManager.registerListener(
                    this,
                    it,
                    SensorManager.SENSOR_DELAY_FASTEST)
        }

        sensorManager.registerListener(
                this,
                gyro,
                SensorManager.SENSOR_DELAY_FASTEST)

        sensorManager.registerListener(this,
                prox,
                SensorManager.SENSOR_DELAY_FASTEST)
    }

}