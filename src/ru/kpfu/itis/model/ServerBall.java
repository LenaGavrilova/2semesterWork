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

        try {
            game.getServer().getOut().writeUTF("Ball " + x + " " + y + "\n");
            game.getServer().getOut().writeUTF("Score " + score.player1 + " " + score.player2 + "\n");
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public void getScore() {
        if (score.player1 == 5 || score.player2 == 5) {
            if (score.player1 == 5) {
                JOptionPane.showMessageDialog(null, "Winner is " + game.namePlayer1);
            } else {
                JOptionPane.showMessageDialog(null, "Winner is " + game.namePlayer2);
            }
            String[] options = {"Continue", "Exit"};
            int winOption = JOptionPane.showOptionDialog(null,
                    "Please choose what you need",
                    "Ping-pong game",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (winOption) {
                case (1):
                    System.exit(0);
                    break;
                case (0):
                    score.player1 = 0;
                    score.player2 = 0;
                    break;
            }
        }
    }

    @Override
    public void draw(Graphics graphics, Color c) {
        graphics.setColor(c);
        graphics.fillOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }
}
