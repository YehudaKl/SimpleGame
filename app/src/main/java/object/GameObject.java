package object;

import android.graphics.Canvas;

public abstract class GameObject {

    protected double positionX;
    protected double positionY;
    protected double velocityX;
    protected double velocityY;

    public GameObject(double positionX, double positionY){
        this.positionX = positionX;
        this.positionY = positionY;

        this.velocityX = 0;
        this.velocityY = 0;
    }

    protected static double distanceBetweenGameObjects(GameObject obj1, GameObject obj2) {
        return Math.sqrt(
                Math.pow(obj1.getPositionX() - obj2.getPositionX(), 2)+
                Math.pow(obj1.getPositionY() - obj2.getPositionY(), 2)
        );
    }

    public double getPositionX(){
        return positionX;
    }
    public double getPositionY(){
        return positionY;
    }
    public void setPosition(double positionX, double positionY){
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public abstract void draw(Canvas canvas);
    public abstract void update();



}
