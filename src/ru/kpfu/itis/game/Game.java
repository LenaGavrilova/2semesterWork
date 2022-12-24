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

    private Racket clientRacket;
    private Racket serverRacket;
    private ServerBall serverBall;
    private ClientBall clientBall;
    private Score score;

    private Server server;
    private Client client;

    private boolean isRunning;
    private Thread thread;

    private String ip;

    private int status = -1;
    private boolean paused;

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

    public int getPORT() {
        return PORT;
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public String getTITLE() {
        return TITLE;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getRACKET_WIDTH() {
        return RACKET_WIDTH;
    }

    public int getRACKET_HEIGHT() {
        return RACKET_HEIGHT;
    }

    public String[] getRESULT_OPTIONS() {
        return RESULT_OPTIONS;
    }

    public String getWELCOME_MESSAGE() {
        return WELCOME_MESSAGE;
    }

    public String getSERVER_MESSAGE() {
        return SERVER_MESSAGE;
    }

    public String getCLIENT_MESSAGE() {
        return CLIENT_MESSAGE;
    }

    public BufferStrategy getBufferStrategy() {
        return bufferStrategy;
    }

    public void setBufferStrategy(BufferStrategy bufferStrategy) {
        this.bufferStrategy = bufferStrategy;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }

    public void setKeyManager(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    public Color getColor1() {
        return color1;
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public Color getColor2() {
        return color2;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    public void setClientRacket(Racket clientRacket) {
        this.clientRacket = clientRacket;
    }

    public void setServerRacket(Racket serverRacket) {
        this.serverRacket = serverRacket;
    }

    public ServerBall getServerBall() {
        return serverBall;
    }

    public void setServerBall(ServerBall serverBall) {
        this.serverBall = serverBall;
    }

    public void setClientBall(ClientBall clientBall) {
        this.clientBall = clientBall;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public String[] getWELCOME_OPTIONS() {
        return WELCOME_OPTIONS;
    }
}