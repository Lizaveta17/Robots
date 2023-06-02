package entity;

import logic.MathLogic;

import java.awt.*;

public class Robot{
    public volatile double x;
    public volatile double y;
    public volatile double direction;
    public volatile double velocity;
    public final Color bodyColor;
    public final int widthDiam;
    public final int heightDiam;

    public Robot(double x, double y, double direction, double velocity, Color bodyColor, int widthDiam, int heightDiam){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.velocity = velocity;
        this.bodyColor = bodyColor;
        this.widthDiam = widthDiam;
        this.heightDiam = heightDiam;
    }

    public int getRoundedX(){
        return MathLogic.round(x);
    }
    public int getRoundedY(){
        return MathLogic.round(y);
    }
}

