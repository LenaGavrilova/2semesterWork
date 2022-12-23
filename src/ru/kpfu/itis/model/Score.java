package ru.kpfu.itis.model;

import java.awt.*;

public class Score {
    public static int GAME_WIDTH;
    public static int GAME_HEIGHT;

    public void setPlayer1(int player1) {
        this.player1 = player1;
    }

    public void setPlayer2(int player2) {
        this.player2 = player2;
    }

    public int player1;
    public int player2;

    public Score(int gameWidth, int gameHeight) {
        Score.GAME_WIDTH = gameWidth;
        Score.GAME_HEIGHT = gameHeight;
    }

    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.setFont(new Font("Consolas", Font.PLAIN, 60));
        g.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);
        g.drawString(String.valueOf(player1), (GAME_WIDTH/2) - 85 , 50);
        g.drawString(String.valueOf(player2), (GAME_WIDTH/2) + 20 , 50);
    }
}
