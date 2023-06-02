package models;

import java.awt.*;

public class ComputerRobot extends Robot{
    public final int headDiam;
    public ComputerRobot(double x, double y, double direction, double velocity, Color bodyColor, int widthDiam, int heightDiam, int headDiam) {
        super(x, y, direction, velocity, bodyColor, widthDiam, heightDiam);
        this.headDiam = headDiam;
    }
}
