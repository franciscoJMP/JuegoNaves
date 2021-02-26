package com.example.juegoandroidspaceinvaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlotaNaves {
    private List<List<NavesInvasoras>> listaNavesInvasoras;
    private GameView gameView;
    private int spaceBetweenRows=25;
    private int periodMoveDown = 60;
    private int timerMoveDown =20;
    private int pasoAbajo = 20;
    private int lastDestroyedShipPoints;
    private int alturaAterrizaje;
    private boolean arrive = false;

    Bitmap naveInvasora1,naveInvasora2;

    private int numeroDeNaves= 3;

    public  FlotaNaves(GameView gameView,int landingHeight){
        timerMoveDown = timerMoveDown/(2*2);
        periodMoveDown = periodMoveDown/(2*2);
        this.gameView = gameView;
        this.alturaAterrizaje = landingHeight;
        this.naveInvasora1= BitmapFactory.decodeResource(gameView.getResources(),R.drawable.invaderspaceship_1);
        this.naveInvasora2=BitmapFactory.decodeResource(gameView.getResources(),R.drawable.invaderspaceship_2);
        listaNavesInvasoras=new ArrayList<List<NavesInvasoras>>();

        listaNavesInvasoras.add(crearFilaDeNaves(0,naveInvasora1,1,15,numeroDeNaves-1,300));
        listaNavesInvasoras.add(crearFilaDeNaves(1,naveInvasora2,1,2,numeroDeNaves,200));
        listaNavesInvasoras.add(crearFilaDeNaves(2,naveInvasora1,1,15,numeroDeNaves,100));
        listaNavesInvasoras.add(crearFilaDeNaves(3,naveInvasora1,1,15,numeroDeNaves,100));
        listaNavesInvasoras.add(crearFilaDeNaves(4,naveInvasora2,1,2,numeroDeNaves,200));

    }

    public List<List<NavesInvasoras>> getListaNavesInvasoras() {
        return listaNavesInvasoras;
    }

    public void setListaNavesInvasoras(List<List<NavesInvasoras>> listaNavesInvasoras) {
        this.listaNavesInvasoras = listaNavesInvasoras;
    }

    public void onDraw(Canvas canvas){
        for (List<NavesInvasoras> filaFlota : listaNavesInvasoras) {
            for (Sprite sprite : filaFlota) {
                sprite.onDraw(canvas);
            }
        }

        timerMoveDown = timerMoveDown - 1;

        if (timerMoveDown == 0) {
            moveDown();
            timerMoveDown = periodMoveDown;
        }

    }

    private void moveDown() {
        for (List<NavesInvasoras> filaFlota : listaNavesInvasoras){
            for (NavesInvasoras sprite : filaFlota) {
                sprite.moverAbajo(pasoAbajo);
                if(sprite.getY()>alturaAterrizaje){
                    arrive = true;
                }
            }
        }
    }

    private List<NavesInvasoras> crearFilaDeNaves(int filaFlota, Bitmap bmp, int filaSprite, int columanSprite, int numeroDeNaves, int puntos) {
        ArrayList<NavesInvasoras> filas=new ArrayList<NavesInvasoras>();
        int anchoNave = bmp.getWidth()/columanSprite;
        int dx=(gameView.getWidth()-anchoNave)/(numeroDeNaves-1);

        int x=0;
        int y= spaceBetweenRows;

        for(int i =0;i<numeroDeNaves;i++){
            filas.add(crearNavesInvasoras(filaFlota,bmp,filaSprite,columanSprite,x,y,puntos));
            x=x+dx;
        }
        spaceBetweenRows = spaceBetweenRows+bmp.getHeight();
        return filas;
    }

    private NavesInvasoras crearNavesInvasoras(int filaFlota, Bitmap bmp, int filaSprite, int columanSprite, int x, int y, int puntos) {
        NavesInvasoras navesInvasoras = new NavesInvasoras(gameView,bmp,filaSprite,columanSprite,x,y,puntos);
        return navesInvasoras;
    }
    public boolean invaderArrive() {
        return arrive;
    }

    public boolean allInvadersDestroyed(){
        boolean flotaDestruida = false;
        if(listaNavesInvasoras.size() == 0){
            flotaDestruida = true;
        }
        return flotaDestruida;


    }
    public Sprite getShooter() {
        Random rnd = new Random();
        int disparoFila = 0;
        int disparoColumna = rnd.nextInt(listaNavesInvasoras.get(disparoFila).size());
        return listaNavesInvasoras.get(disparoFila).get(disparoColumna);
    }

    public boolean isCollision(Sprite goodSpaceShipShoot) {

        for (List<NavesInvasoras> filaFlota: listaNavesInvasoras){
            for (NavesInvasoras sprite : filaFlota) {

                if(sprite.isCollition(goodSpaceShipShoot)){
                    lastDestroyedShipPoints = sprite.getPuntos();
                    filaFlota.remove(sprite);


                    if(filaFlota.size()==0){
                       listaNavesInvasoras.remove(filaFlota);
                    }
                    return true;
                }
            }
        }

        return false;
    }
    public int getPuntos(){
        return lastDestroyedShipPoints;
    }

}
