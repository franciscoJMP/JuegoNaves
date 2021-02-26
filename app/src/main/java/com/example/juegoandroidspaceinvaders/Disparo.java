package com.example.juegoandroidspaceinvaders;

import android.graphics.Bitmap;

public class Disparo extends Sprite {
    boolean disparoInvasores;

    public Disparo(GameView gameView, Bitmap bmp, int bmpRows,int bmpColumns,int x,int y,int xSpeed, boolean disparoInvasores,boolean vivo) {
        super(gameView, bmp, bmpRows,bmpColumns);

        this.x=x;
        this.y=y;
        this.ySpeed = disparoInvasores ? 25 : -35;
        this.disparoInvasores=disparoInvasores;
        this.vivo=vivo;
    }

    @Override
    public void update() {
        if (x >= gameView.getWidth() - ancho - xSpeed || x + xSpeed <= 0) {
            vivo = false;
        }
        else {
            x = x + xSpeed;
        }

        if (y >= gameView.getHeight() - alto - ySpeed || y + ySpeed <= 0) {
            vivo = false;
        }
        else {
            y = y + ySpeed;
        }

        columnaActual = getSiguienteAnimacionColumna();
    }

    @Override
    public int getSiguienteAnimacionColumna() {
        return ++columnaActual % bmpColumns;
    }

    @Override
    public int getSiguienteAnimacionFila() {
        return 0;
    }

    public int getAbajo(){return y+ancho;}
}
