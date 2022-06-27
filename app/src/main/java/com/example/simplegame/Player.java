package com.example.simplegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

/*
 * player is the main character of the game, which user can control with a
 * touch joystick.
 */


public class Player extends Circle {

    private double radius;
    private double maxSpeed;

    private Joystick joystick;
    private Paint paint;

    public Player(Context context, Joystick joystick, double radius, double maxSpeed, double positionX, double positionY){
        super(ContextCompat.getColor(context, R.color.player),radius, positionX, positionY);

        this.joystick = joystick;
        /*
         * initial the max speed by diving it with
         * MAX_UPS from the gameLoop class in order to solve
         * slower-faster system speed gap
         */
        this.maxSpeed = maxSpeed/GameLoop.MAX_UPS;
    }

    public void update() {
        // Update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX() * maxSpeed;
        velocityY = joystick.getActuatorY() * maxSpeed;

        //Update position
        positionX += velocityX;
        positionY += velocityY;
    }

    public void setPosition(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
}
