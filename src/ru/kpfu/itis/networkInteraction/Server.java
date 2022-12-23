package ru.kpfu.itis.networkInteraction;

import ru.kpfu.itis.game.Game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    public int port;
    public Game game;
    public String line;

    private boolean keepRunning;
    public Thread thread;

    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Server(int port, Game game) {
        this.port = port;
        this.game = game;
    }

    public DataOutputStream getOut() {
        return out;
    }

    @Override
    public void run() {
        while (keepRunning) {
            try {
                server = new ServerSocket(port);
                System.out.println("Server started");
                System.out.println("Waiting for a client ...");
                socket = server.accept();
                System.out.println("Client accepted");
                game.play();
//получаем данные из клиентского сокета
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());

                String line = "";
                //читаем сообщение из клиента
                while (!line.equals("Over")) {
                    try {
                        line = in.readUTF();
                        if (line.startsWith("w")) {
                            game.getRacket1().moveUp();
                        } else if (line.startsWith("s")) {
                            game.getRacket1().moveDown();
                        } else if (line.startsWith("up")) {
                            game.getRacket1().moveUp();
                        } else if (line.startsWith("down")) {
                            game.getRacket1().moveDown();
                        }
                    } catch (IOException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
                System.out.println("Closing connection");
                try {
                    in.close();
                    out.close();
                    socket.close();
                    keepRunning = false;
                } catch (IOException ex) {
                    throw new IllegalStateException(ex);
                }
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }

    }

    public synchronized void start() {
        if (keepRunning) {
            return;
        } else {
            keepRunning = true;
        }
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!keepRunning) {
            return;
        } else {
            keepRunning = false;
        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
