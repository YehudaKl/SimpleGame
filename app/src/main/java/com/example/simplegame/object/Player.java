package com.example.simplegame.object;

import android.content.Context;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.simplegame.GameLoop;
import com.example.simplegame.Joystick;
import com.example.simplegame.R;
import com.example.simplegame.Utils;

/*
 * player is the main character of the game, which user can control with a
 * touch joystick.
 */


public class Player extends Circle {

    private final double maxSpeed;

    private Joystick joystick;

    public Player(Context context, Joystick joystick, double radius, double maxSpeed, double positionX, double positionY){
        super(ContextCompat.getColor(context, R.color.player),radius, positionX, positionY);

        this.joystick = joystick;
        /*
         * initial the max speed by diving it with
         * MAX_UPS from the gameLoop class in order to solve
         * slower-faster system speed gap
         */
        this.maxSpeed = maxSpeed/ GameLoop.MAX_UPS;
    }

    public void update() {

        // Update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX() * maxSpeed;
        velocityY = joystick.getActuatorY() * maxSpeed;

        //Update position
        positionX += velocityX;
        positionY += velocityY;

        //Update direction
        if(velocityX != 0 || velocityY != 0){
            // Normalize the vector
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = velocityX/distance;
            directionY = velocityY/distance;
        }





    }

}
