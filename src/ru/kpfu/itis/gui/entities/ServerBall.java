package ru.kpfu.itis.gui.entities;

import ru.kpfu.itis.game.Game;
import java.awt.*;
import java.io.IOException;

public class ServerBall implements Ball {
    public int x = 300;
    public int y = 400;
    public final int RADIUS = 8;
    private int xMove = 7;
    private int yMove = 7;
    public final int SPEED = -1;
    private final int WIDTH;
    private final int HEIGHT;
    private final int SCORE_TO_WIN = 4;

     final String WINNER_MESSAGE = "YOU WIN!";
     final String LOSER_MESSAGE = "YOU LOSE!";

    private final Racket clientRacket;
    private final Racket serverRacket;
    public Game game;
    private final Score SCORE;

    public ServerBall(Game game, int width, int height, Score score) {
        this.game = game;
        this.clientRacket = game.getClientRacket();
        this.serverRacket = game.getServerRacket();
        this.WIDTH = width;
        this.HEIGHT = height;
        this.SCORE = score;
    }

    @Override
    public void redraw() {
        Rectangle racket1 = new Rectangle(clientRacket.x, clientRacket.y, clientRacket.racketWidth, clientRacket.racketHeight);
        Rectangle racket2 = new Rectangle(serverRacket.x, serverRacket.y, serverRacket.racketWidth, serverRacket.racketHeight);

        x += xMove;

        Rectangle border = new Rectangle(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);

        if (border.intersects(racket1) || border.intersects(racket2)) {
            xMove *= SPEED;
            x += xMove;
        }

        y += yMove;

        if (y < 0 || y > HEIGHT || border.intersects(racket1) || border.intersects(racket2)) {
            yMove *= SPEED;
            y += yMove;
        }

        if (x < 0) {
            xMove *= SPEED;
            x += xMove;
            SCORE.player2++;
        }

        if (x > WIDTH - RADIUS * 2) {
            xMove *= SPEED;
            x += xMove;
            SCORE.player1++;
        }

        if (SCORE.player1 > SCORE_TO_WIN) {
            try {
                game.gamePaused();
                game.getServer().getOut().writeUTF(WINNER_MESSAGE);
                game.showResult(LOSER_MESSAGE);
            } catch (IOException ex){
                throw new IllegalArgumentException(ex);
            }
        }

        if(SCORE.player2 > SCORE_TO_WIN) {
            try{
                game.gamePaused();
                game.getServer().getOut().writeUTF(LOSER_MESSAGE);
                game.showResult(WINNER_MESSAGE);
            } catch (IOException ex){
                throw new IllegalArgumentException(ex);
            }
        }

        try {
            game.getServer().getOut().writeUTF("Ball " + x + " " + y);
            game.getServer().getOut().writeUTF("Score " + SCORE.player1 + " " + SCORE.player2);
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
