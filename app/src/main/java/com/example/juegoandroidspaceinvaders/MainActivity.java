package com.example.juegoandroidspaceinvaders;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {
    private GameView view;
    public SensorManager manager;
    public SensorEventListener event;
    public SensorEvent event2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view=new GameView(this,this);
        setContentView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.pausarMusica();
        if(manager!=null && event!=null){
            manager.unregisterListener(event);
        }
        finish();
        view.gameLoopThread.pause=true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        view.iniciarMusica();
        if(manager!=null && event!=null){
            manager.registerListener(event, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        }
        view.gameLoopThread.pause=false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        view.pararMusica();
        view.liberarReproductor();
        if(manager!=null && event!=null){
            manager.unregisterListener(event);
        }
        finish();

    }
}