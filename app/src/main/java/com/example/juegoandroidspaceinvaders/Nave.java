package com.example.juegoandroidspaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class Nave extends Sprite implements SensorEventListener {
    //Esta variable nos va a permitir calibrar la sensibilidad del sensor
    float sensivilidadSensor;
    Context contexto;
    static final float MULTIPLICADOR= (float) 9.82;

    public Nave(GameView gameView, Bitmap bmp, int bmpRows, int bmpColum, Context contexto) {
        super(gameView, bmp, bmpRows, bmpColum);
        x = (gameView.getWidth() - ancho) / 2;
        y = gameView.getHeight() - alto;
        xSpeed = 0;
        ySpeed = 0;
        this.contexto=contexto;

        //Inicializamos el sensor y su sensibilidad

        SensorManager sensorManager = (SensorManager) contexto.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        this.gameView.activity.manager=sensorManager;
        sensivilidadSensor = (float) 1.75;
    }


    @Override
    public void update() {
        if (x >= gameView.getWidth() - ancho / 2 - xSpeed || x + xSpeed <= -ancho / 2) {
        } else {
            x = x + xSpeed;
        }

        columnaActual = getSiguienteAnimacionColumna();
    }

    @Override
    public int getSiguienteAnimacionColumna() {
        float y1, y2, x1, x2;
        x1 = -sensivilidadSensor * MULTIPLICADOR;
        x2 = sensivilidadSensor * MULTIPLICADOR;
        y1 = 0;
        y2 = 12;
        return (int) ((y2 - y1) / (x2 - x1) * ((float) xSpeed - x1) + y1);
    }

    @Override
    public int getSiguienteAnimacionFila() {
        return 0;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x;
        x = event.values[0];
        if (x > MULTIPLICADOR) {
            x =  MULTIPLICADOR;

        } else if (x < -MULTIPLICADOR) {

            x = -MULTIPLICADOR;
        }
        this.xSpeed = (int) (-x * sensivilidadSensor);
        this.gameView.activity.event2=event;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public int getxSpeed() {
        return xSpeed;
    }

    @Override
    public boolean isCollition(Sprite sprite2) {
        int auxX = x + 20;
        int auxAncho = ancho - 40;

        if (
                auxX < sprite2.getX() + sprite2.getAncho() &&
                auxX + auxAncho > sprite2.getX() &&
                this.y + this.alto > sprite2.getY() &&
                this.y < sprite2.getY() + sprite2.getAlto()
        ) {
            return true;
        } else {
            return false;
        }
    }
}
