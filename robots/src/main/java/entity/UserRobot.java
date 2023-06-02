package entity;

import java.awt.*;

public class UserRobot extends Robot{
    public UserRobot(double x, double y, double direction, double velocity, Color bodyColor, int widthDiam, int heightDiam) {
        super(x, y, direction, velocity, bodyColor, widthDiam, heightDiam);
    }

    public void goLeft(){
        double newX = positionX - velocity;
        positionX = newX;
    }
    public void goRight(){
        double newX = positionX + velocity;
        positionX = newX;
    }
    public void goUp(){
        double newY = positionY - velocity;
        positionY = newY;
    }
    public void goDown(){
        double newY = positionY + velocity;
        positionY = newY;

    }
}
