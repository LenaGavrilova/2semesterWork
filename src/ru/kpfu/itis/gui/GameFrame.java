package ru.kpfu.itis.gui;

import javax.swing.*;
import java.awt.*;

public class GameFrame {
    private JFrame frame;
    private Canvas canvas;

    private final String TITLE;
    private final int WIDTH;
    private final int HEIGHT;
    private final Dimension SCREEN_SIZE;

    public GameFrame(String title, int width, int height) {
        this.TITLE = title;
        this.WIDTH = width;
        this.HEIGHT = height;
        SCREEN_SIZE = new Dimension(WIDTH,HEIGHT);
        init();
    }

    private void init() {
        frame = new JFrame(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(SCREEN_SIZE);
        canvas.setMaximumSize(SCREEN_SIZE);
        canvas.setMinimumSize(SCREEN_SIZE);
        canvas.setFocusable(false);

        frame.add(canvas);
        frame.pack();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public JFrame getFrame() {
        return frame;
    }
}
