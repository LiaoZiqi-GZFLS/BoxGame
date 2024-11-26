package com.example.boxgame;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Player extends Character{
    public Player(int x, int y) {
        super(x, y);
    }
    public Player(){
        super();
    }

    @NotNull
    @Contract("_ -> new")
    public static int[] getPlayerPosition(Character t_player){
        return new int[]{t_player.getX(),t_player.getY()};
    }
}