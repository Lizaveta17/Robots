package entity;

import logic.MathLogic;

import java.awt.*;

public class ComputerRobot extends Robot{
    public final int headDiam;
    public ComputerRobot(double x, double y, double direction, double velocity, Color bodyColor, int widthDiam, int heightDiam, int headDiam) {
        super(x, y, direction, velocity, bodyColor, widthDiam, heightDiam);
        this.headDiam = headDiam;
    }

    public void move(){
        double newX = x + velocity * Math.cos(direction);
        double newY = y + velocity * Math.sin(direction);
        x = newX;
        y = newY;

    }

    public void turnToTarget(Target target) {
        double angleToTarget = MathLogic.angleTo(x, y, target.x, target.y);
        direction = angleToTarget;
    }
}
