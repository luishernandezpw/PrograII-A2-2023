package com.ugb.miapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    TextView temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp = findViewById(R.id.lblSensorLuz);
        activarSensorLuz();
    }
    @Override
    protected void onPause() {
        detener();
        super.onPause();
    }
    @Override
    protected void onResume() {
        iniciar();
        super.onResume();
    }
    private void activarSensorLuz(){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(sensor==null){
            temp.setText("Tu telefono NO soporta el sensor de LUZ");
            finish();
        }
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                temp.setText("Proximidad: Valor = "+ sensorEvent.values[0]);
                if( sensorEvent.values[0]<=20 ){
                    getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                } else if (sensorEvent.values[0]>20 && sensorEvent.values[0]<=50) {
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                } else{
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }
    private void iniciar(){
        sensorManager.registerListener(sensorEventListener, sensor, 2000*1000);
    }
    private void detener(){
        sensorManager.unregisterListener(sensorEventListener);
    }
}