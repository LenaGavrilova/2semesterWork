package ru.kpfu.itis.gui.entities;

import java.awt.*;

public class ClientBall implements Ball {
    private int x;
    private int y;
    private final int RADIUS = 8;

    @Override
    public void redraw() {
    }

    @Override
    public void draw(Graphics graphics, Color c) {
        graphics.setColor(c);
        graphics.fillOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRADIUS() {
        return RADIUS;
    }
}
