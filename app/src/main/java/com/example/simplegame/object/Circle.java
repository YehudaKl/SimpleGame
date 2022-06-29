package com.example.simplegame.object;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Circle extends GameObject {

    protected double radius;
    protected Paint paint;

    // Collision detection for circles
    public static boolean isColliding(Circle circle1 , Circle circle2) {
        double distance = distanceBetweenGameObjects(circle1, circle2);
        return (distance < circle1.radius + circle2.radius);
    }

    public Circle(int color, double radius, double positionX, double positionY) {
        super(positionX, positionY);

        this.radius = radius;
        this.paint = new Paint();

        paint.setColor(color);
    }



    public void draw(Canvas canvas){
        canvas.drawCircle((float) positionX, (float) positionY, (float) radius, paint);
    }




}
