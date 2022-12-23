package ru.kpfu.itis.model;

import ru.kpfu.itis.game.Game;

import java.awt.*;
import java.io.IOException;

public class ServerBall implements Ball{
    private int x = 300;
    private int y = 400;
    private final int RADIUS = 8;
    private int xMove;
    private int yMove;
    private final int SPEED = -1;
    private int width;
    private int height;

    private Racket racket1;
    private Racket racket2;
    public Game game;
    private Score score;

    public ServerBall(Game game,int width,int height,Score score) {
        this.game = game;
        this.racket1 = game.getRacket1();
        this.racket2 = game.getRacket2();
        xMove = 7;
        yMove = 7;
        this.width = width;
        this.height = height;
        this.score = score;
    }

    @Override
    public void tick() {

        Rectangle r1 = new Rectangle(racket1.x, racket1.y, racket1.racketWidth, racket1.racketHeight);
        Rectangle r2 = new Rectangle(racket2.x, racket2.y, racket2.racketWidth, racket2.racketHeight);

        x += xMove;

        Rectangle border = new Rectangle(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);

        if (border.intersects(r1) || border.intersects(r2)) {
            xMove *= SPEED;
            x += xMove;
        }
        if (x < 0 ) {
            xMove *= SPEED;
            x += xMove;
            score.player2++;
        }
        if(x > width - RADIUS * 2){
            xMove *= SPEED;
            x += xMove;
            score.player1++;
        }

        y += yMove;

        if ( y < 0 || y > height || border.intersects(r1) || border.intersects(r2)) {
            yMove *= SPEED;
            y += yMove;
        }
        try {
            game.getServer().getOut().writeUTF("Ball "+ x +" "+ y + "\n" );
        }catch (IOException e){
            throw new IllegalArgumentException(e);
        }

    }

    @Override
    public void draw(Graphics graphics, Color c) {
        graphics.setColor(c);
        graphics.fillOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }
}
