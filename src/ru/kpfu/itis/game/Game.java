package ru.kpfu.itis.game;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import ru.kpfu.itis.controller.KeyManager;
import ru.kpfu.itis.model.ClientBall;
import ru.kpfu.itis.model.Racket;
import ru.kpfu.itis.model.Score;
import ru.kpfu.itis.model.ServerBall;
import ru.kpfu.itis.networkInteraction.Client;
import ru.kpfu.itis.networkInteraction.Server;
import ru.kpfu.itis.view.GameFrame;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;

public class Game implements Runnable {
    private final int PORT = 86;


    public String namePlayer2;
    public String namePlayer1;

    public Color color1;
    public Color color2;

    private GameFrame gameFrame;
    private final String title;
    private final int width;
    private final int height;
    private BufferStrategy bufferStrategy;
    private Graphics graphics;

    public KeyManager keyManager;

    public Racket racket1;
    public Racket racket2;
    public ServerBall serverBall;
    public ClientBall clientBall;

    public Score score;


    public Server server;
    public Client client;

    private boolean isRunning;
    public Thread thread;


    public int status = -1;
    private boolean paused;

    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void tick() {
        keyManager.tick();
        if (status == 1) {
            clientBall.tick();
        } else {
            serverBall.tick();
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

        racket1.draw(graphics, Color.red);
        racket2.draw(graphics, Color.blue);

        score.draw(graphics);

        if (status == 1) {
            clientBall.draw(graphics, color1);
        } else {
            serverBall.draw(graphics, color2);

        }

        bufferStrategy.show();
        graphics.dispose();

    }

    public void init() {

        gameFrame = new GameFrame(title, width, height);

        score = new Score(width, height);

        keyManager = new KeyManager(this);

        gameFrame.getFrame().addKeyListener(keyManager);

        racket1 = new Racket(0, 0, 25, 200, height);
        racket2 = new Racket(775, 0, 25, 200, height);
        serverBall = new ServerBall(this,width,height,score);
        clientBall = new ClientBall();



        String[] options = {"Start game!", "Join game!"};
        int welcomeOption = JOptionPane.showOptionDialog(gameFrame.getFrame(),
                "Please choose what you need",
                "Ping-pong game",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (welcomeOption) {
            case (-1):
                System.exit(0);
                break;
            case (1):
                color1 = setBallColor();
                namePlayer2 = inputName();
                status = 1;
                paused = false;
                client = new Client(null,PORT,this);
                client.start();
                break;
            case (0):
                color2 = setBallColor();
                namePlayer1= inputName();
                status = 0;
                paused = true;
                server = new Server(PORT, this);
                server.start();
                JOptionPane.showMessageDialog(null,"Waiting for player 2. Enter OK and game will start when player 2 join");
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

    public int getStatus() {
        return status;
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    public ServerBall getServerBall() {

        return serverBall;
    }

    public ClientBall getClientBall() {

        return clientBall;
    }

    public Score getScore() {
        return score;
    }

    public String inputName() {
        String name =  JOptionPane.showInputDialog(gameFrame.getFrame(),"Input your name:");
        if (name.length() != 0) {
            return name;
        } else {
            while (name.length() == 0) {
                name = JOptionPane.showInputDialog(gameFrame.getFrame(),"Input your name:");
            }
            return name;

        }
    }

    public Color setBallColor() {
        return  JColorChooser.showDialog(null, "Цвет", null);
    }

    @Override
    public void run() {

        init();

        double fps = 60;
        double timePeriodTick = 1000000000 / fps;
        double delta = 0;
        double now;
        double lastTime = System.nanoTime();

        while (isRunning) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePeriodTick;
            lastTime = now;

            if (delta >= 1) {
                if (!paused) {
                    tick();
                }
                draw();
                delta--;
            }
        }
    }

    public synchronized void start() {
        if (isRunning) {
            return;
        } else {
            isRunning = true;
        }

        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {

        if (!isRunning) {
            return;
        } else {
            isRunning = false;
        }

        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void play() {
        paused = false;
    }
}