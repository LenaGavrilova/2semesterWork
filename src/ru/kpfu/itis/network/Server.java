package ru.kpfu.itis.network;

import ru.kpfu.itis.game.Game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private int port;
    private Game game;
    private String line;

    private boolean keepRunning;
    private Thread thread;

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
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());

                String line = "";

                while (!line.equals("Over")) {
                    try {
                        line = in.readUTF();
                        if (line.startsWith("w")) {
                            game.getClientRacket().moveUp();
                        } else if (line.startsWith("s")) {
                            game.getClientRacket().moveDown();
                        } else if (line.startsWith("up")) {
                            game.getClientRacket().moveUp();
                        } else if (line.startsWith("down")) {
                            game.getClientRacket().moveDown();
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public boolean isKeepRunning() {
        return keepRunning;
    }

    public void setKeepRunning(boolean keepRunning) {
        this.keepRunning = keepRunning;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }
}
