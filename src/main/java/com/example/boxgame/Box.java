package com.example.boxgame;

public class Box {
    private int x;
    private int y;
    private int oldX;
    private int oldY;

    public Box(int x, int y) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
    }
    public Box(){
        this.x = 0;
        this.y = 0;
        this.oldX = 0;
        this.oldY = 0;
    }

    public static int[][] getBoxesPosition(Box[] t_boxes){
        int[][] BoxesPosition = new int[t_boxes.length][2];
        for (int i = 0; i < t_boxes.length; i++) {
            BoxesPosition[i][0] = t_boxes[i].x;
            BoxesPosition[i][1] = t_boxes[i].y;
        }
        return BoxesPosition;
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