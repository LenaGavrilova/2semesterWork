package ru.kpfu.itis.listener;

import ru.kpfu.itis.game.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class KeyManager implements KeyListener {
    private final Game GAME;
    private final boolean[] KEYS;
    public boolean up;
    public boolean down;
    public boolean w;
    public boolean s;

    public KeyManager(Game game) {
        this.GAME = game;
        KEYS = new boolean[256];
    }

    public void redraw() {
        up = KEYS[KeyEvent.VK_UP];
        down = KEYS[KeyEvent.VK_DOWN];
        w = KEYS[KeyEvent.VK_W];
        s = KEYS[KeyEvent.VK_S];

        if (up) {
            GAME.getRacket().moveUp();
        }
        if (down) {
            GAME.getRacket().moveDown();
        }
        if (w) {
            GAME.getRacket().moveUp();
        }
        if (s) {
            GAME.getRacket().moveDown();
        }

        if (GAME.getStatus() == 0) {
            if (w) {
                try {
                    GAME.getServer().getOut().writeUTF("w\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            if (s) {
                try {
                    GAME.getServer().getOut().writeUTF("s\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            if (up) {
                try {
                    GAME.getServer().getOut().writeUTF("up\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            if (down) {
                try {
                    GAME.getServer().getOut().writeUTF("down\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        if (GAME.getStatus() == 1) {
            if (w) {
                try {
                    GAME.getClient().getOut().writeUTF("w\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            if (s) {
                try {
                    GAME.getClient().getOut().writeUTF("s\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            if (up) {
                try {
                    GAME.getClient().getOut().writeUTF("up\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            if (down) {
                try {
                    GAME.getClient().getOut().writeUTF("down\n");
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
