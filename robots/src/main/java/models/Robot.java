package models;

import java.awt.*;

public class Robot {
    public volatile double positionX;
    public volatile double positionY;
    public volatile double direction;
    public volatile double velocity;
    public final Color bodyColor;
    public final int widthDiam;
    public final int heightDiam;

    public Robot(double x, double y, double direction, double velocity, Color bodyColor, int widthDiam, int heightDiam){
        positionX = x;
        positionY = y;
        this.direction = direction;
        this.velocity = velocity;
        this.bodyColor = bodyColor;
        this.widthDiam = widthDiam;
        this.heightDiam = heightDiam;
    }
}

