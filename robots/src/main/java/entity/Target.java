package entity;

import java.awt.*;

public class Target {
    public volatile int x;
    public volatile int y;
    public volatile int diam;
    public final int radius;

    public Target(int x, int y, int diam) {
        this.x = x;
        this.y = y;
        this.diam = diam;
        radius = diam / 2;
    }
}
