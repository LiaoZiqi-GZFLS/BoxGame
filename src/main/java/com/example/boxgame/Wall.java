package com.example.boxgame;

public class Wall {
    private int x;
    private int y;
    private int oldX;
    private int oldY;

    public Wall(int x, int y) {
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
