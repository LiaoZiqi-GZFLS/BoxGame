package com.example.boxgame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player {
    private int x;
    private int y;
    private int oldX;
    private int oldY;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public void move(int dx, int dy) {
        oldX = x;
        oldY = y;
        x += dx;
        y += dy;
    }
}