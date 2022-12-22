package ru.kpfu.itis.networkInteraction;

import ru.kpfu.itis.game.Game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class Client implements Runnable {
    private String ipAddress;
    private int port;
    private Game game;

    private boolean isRunning;
    private Thread thread;

    private Socket socket = null;
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;

    public Client(String ipAddress, int port, Game game) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.game = game;

    }

    public Socket getSocket() {
        return socket;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public void run() {

        while (isRunning) {
            try {
                socket = new Socket(ipAddress, port);
                inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }

            String line = "";

            while (!line.equals("Over")) {
                game.tick();
                game.draw();
                try {
                    line = inputStream.readUTF();
                    if (line.startsWith("w")) {
                        game.getRacket2().moveUp();
                    } else if (line.startsWith("s")) {
                        game.getRacket2().moveDown();
                    } else if (line.startsWith("up")) {
                        game.getRacket1().moveUp();
                    } else if (line.startsWith("down")) {
                        game.getRacket1().moveDown();
                    } else if (line.startsWith("Ball")) {
                        int[] ints = Arrays.stream(line.replaceAll("-", " -").split("[^-\\d]+"))
                                .filter(s -> !s.matches("-?"))
                                .mapToInt(Integer::parseInt).toArray();

                        game.getClientBall().setX(ints[0]);
                        game.getClientBall().setY(ints[1]);

                    }

                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            try {
                inputStream.close();
                outputStream.close();
                socket.close();
                isRunning = false;
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
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
            e.printStackTrace();
        }
    }

}
