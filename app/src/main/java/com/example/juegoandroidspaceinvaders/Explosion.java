package com.example.juegoandroidspaceinvaders;

import android.graphics.Bitmap;

public class Explosion extends Sprite {
    private int vidas = 3;

    public Explosion(GameView gameView, Bitmap bmp, int bmpRows,int bmpColumns,int x,int y,boolean vivo) {
        super(gameView, bmp, bmpRows,bmpColumns);
        this.vivo=vivo;
        this.x=x;
        this.y=y;
    }

    @Override
    public void update() {
        if(--vidas<1){
            vivo=false;
        }else {
            columnaActual=getSiguienteAnimacionColumna();
        }
    }

    @Override
    public int getSiguienteAnimacionFila() {
        return 0;
    }

    @Override
    public int getSiguienteAnimacionColumna() {
        return ++columnaActual % bmpColumns;
    }
}
