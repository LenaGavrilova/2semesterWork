package ru.kpfu.itis.view;

import java.awt.*;

public class Racket {
    public int x, y, racketWidth, racketHeight, ySpeed;
    private int screenHeight;
    private int speed = 10;

    public Racket(int x, int y, int racketWidth, int racketHeight, int screenHeight) {
        this.x = x;
        this.y = y;
        this.racketWidth = racketWidth;
        this.racketHeight = racketHeight;
        this.screenHeight = screenHeight;
    }


    public void render(Graphics g) {
        //сделать чтобы клиент передвал цвет
        g.setColor(Color.blue);
        g.fillRect(x, y, racketWidth, racketHeight);
    }

    public void moveUp() {
        if (y - speed >= 0) {
//            y -= 10;
            setY(-speed);
            move();
        }
    }

    public  void moveDown() {
        if (y + speed + racketHeight <= screenHeight) {
//            y += 10;
            setY(speed);
            move();
        }
    }

    public void setY(int y) {
        ySpeed = y;
    }

    public void move(){
        y = y + ySpeed;
    }
}
