package com.example.juegoandroidspaceinvaders;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class GameLoopThread extends Thread {
    private static final long FPS = 20;
    private GameView view;
    private boolean running = false;

    public int tiempo = 80;
    public int milisecond=0;
    public boolean pause;

    public GameLoopThread(GameView view) {
        this.view = view;
    }

    public GameLoopThread() {
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        long ticksPS = 1000/FPS;
        long startTime;
        long sleepTime;

        while(running){
            Canvas canvas = null;
            startTime=System.currentTimeMillis();

            try {
                canvas = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    if(!pause){
                        view.onDraw(canvas);
                    }


                }
            }
            finally {
                if (canvas != null) {
                    view.getHolder().unlockCanvasAndPost(canvas);
                }
            }



            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);

            try {
                if (sleepTime > 0){
                    sleep(sleepTime);
                    milisecond+=100;

                }else{
                    sleep(10);
                    milisecond+=100;
                    if(milisecond==1000){
                        tiempo--;
                        milisecond=0;
                    }


                }

            }
            catch (Exception e) {}




        }
    }



}
