package com.example.simplegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public abstract class Circle extends GameObject{

    protected double radius;
    protected Paint paint;

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
