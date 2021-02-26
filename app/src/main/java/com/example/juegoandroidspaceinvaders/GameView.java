package com.example.juegoandroidspaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import android.view.CollapsibleActionView;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import androidx.annotation.NonNull;

public class GameView extends SurfaceView {
    public MainActivity activity;
    private Bitmap navePrincipal, fondoJuego, disparoNave, disparoInvasor, explosion,fondoGameOver;
    private SurfaceHolder holder;
    public GameLoopThread gameLoopThread;
    private Context contexto;
    private Nave nave;
    private FlotaNaves flotaNaves;
    private int x = 0;
    private Disparo dNave;
    private Disparo iNave;
    private Explosion ex;
    private int estadoJuego = 1;
    private long ultimoClik;
    private Paint pincelPuntos=new Paint();
    private int puntuacion = 0;


    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;
    private int soundId,soundExplosion;


    public GameView(Context context, MainActivity activity) {
        super(context);
        this.contexto = context;
        this.activity = activity;

        //Configuracion de la musica de fondo

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.music);
            mediaPlayer.setLooping(true);
        }

        //Cargar Sonidos

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load(getContext(), R.raw.puaj, 1);
        soundExplosion = soundPool.load(getContext(),R.raw.playerexplode,1);
        gameLoopThread = new GameLoopThread();

        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                gameLoopThread.setView(GameView.this);
                fondoJuego = BitmapFactory.decodeResource(getResources(), R.drawable.background);
                fondoGameOver = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
                explosion = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
                disparoNave = BitmapFactory.decodeResource(getResources(),R.drawable.goodspaceshipshoot);
                disparoInvasor = BitmapFactory.decodeResource(getResources(), R.drawable.invaderspaceshipshoot);
                crearNave();
                crearFlotaNaves();
                crearDisparoNave(false);
                crearDisparoInvasores(false);
                crearExplosion(false,false);
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }


            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
    }




    private void crearExplosion(boolean vivo, boolean naveInvasora) {
        int colunmasSprite=25,filasSprite=1;
        int x,y;
        if(naveInvasora){
            x = dNave.getX()+dNave.getAncho()/2 - (explosion.getWidth()/colunmasSprite)/2;
            y = dNave.getY()-dNave.getAlto()/2;
        }else{
            x= nave.getX();
            y=nave.getY();
        }
        ex=new Explosion(this,explosion,filasSprite,colunmasSprite,x,y,vivo);

    }

    private void crearDisparoInvasores(boolean vivo) {
        int colunmasSprite=9,filasSprite=1;
        Sprite disparoNave = flotaNaves.getShooter();
        int x = disparoNave.getX()+disparoNave.getAncho()/2 - (disparoInvasor.getWidth()/colunmasSprite) / 2;;
        int y = disparoNave.getY();
        this.iNave = new Disparo(this,disparoInvasor,filasSprite,colunmasSprite,x,y,0,true,vivo);
    }
    private void crearNave() {
        navePrincipal = BitmapFactory.decodeResource(getResources(), R.drawable.goodspaceship);
        this.nave = new Nave(this, navePrincipal, 1, 13, contexto);
    }

    private void crearFlotaNaves() {
        int alturaAterrizaje = nave.getAlto();
        this.flotaNaves = new FlotaNaves(this, this.getHeight() - alturaAterrizaje);
    }

    private void crearDisparoNave(boolean vivo) {
        int columnasSprite=2,filasSprite=1;
        int x = nave.getX()+nave.getAncho()/2 - (disparoNave.getWidth()/columnasSprite)/2;
        int y = nave.getY();
        int xSpeed = nave.getxSpeed();
        this.dNave= new Disparo(this,disparoNave,filasSprite,columnasSprite,x,y,xSpeed,false,vivo);
    }

    private void comprobarColisiones(){
        //Colision entre un disparo de la nave hacia una nave de la flota

        if(dNave.isVivo()){
            if(flotaNaves.isCollision(this.dNave)){
                this.puntuacion +=flotaNaves.getPuntos();
                dNave.setVivo(false);
                crearExplosion(true,true);
                soundPool.play(soundExplosion,1,1,0,0,1);
            }
        }

        //Colision entre un disparo de la nave y un disparo de la flota

        if(dNave.isVivo() && iNave.isVivo()){
            if(iNave.isCollition(this.dNave)){
                this.iNave.setVivo(false);
                this.dNave.setVivo(false);
                crearExplosion(true,true);
                soundPool.play(soundExplosion,1,1,0,0,1);
            }
        }
        //Colision entre la nave y un disparo de la flota
        if(iNave.getAbajo()>nave.getY() && iNave.isVivo()){
            if(nave.isCollition(this.iNave)){
                this.iNave.setVivo(false);
                this.nave.setVivo(false);
                crearExplosion(true,false);
                soundPool.play(soundExplosion,1,1,0,0,1);
            }
        }

    }
    private void pintarPuntos(Canvas canvas){
        if(gameLoopThread.tiempo<20){
            pincelPuntos.setColor(Color.RED);
        }else{
            pincelPuntos.setColor(Color.WHITE);
        }

        pincelPuntos.setTextSize(40);
        canvas.drawText("Puntos: "+puntuacion+" Tiempo de juego: "+gameLoopThread.tiempo,0,40,pincelPuntos);
    }

    private void pintar(Canvas canvas) {

        if (this.nave.isVivo()) {
            nave.onDraw(canvas);
        }

        this.flotaNaves.onDraw(canvas);

        if(this.dNave.isVivo()){
            this.dNave.onDraw(canvas);
        }

        if(this.iNave.isVivo()){
            this.iNave.onDraw(canvas);
        }else{
            if(Math.random()<0.1){
                crearDisparoInvasores(true);
            }
        }

        pintarPuntos(canvas);

        comprobarColisiones();


        if(this.ex.isVivo()){
            this.ex.onDraw(canvas);
        }

        //Si las naves invasoras llegan al final o si la nave es destruida

        if(flotaNaves.invaderArrive() || !nave.isVivo() || gameLoopThread.tiempo==0){
            estadoJuego = 2;

        }

        //Si todos los invasores se han destruido volvemos a empezar
        if(flotaNaves.allInvadersDestroyed()){
            estadoJuego = 3;
            
        }


    }

    private void mostrarGameOver(Canvas canvas) {
        Typeface typeface = Typeface.create("Helvetica",Typeface.BOLD);
        Paint pincel = new Paint();
        pincel.setColor(Color.WHITE);
        pincel.setTextSize(60);
        pincel.setTypeface(typeface);
        canvas.drawColor(Color.BLACK);
        Drawable gameover = contexto.getResources().getDrawable(R.drawable.gameover);
        gameover.setBounds(0,this.getHeight()/2-600,this.getWidth(),this.getHeight()/2);
        gameover.draw(canvas);
        canvas.drawText("TOCA PARA REINICIAR",canvas.getWidth()/2-300,1100,pincel);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap escalado = Bitmap.createScaledBitmap(fondoJuego, canvas.getWidth(), canvas.getHeight(), false);
        canvas.drawBitmap(escalado, 0, 0, null);
        switch (estadoJuego){
            case 1:
                pintar(canvas);
                break;
            case 2:
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                mostrarGameOver(canvas);
                break;
            case 3:
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        
                    }
                });
                mostrarPantallaGanadora(canvas);
                break;

        }

    }

    private void mostrarPantallaGanadora(Canvas canvas) {
        Typeface typeface = Typeface.create("Helvetica",Typeface.BOLD);
        Paint pincel = new Paint();
        pincel.setColor(Color.WHITE);
        pincel.setTextSize(60);
        pincel.setTypeface(typeface);
        canvas.drawColor(Color.BLACK);
        Drawable gameWin = contexto.getResources().getDrawable(R.drawable.win);
        gameWin.setBounds(0,this.getHeight()/2-600,this.getWidth(),this.getHeight()/2);
        gameWin.draw(canvas);
        canvas.drawText("PUNTUACION : "+puntuacion,canvas.getWidth()/2-200,1000,pincel);
        canvas.drawText("TOCA PARA REINICIAR",canvas.getWidth()/2-300,1100,pincel);

    }


    //Funciones para la musica

    public void iniciarMusica() {
        mediaPlayer.start();
    }

    public void liberarReproductor() {
        mediaPlayer.release();
        soundPool.release();
    }

    public void pararMusica() {
        mediaPlayer.stop();
    }

    public void pausarMusica() {
        mediaPlayer.pause();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if((System.currentTimeMillis() - ultimoClik > 300 )){
            ultimoClik = System.currentTimeMillis();
            synchronized (getHolder()){
                switch (estadoJuego){
                    case 1:
                        if(!dNave.isVivo()){
                            crearDisparoNave(true);
                            soundPool.play(soundId,1,1,0,0,1);
                        }
                        break;
                    case 2: case 3:
                        estadoJuego = 1;
                        puntuacion = 0;
                        crearNave();
                        crearFlotaNaves();
                        crearDisparoNave(false);
                        crearDisparoInvasores(false);
                        gameLoopThread.tiempo=80;
                        break;

                }
            }
        }

        return true;
    }
}
