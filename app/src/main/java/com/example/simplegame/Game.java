package com.example.simplegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * Game maneges all objects in the game and is responsible for updating all states and render all objects to the screen
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoop gameLoop;
    private Context context;
    private final Player player;
    private final Joystick joystick;
    private final Enemy enemy;

    // Player settings
    private final int PLAYER_INITIAL_X = 500;
    private final int PLAYER_INITIAL_Y = 500;
    private final int PLAYER_RADIUS = 30;
    private final int PLAYER_MAX_SPEED = 400;

    // Joystick settings
    private final int JOYSTICK_POSITION_X = 150;
    private final int JOYSTICK_POSITION_Y = 950;
    private final int JOYSTICK_OUTER_RADIUS = 70;
    private final int JOYSTICK_INNER_RADIUS = 40;

    public Game(Context context) {
        super(context);

        //Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.gameLoop = new GameLoop(this, surfaceHolder);
        this.context = context;
        this.joystick = new Joystick(JOYSTICK_POSITION_X, JOYSTICK_POSITION_Y, JOYSTICK_OUTER_RADIUS, JOYSTICK_INNER_RADIUS);
        this.player = new Player(getContext(), joystick, PLAYER_RADIUS, PLAYER_MAX_SPEED, PLAYER_INITIAL_X, PLAYER_INITIAL_Y);
        this.enemy = new Enemy();

        setFocusable(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        //Handle touch event actions
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(joystick.isPressed((double)event.getX(), (double)event.getY())){
                    joystick.setIsPressed(true);
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()){
                    joystick.setActuator((double)event.getX(), (double)event.getY());
                    return true;
                }
            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
                return true;

        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);

        player.draw(canvas);
        joystick.draw(canvas);
    }

    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(gameLoop.getAverageUPS());

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.mangeta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);

    }

    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameLoop.getAverageFPS());

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.mangeta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);

    }

    public void update(){
        // Update the sate
        player.update();
        joystick.update();
    }
}
