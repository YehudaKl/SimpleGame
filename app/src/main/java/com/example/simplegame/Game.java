package com.example.simplegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.simplegame.object.Circle;
import com.example.simplegame.object.Enemy;
import com.example.simplegame.object.Player;
import com.example.simplegame.object.Spell;

/**
 * Game maneges all objects in the game and is responsible for updating all states and render all objects to the screen
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private final Player player;
    private final Joystick joystick;
    private GameLoop gameLoop;
    private Context context;
    private List<Enemy> enemyList;
    private List<Spell> spellList;
    private int joystickPointerId;
    private int numberOfSpellsToCast;

    // General settings
    public final int MAX_UPS = 30;

    // Player settings
    private final int PLAYER_INITIAL_X = 500;
    private final int PLAYER_INITIAL_Y = 500;
    private final int PLAYER_RADIUS = 30;
    private final int PLAYER_MAX_SPEED = 400;

    //Spell settings
    private final int SPELL_MAX_SPEED = 800;
    private final int SPELL_RADIUS = 30;

    // Enemy settings
    private final int ENEMY_RADIUS = 30;
        // the factor is multiplied by the player max speed when specifying the max speed to the enemy
    private final float ENEMY_SPEED_DECREASE_FACTOR = 0.5f;

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
        this.enemyList = new ArrayList<Enemy>();
        this.spellList = new ArrayList<Spell>();
        this.joystickPointerId = 0;
        setFocusable(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        //Handle touch event actions
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(joystick.getIsPressed()){
                    numberOfSpellsToCast++;
                }
                else if(joystick.isPressed((double)event.getX(), (double)event.getY())){
                    // Joystick is pressed in this event -> setIsPressed(true) and store ID
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                }
                else{
                    // Joystick was not pressed previously and is not pressed at this event -> cast spell
                    numberOfSpellsToCast++;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                // Joystick was pressed previously and now is moved
                if(joystick.getIsPressed()){
                    joystick.setActuator((double)event.getX(), (double)event.getY());
                    return true;
                }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerId == event.getPointerId(event.getActionIndex())) {
                    // Joystick was let got of -> setIsPressed(false) and resetActuator
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                    return true;
                }

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

        for(Enemy enemy: enemyList){
            enemy.draw(canvas);
        }

        for(Spell spell: spellList){
            spell.draw(canvas);
        }







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

        // Spawn enemy when it is time to spawn new one
        if(Enemy.readyToSpawn()){
            enemyList.add(new Enemy(context, player, PLAYER_RADIUS, PLAYER_MAX_SPEED * ENEMY_SPEED_DECREASE_FACTOR));
        }

        // Updating enemies
        for(Enemy enemy: enemyList){
            enemy.update();
        }

        // Adding the spells to the spellList one iteration in
        // order to prevent a crash
        while (numberOfSpellsToCast > 0){
            spellList.add(new Spell(context, player, SPELL_RADIUS, SPELL_MAX_SPEED));
            numberOfSpellsToCast--;
        }
        // Updating spells
        for(Spell spell: spellList){
            spell.update();
        }

        // Iterate through enemyList and check for collision between each enemy
        // and player and all spells
        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while(iteratorEnemy.hasNext()){
            // Checking for collision with the player
            Enemy enemy = iteratorEnemy.next();
            if(Circle.isColliding(enemy, player)){
                iteratorEnemy.remove();
                // Decrease player health
            }
            // Iterating the spells
            Iterator<Spell> iteratorSpell = spellList.iterator();
            while(iteratorSpell.hasNext()){
                Circle spell = iteratorSpell.next();
                if(Circle.isColliding(enemy, spell)){
                    iteratorEnemy.remove();
                    iteratorSpell.remove();

                }
            }
        }


    }
}
