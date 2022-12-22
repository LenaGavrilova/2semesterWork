package ru.kpfu.itis.game;

import ru.kpfu.itis.model.ClientBall;
import ru.kpfu.itis.model.Racket;
import ru.kpfu.itis.model.ServerBall;
import ru.kpfu.itis.view.GameFrame;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.*;

public class Game{

    private GameFrame gameFrame;
    private final String title;
    private final int width;
    private final int height;
    private BufferStrategy bufferStrategy;
    private Graphics graphics;

    private boolean isRunning;

    Racket racket1;
    Racket racket2;
    ServerBall serverBall;
    ClientBall clientBall;

    private int status = -1;

    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void draw() {
        bufferStrategy = gameFrame.getCanvas().getBufferStrategy();

        if (bufferStrategy == null) {
            gameFrame.getCanvas().createBufferStrategy(3);
            return;
        }

        graphics = bufferStrategy.getDrawGraphics();

        graphics.clearRect(0, 0, width, height);

        racket1.draw(graphics);
        racket2.draw(graphics);
        serverBall.draw(graphics);
        clientBall.draw(graphics);

        if(status==1){
            serverBall.draw(graphics);
        } else{
            clientBall.draw(graphics);}

        bufferStrategy.show();
        graphics.dispose();

    }

    public void init() {

        gameFrame = new GameFrame(title, width, height);

        racket1 = new Racket(0, 0, 25, 200, height);
        racket2 = new Racket(775, 0, 25, 200, height);
        clientBall = new ClientBall();
        serverBall = new ServerBall(racket1,racket2,width,height);

        String[] options = {"Player 1 ","Player 2"};
        int welcomeOption = JOptionPane.showOptionDialog(gameFrame.getFrame(),
                "Please choose your role",
                "Ping-pong game",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (welcomeOption){
            case (-1):
                System.exit(0);
            case (1):
                status = 1;
            case (0):
                status=0;
        }

    }

}