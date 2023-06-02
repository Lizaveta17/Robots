package entity;

import logic.MathLogic;

import java.awt.*;

public class UserRobot extends Robot{
    public UserRobot(double x, double y, double direction, double velocity, Color bodyColor, int widthDiam, int heightDiam) {
        super(x, y, direction, velocity, bodyColor, widthDiam, heightDiam);
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

}
