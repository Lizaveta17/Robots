package entity;

import logic.MathLogic;

import java.awt.*;

public class UserRobot extends Robot{
    public final double velocityDiff;
    private final double minVelocity = 1;
    public UserRobot(
            double x, double y, double direction, double velocity, Color bodyColor, int widthDiam, int heightDiam, double velocityDiff
    ) {
        super(x, y, direction, velocity, bodyColor, widthDiam, heightDiam);
        this.velocityDiff = velocityDiff;
    }

    public void goLeft(){
        double newX = x - velocity;
        x = newX;
    }
    public void goRight(){
        double newX = x + velocity;
        x = newX;
    }
    public void goUp(){
        double newY = y - velocity;
        y = newY;
    }
    public void goDown(){
        double newY = y + velocity;
        y = newY;

    }
    public void increaseVelocity(){
        double newVelocity = velocity + velocityDiff;
        velocity = newVelocity;
    }

    public void decreaseVelocity(){
        double newVelocity = velocity - velocityDiff;
        if (newVelocity >= minVelocity){
            velocity = newVelocity;
        }
    }
}
