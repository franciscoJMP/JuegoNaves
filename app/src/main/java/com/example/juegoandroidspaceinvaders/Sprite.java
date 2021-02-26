package com.example.juegoandroidspaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.widget.Toast;

public class Sprite {
    protected int
            x = 0,
            y = 0,
            xSpeed,
            ySpeed,
            ancho,
            alto,
            bmpRows = 2,
            bmpColumns = 12,
            columnaActual = 0,
            filaActual = 0;

    protected GameView gameView;
    protected Bitmap bmp;


    protected Rect src, dst;

    //Detectamos si esta vivo o muerto;
    protected boolean vivo;

    public Sprite(GameView gameView, Bitmap bmp, int bmpRows,int bmpColumns) {

        //Esto se pone porque no todos los sprites tiene las mismas filas y columnas
        this.bmpRows = bmpRows;
        this.bmpColumns = bmpColumns;
        this.gameView = gameView;
        this.bmp = bmp;

        this.ancho = bmp.getWidth()/bmpColumns;
        this.alto = bmp.getHeight()/bmpRows;

        //Inicializamos las matrices para pintar cada sprite

        int auxX = columnaActual * ancho;
        int auxY = filaActual * alto;

        src = new Rect(auxX, auxY, auxX + ancho, auxY + alto);
        dst = new Rect(x, y, x + ancho, y + alto);

        //Esto dependara de cada sprite
        
        x = 30;
        y = 30;
        xSpeed = 0;
        ySpeed = 0;
        vivo = true;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getBmpRows() {
        return bmpRows;
    }

    public void setBmpRows(int bmpRows) {
        this.bmpRows = bmpRows;
    }

    public int getBmpColumns() {
        return bmpColumns;
    }

    public void setBmpColumns(int bmpColumns) {
        this.bmpColumns = bmpColumns;
    }

    public int getColumnaActual() {
        return columnaActual;
    }

    public void setColumnaActual(int columnaActual) {
        this.columnaActual = columnaActual;
    }

    public int getFilaActual() {
        return filaActual;
    }

    public void setFilaActual(int filaActual) {
        this.filaActual = filaActual;
    }

    public Rect getSrc() {
        return src;
    }

    public void setSrc(Rect src) {
        this.src = src;
    }

    public Rect getDst() {
        return dst;
    }

    public void setDst(Rect dst) {
        this.dst = dst;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    public void update() {
        if (x >= gameView.getWidth() - ancho - xSpeed || x + xSpeed <= 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;

        if (y >= gameView.getHeight() - alto - ySpeed || y + ySpeed <= 0) {
            ySpeed = -ySpeed;
        }

        y = y + ySpeed;
        columnaActual = ++columnaActual % bmpColumns;
    }

    public void onDraw(Canvas canvas) {
        update();
        int srcX = columnaActual* ancho;
        int srcY = filaActual * alto;

        src.left=srcX;
        src.top = srcY;
        src.right = srcX + ancho;
        src.bottom = srcY + alto;

        dst.left=x;
        dst.top=y;
        dst.right=x+ancho;
        dst.bottom=y+alto;

        canvas.drawBitmap(bmp, src, dst, null);
    }

    public int getSiguienteAnimacionFila() {
        return 0;
    }

    public int getSiguienteAnimacionColumna() {
        return 0;
    }

    public boolean isCollition(float x2, float y2) {
        return x2 > x && x2 < x + ancho && y2 > y && y2 < y + alto;
    }

    public boolean isCollition(Sprite sprite2) {

        //Detecta la colision entre dos sprites
        if (this.x < sprite2.getX() + sprite2.getAncho() &&
                this.x + this.ancho > sprite2.getX() &&
                this.y + this.alto > sprite2.getY() &&
                this.y < sprite2.getY() + sprite2.getAlto()) {
            return true;

        } else {
            return false;
        }

    }
}
