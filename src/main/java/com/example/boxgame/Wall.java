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
    public Wall(){
        this.x = 0;
        this.y = 0;
        this.oldX = 0;
        this.oldY = 0;
    }

    public static int[][] getWallsPosition(Wall[] t_walls){
        int[][] WallsPosition = new int[t_walls.length][2];
        for (int i = 0; i < t_walls.length; i++) {
            WallsPosition[i][0] = t_walls[i].getX();
            WallsPosition[i][1] = t_walls[i].getY();
        }
        return WallsPosition;
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
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setOldX(int oldX) {
        this.oldX = oldX;
    }

    public void setOldY(int oldY) {
        this.oldY = oldY;
    }

    public void move(int dx, int dy) {
        oldX = x;
        oldY = y;
        x += dx;
        y += dy;
    }
}
