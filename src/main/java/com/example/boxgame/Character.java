package com.example.boxgame;

import javafx.scene.image.Image;

public class Character {
    protected int x;
    protected int y;
    protected int oldX;
    protected int oldY;

    protected Image image;
    protected int imageID;

    public Character(int x, int y) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.imageID = 0;
    }
    public Character(){
        this.x = 0;
        this.y = 0;
        this.oldX = 0;
        this.oldY = 0;
        this.imageID = 0;
    }

    public static int[] getCharacterPosition(Character t_character){
        return new int[]{t_character.getX(),t_character.getY()};
    }
    public static int[][] getCharacterPosition(Character[] t_character){
        int[][] CharactersPosition = new int[t_character.length][2];
        for (int i = 0; i < t_character.length; i++) {
            CharactersPosition[i][0] = t_character[i].getX();
            CharactersPosition[i][1] = t_character[i].getY();
        }
        return CharactersPosition;
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
