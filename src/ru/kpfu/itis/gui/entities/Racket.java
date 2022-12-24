package ru.kpfu.itis.gui.entities;

import java.awt.*;

public class Racket {
    private int x;
    private int y;
    private int racketWidth;
    private int racketHeight;
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

    public void move(){
        y += ySpeed;
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
        ySpeed = y;
    }

    public int getRacketWidth() {
        return racketWidth;
    }

    public void setRacketWidth(int racketWidth) {
        this.racketWidth = racketWidth;
    }

    public int getRacketHeight() {
        return racketHeight;
    }

    public void setRacketHeight(int racketHeight) {
        this.racketHeight = racketHeight;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }

    public int getSPEED() {
        return SPEED;
    }
}
