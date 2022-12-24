package ru.kpfu.itis.networkInteraction;

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

    public Socket getSocket() {
        return socket;
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
                //отправляем данные в сокет
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }

            String line = "";
            //читаем сообщение из клиента
            while (!line.equals("Over")) {
//                System.out.println(line);
                game.tick();
                game.draw();
                try {
                    line = inputStream.readUTF();
                    if (line.startsWith("w")) {
                        game.getRacket2().moveUp();
                    } else if (line.startsWith("s")) {
                        game.getRacket2().moveDown();
                    } else if (line.startsWith("up")) {
                        game.getRacket2().moveUp();
                    } else if (line.startsWith("down")) {
                        game.getRacket2().moveDown();
                    } else if (line.startsWith("Ball")) {
                        String[] ints = line.split(" ");
                        game.getClientBall().setX(Integer.parseInt(ints[1]));
                        game.getClientBall().setY(Integer.parseInt(ints[2]));
                    } else if (line.startsWith("Score")) {
                        String[] ints = line.split(" ");
                        game.getScore().setPlayer1(Integer.parseInt(ints[1]));
                        game.getScore().setPlayer2(Integer.parseInt(ints[2]));
//                    } else if(line.startsWith("Winner")) {
//                        String[] parts = line.split(" ");
//                        System.out.println(parts[0]);
//                        System.out.println(parts[1]);
//                    }
                    }else if(line.startsWith("You win!")){
                        System.out.println(line);
                    } else if(line.startsWith("You lose!")){
                        System.out.println(line);
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
