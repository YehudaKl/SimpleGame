package com.example.simplegame.object;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.simplegame.GameLoop;
import com.example.simplegame.R;

public class Spell extends Circle{


    private final Player spellCaster;

    private final double maxSpeed;

    public Spell(Context context, Player spellCaster, int radius, double maxSpeed){
        super(ContextCompat.getColor(context, R.color.spell),
                radius,
                spellCaster.getPositionX(),
                spellCaster.getPositionY()
        );

        this.spellCaster = spellCaster;
        this.maxSpeed = maxSpeed/GameLoop.MAX_UPS;


        velocityX = spellCaster.getDirectionX()*this.maxSpeed;
        velocityY = spellCaster.getDirectionY()*this.maxSpeed;

    }

    @Override
    public void update(){
        positionX += velocityX;
        positionY += velocityY;
    }




}
