package entity;

import java.awt.*;

public class ColorTarget extends Target{
    public final Color color;
    public ColorTarget(int x, int y, int diam, Color color) {
        super(x, y, diam);
        this.color = color;
    }
}
