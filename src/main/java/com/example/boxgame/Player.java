package com.example.boxgame;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static com.example.boxgame.CharacterImages.getPlayerImage;

public class Player extends Character{
    public Player(int x, int y) {
        super(x, y);
        this.image = getPlayerImage(0,0);
    }
    public Player(){
        super();
        this.image = getPlayerImage(0,0);
    }

    @NotNull
    @Contract("_ -> new")
    public static int[] getPlayerPosition(Character t_player){
        return new int[]{t_player.getX(),t_player.getY()};
    }
}