package object;


import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.simplegame.GameLoop;
import com.example.simplegame.R;

/**
 * Enemy is a character which always moves in the direction of the player.
 */

public class Enemy extends Circle {

    private double maxSpeed;
    private final Player player;
    public Enemy(Context context, Player player, double radius, double maxSpeed, double positionX, double positionY) {
        super(ContextCompat.getColor(context, R.color.enemy), radius, positionX, positionY);

        this.maxSpeed = maxSpeed/ GameLoop.MAX_UPS;
        this.player = player;
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

}
