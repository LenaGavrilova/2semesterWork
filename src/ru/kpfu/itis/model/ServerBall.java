package ru.kpfu.itis.model;

import ru.kpfu.itis.game.Game;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ServerBall implements Ball {
    public int x = 300;
    public int y = 400;
    public final int RADIUS = 8;
    public int xMove;
    public int yMove;
    public final int SPEED = -1;
    private int width;
    private int height;

    private Racket racket1;
    private Racket racket2;
    public Game game;
    private Score score;

    public ServerBall(Game game, int width, int height, Score score) {
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

        y += yMove;

        if (y < 0 || y > height || border.intersects(r1) || border.intersects(r2)) {
            yMove *= SPEED;
            y += yMove;
        }

        if (x < 0) {
            xMove *= SPEED;
            x += xMove;
            score.player2++;
        }
        if (x > width - RADIUS * 2) {
            xMove *= SPEED;
            x += xMove;
            score.player1++;
        }

//        if(score.player2 == 5) {
//            try{
//                game.getServer().getOut().writeUTF("Winner " + game.namePlayer2);
//            } catch (IOException ex){
//                throw new IllegalArgumentException(ex);
//            }
//        }

//        if(score.player1 == 5) {
//            try{
//                game.getServer().getOut().writeUTF("Winner " + game.namePlayer1);
//            } catch (IOException ex){
//                throw new IllegalArgumentException(ex);
//            }
//        }


        try {
            game.getServer().getOut().writeUTF("Ball " + x + " " + y);
            game.getServer().getOut().writeUTF("Score " + score.player1 + " " + score.player2);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    @Override
    public void draw(Graphics graphics, Color c) {
        graphics.setColor(c);
        graphics.fillOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }
}
