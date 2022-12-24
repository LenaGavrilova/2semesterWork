package ru.kpfu.itis.gui.entities;

import java.awt.*;

public class Racket {
    public int x;
    public int y;
    public int racketWidth;
    public int racketHeight;
    private int ySpeed;
    private final int SCREEN_HEIGHT;
    private final int SPEED = 10;

    public Racket(int x, int y, int racketWidth, int racketHeight, int screenHeight) {
        this.x = x;
        this.y = y;
        this.racketWidth = racketWidth;
        this.racketHeight = racketHeight;
        this.SCREEN_HEIGHT = screenHeight;
    }

    public void draw(Graphics g, Color c) {
        g.setColor(c);
        g.fillRect(x, y, racketWidth, racketHeight);
    }

    public void moveUp() {
        if (y - SPEED >= 0) {
            setY(-SPEED);
            move();
        }
    }

    public  void moveDown() {
        if (y + SPEED + racketHeight <= SCREEN_HEIGHT) {
            setY(SPEED);
            move();
        }
    }

    public void setY(int y) {
        ySpeed = y;
    }

    public void move(){
        y += ySpeed;
    }
}
