package com.example.juegoandroidspaceinvaders;

import android.graphics.Bitmap;

public class NavesInvasoras extends Sprite {
    public int puntos;
    public NavesInvasoras(GameView gameView, Bitmap bmp, int bmpColumns, int bmpRows,int x ,int y, int puntos) {
        super(gameView, bmp, bmpColumns, bmpRows);
        this.x=x;
        this.y=y;
        this.xSpeed=0;
        this.ySpeed=0;
        this.puntos=puntos;
    }

    @Override
    public void update() {
        if (x >= gameView.getWidth() - ancho - xSpeed || x + xSpeed <= 0) {
        }
        else {
            x = x + xSpeed;
        }

        columnaActual = getSiguienteAnimacionColumna();
    }

    @Override
    public int getSiguienteAnimacionColumna() {
        return ++columnaActual % bmpColumns;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    public void moverAbajo(int pasoAbajo){
        this.y=y+pasoAbajo;
    }
}
