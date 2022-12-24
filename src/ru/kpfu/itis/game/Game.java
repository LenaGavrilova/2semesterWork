package ru.kpfu.itis.game;

import ru.kpfu.itis.listener.KeyManager;
import ru.kpfu.itis.gui.entities.ClientBall;
import ru.kpfu.itis.gui.entities.Racket;
import ru.kpfu.itis.gui.entities.Score;
import ru.kpfu.itis.gui.entities.ServerBall;
import ru.kpfu.itis.network.Client;
import ru.kpfu.itis.network.Server;
import ru.kpfu.itis.gui.GameFrame;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.*;

public class Game implements Runnable {

    private final int PORT = 86;

    private GameFrame gameFrame;
    private final String TITLE = "PING PONG";
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private final int RACKET_WIDTH = 25;
    private final int RACKET_HEIGHT = 200;

    private final String[] WELCOME_OPTIONS = {"Start game!", "Join game!"};
    private final String[] RESULT_OPTIONS = {"Continue", "Exit"};
    private final String WELCOME_MESSAGE = "Please choose what you need";
    private final String SERVER_MESSAGE = "Waiting for player 2. Enter OK and game will start when player 2 join. \n" + "Your IP address is " + getIP();
    private final String CLIENT_MESSAGE = "Input IP address to start game: ";

    private BufferStrategy bufferStrategy;
    private Graphics graphics;

    private KeyManager keyManager;

    private Color color1;
    private Color color2;

    public Racket clientRacket;
    public Racket serverRacket;
    public ServerBall serverBall;
    public ClientBall clientBall;
    public Score score;

    public Server server;
    public Client client;

    private boolean isRunning;
    public Thread thread;

    private String ip;

    private int status = -1;
    public boolean paused;

    public Game() {
        start();
    }

    public void redraw() {
        keyManager.redraw();
        if (status == 1) {
            clientBall.redraw();
        } else {
            serverBall.redraw();
        }
    }

    public void draw() {
        bufferStrategy = gameFrame.getCanvas().getBufferStrategy();

        if (bufferStrategy == null) {
            gameFrame.getCanvas().createBufferStrategy(3);
            return;
        }

        graphics = bufferStrategy.getDrawGraphics();

        graphics.clearRect(0, 0, WIDTH, HEIGHT);
        clientRacket.draw(graphics, Color.red);
        serverRacket.draw(graphics, Color.blue);
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
        gameFrame = new GameFrame(TITLE, WIDTH, HEIGHT);
        score = new Score(WIDTH, HEIGHT);
        keyManager = new KeyManager(this);
        gameFrame.getFrame().addKeyListener(keyManager);

        clientRacket = new Racket(0, (HEIGHT / 2) - (RACKET_HEIGHT / 2), RACKET_WIDTH, RACKET_HEIGHT, HEIGHT);
        serverRacket = new Racket(WIDTH - RACKET_WIDTH, (HEIGHT / 2) - (RACKET_HEIGHT / 2), RACKET_WIDTH, RACKET_HEIGHT, HEIGHT);
        serverBall = new ServerBall(this, WIDTH, HEIGHT, score);
        clientBall = new ClientBall();

        int chosenOption = JOptionPane.showOptionDialog(gameFrame.getFrame(),
                WELCOME_MESSAGE,
                TITLE,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                WELCOME_OPTIONS,
                WELCOME_OPTIONS[0]);

        switch (chosenOption) {
            case (-1):
                System.exit(0);
                break;
            case (0):
                color2 = setBallColor();
                status = 0;
                paused = true;
                server = new Server(PORT, this);
                server.start();
                JOptionPane.showMessageDialog(gameFrame.getFrame(), SERVER_MESSAGE);
                break;
            case (1):
                color1 = setBallColor();
                ip = inputData(CLIENT_MESSAGE);
                status = 1;
                paused = false;
                client = new Client(ip, PORT, this);
                client.start();
                break;
        }
    }

    public void showResult(String message) throws IOException {
        int chosenOption = JOptionPane.showOptionDialog(gameFrame.getFrame(),
                message,
                TITLE,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                RESULT_OPTIONS,
                RESULT_OPTIONS[0]);
        switch (chosenOption) {
            case (1):
                System.exit(0);
                break;
            case (0):
                play();
                score.player1 = 0;
                score.player2 = 0;
                break;
        }
    }

    public String inputData(String message) {
        String data = JOptionPane.showInputDialog(gameFrame.getFrame(), message);
        while (data.length() == 0) {
            data = JOptionPane.showInputDialog(gameFrame.getFrame(), message);
        }
        return data;
    }


    public Color setBallColor() {
        return JColorChooser.showDialog(gameFrame.getFrame(), "Choose ball color", Color.green);
    }

    @Override
    public void run() {
        init();

        double fps = 60;
        double timePeriodRedrawing = 1000000000 / fps;
        double delta = 0;
        double now;
        double lastTime = System.nanoTime();

        while (isRunning) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePeriodRedrawing;
            lastTime = now;
            if (delta >= 1) {
                if (!paused) {
                    redraw();
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

    public void play() {
        paused = false;
    }

    public void gamePaused() {
        paused = true;
    }

    public String getIP() {
        String address;
        try {
            address = String.valueOf(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
        String[] parts = address.split("/");
        return parts[1];
    }

    public Racket getRacket() {
        if (status == 1) {
            return getClientRacket();
        } else {
            return getServerRacket();
        }
    }

    public Racket getClientRacket() {
        return clientRacket;
    }

    public Racket getServerRacket() {
        return serverRacket;
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

    public ClientBall getClientBall() {
        return clientBall;
    }

    public Score getScore() {
        return score;
    }
}