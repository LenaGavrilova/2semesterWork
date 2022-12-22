package ru.kpfu.itis.controller;

import ru.kpfu.itis.game.Game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    private Game game;
    private final boolean[] KEYS;
    public boolean up, down, w, s;

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
