package ru.kpfu.itis.game;

import ru.kpfu.itis.controller.KeyManager;
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

    private KeyManager keyManager;

    private Racket racket1;
    private Racket racket2;
    private ServerBall serverBall;
    private ClientBall clientBall;

    private int status = -1;
    public boolean paused;

    public Game (String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void tick() {
        keyManager.tick();
        if (status == 1) {
            serverBall.tick();
        } else {
            clientBall.tick();
        }
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
        keyManager = new KeyManager(this);
        gameFrame.getFrame().addKeyListener(keyManager);

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
                break;
            case (1):
                status = 1;
                paused = true;
                break;
            case (0):
                status = 0;
                paused = false;
                break;
        }
    }
    public Racket getRacket() {
        if (status == 1) {
            return getRacket1();
        } else {
            return getRacket2();
        }
    }

    public Racket getRacket1() {
        return racket1;
    }

    public Racket getRacket2() {
        return racket2;
    }


}