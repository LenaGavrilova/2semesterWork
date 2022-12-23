package ru.kpfu.itis.controller;

import ru.kpfu.itis.game.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class KeyManager implements KeyListener {
    private Game game;
    private final boolean[] KEYS;
    public boolean up;
    public boolean down;
    public boolean w;
    public boolean s;

    public KeyManager(Game game) {
        this.game = game;
        KEYS = new boolean[256];
    }

    public void tick() {
        up = KEYS[KeyEvent.VK_UP];
        down = KEYS[KeyEvent.VK_DOWN];
        w = KEYS[KeyEvent.VK_W];
        s = KEYS[KeyEvent.VK_S];

        if (up) {
            game.getRacket().moveUp();
        }
        if (down) {
            game.getRacket().moveDown();
        }
        if (w) {
            game.getRacket().moveUp();
        }
        if (s) {
            game.getRacket().moveDown();
        }

        // обработчик для сервера
        if (game.getStatus() == 0) {
            if (w) {
                try {
                    game.getServer().getOut().writeUTF("w\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            if (s) {
                try {
                    game.getServer().getOut().writeUTF("s\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            if (up) {
                try {
                    game.getServer().getOut().writeUTF("up\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            if (down) {
                try {
                    game.getServer().getOut().writeUTF("down\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        //обработчик для клиента
        if (game.getStatus() == 1) {
            if (w) {
                try {
                    game.getClient().getOut().writeUTF("w\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            if (s) {
                try {
                    game.getClient().getOut().writeUTF("s\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            if (up) {
                try {
                    game.getClient().getOut().writeUTF("up\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            if (down) {
                try {
                    game.getClient().getOut().writeUTF("down\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        KEYS[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        KEYS[e.getKeyCode()] = false;
    }


}
