package entity;

import java.awt.*;

public class Target {
    public volatile int x;
    public volatile int y;
    public final Color color;

    private final int diam = 12;

    public Target(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getDiam(){
        return diam;
    };

}
