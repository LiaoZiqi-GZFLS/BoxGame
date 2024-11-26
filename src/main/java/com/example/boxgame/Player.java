package com.example.boxgame;

public class Player extends Character{
    public Player(int x, int y) {
        super(x, y);
    }
    public Player(){
        super();
    }

    public static int[] getPlayerPosition(Character t_player){
        return new int[]{t_player.getX(),t_player.getY()};
    }
}