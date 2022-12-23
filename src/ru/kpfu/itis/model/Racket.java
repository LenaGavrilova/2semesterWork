package ru.kpfu.itis.model;

import java.awt.*;

public class Racket {
    public int x;
    public int y;
    public int racketWidth;
    public int racketHeight;
    public int ySpeed;
    private int screenHeight;
    private final int SPEED = 10;

    public Racket(int x, int y, int racketWidth, int racketHeight, int screenHeight) {
        this.x = x;
        this.y = y;
        this.racketWidth = racketWidth;
        this.racketHeight = racketHeight;
        this.screenHeight = screenHeight;
    }


    public void draw(Graphics g, Color c) {
        //сделать чтобы клиент передвал цвет
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
        if (y + SPEED + racketHeight <= screenHeight) {
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
