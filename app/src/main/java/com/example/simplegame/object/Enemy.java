package com.example.simplegame.object;


import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.simplegame.GameLoop;
import com.example.simplegame.R;

/**
 * Enemy is a character which always moves in the direction of the player.
 */

public class Enemy extends Circle {

    private static double SPAWNS_PER_MINUTE = 20;
    private static double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE/60.0;
    private static double UPDATES_PER_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SECOND;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;


    private double maxSpeed;
    private final Player player;


    public Enemy(Context context, Player player, double radius, double maxSpeed, double positionX, double positionY) {
        super(ContextCompat.getColor(context, R.color.enemy), radius, positionX, positionY);

        this.maxSpeed = maxSpeed/ GameLoop.MAX_UPS;
        this.player = player;
    }

    /**
     * Constructor for random position spawning

     */
    public Enemy(Context context, Player player, double radius, double maxSpeed){
        super(ContextCompat.getColor(context, R.color.enemy),
                radius,
                Math.random() * 1000,
                Math.random() * 1000
        );
        this.player = player;
        this.maxSpeed = maxSpeed/ GameLoop.MAX_UPS;
    }

    @Override
    public void update() {

        // Calculate vector from enemy to player
        double distanceToPlayerX = player.getPositionX() - positionX;
        double distanceToPlayerY = player.getPositionY() - positionY;

        //Calculate (absolute) distance from enemy to player
        double distanceToPlayer = GameObject.distanceBetweenGameObjects(this, player);

        //Calculate direction from enemy to player
        double directionX = distanceToPlayerX/distanceToPlayer;
        double dircetionY = distanceToPlayerY/distanceToPlayer;



        // Set velocity in the direction from enemy to player
        if(distanceToPlayer > 0){// Avoid division by zero
            velocityX = directionX * maxSpeed;
            velocityY = dircetionY * maxSpeed;
        }
        else{
            velocityX = 0;
            velocityY = 0;
        }

        // Update the position
        positionX += velocityX;
        positionY += velocityY;

    }

    /**
     * readyToSpawn checks if a new enemy should spawn, according to the decided
     * number of spawns per minute (see SPAWNS_PER_MINUTE at top)
     * @return
     */
    public static boolean readyToSpawn(){
        if(updatesUntilNextSpawn <= 0){
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        }
        else{
            updatesUntilNextSpawn--;
            return false;
        }
    }

}
