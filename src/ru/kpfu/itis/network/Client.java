package ru.kpfu.itis.network;

import ru.kpfu.itis.game.Game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {
    public String ipAddress;
    public int port;
    public Game game;

    private boolean isRunning;
    public Thread thread;

    private Socket socket = null;
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;

    public Client(String ipAddress, int port, Game game) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.game = game;
    }

    public DataOutputStream getOut() {
        return outputStream;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                socket = new Socket(ipAddress, port);
                System.out.println("Connected");
                inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }

            String line = "";
            while (!line.equals("Over")) {
                game.redraw();
                game.draw();
                try {
                    line = inputStream.readUTF();
                    if (line.startsWith("w")) {
                        game.getServerRacket().moveUp();
                    } else if (line.startsWith("s")) {
                        game.getServerRacket().moveDown();
                    } else if (line.startsWith("up")) {
                        game.getServerRacket().moveUp();
                    } else if (line.startsWith("down")) {
                        game.getServerRacket().moveDown();
                    } else if (line.startsWith("Ball")) {
                        String[] ints = line.split(" ");
                        game.getClientBall().setX(Integer.parseInt(ints[1]));
                        game.getClientBall().setY(Integer.parseInt(ints[2]));
                    } else if (line.startsWith("Score")) {
                        String[] ints = line.split(" ");
                        game.getScore().setPlayer1(Integer.parseInt(ints[1]));
                        game.getScore().setPlayer2(Integer.parseInt(ints[2]));
                    }else if(line.startsWith("YOU WIN!")){
                        game.showResult(line);
                    } else if(line.startsWith("YOU LOSE!")){
                        game.showResult(line);
                    }
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            System.out.println("Closing connection");
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
}
