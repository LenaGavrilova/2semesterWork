package ru.kpfu.itis.model;

import java.awt.*;

public class ServerBall implements Ball{
    private int x = 300;
    private int y = 400;
    private final int radius = 5;
    private int xMove;
    private int yMove;
    private Racket racket1;
    private Racket racket2;
    private final int SPEED = -1;
    private int width;
    private int height;

    public ServerBall(Racket racket1, Racket racket2, int width, int height) {
        this.racket1 = racket1;
        this.racket2 = racket2;
        this.width = width;
        this.height = height;
        xMove = 7;
        yMove = 7;
    }

    @Override
    public void tick() {

        Rectangle r1 = new Rectangle(racket1.x, racket1.y, racket1.racketWidth, racket1.racketHeight);
        Rectangle r2 = new Rectangle(racket2.x, racket2.y, racket2.racketWidth, racket2.racketHeight);

        x += xMove;

        Rectangle border = new Rectangle(x - radius, y - radius, 2 * radius, 2 * radius);

        if (x < 0 || x > width || border.intersects(r1) || border.intersects(r2)) {
            xMove *= SPEED;
            x += xMove;
        }

        y += yMove;

        if (y < 0 || y > height || border.intersects(r1) || border.intersects(r2)) {
            yMove *= SPEED;
            y += yMove;
        }
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }
}
