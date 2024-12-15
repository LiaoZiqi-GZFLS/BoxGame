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

    public void move(int dx, int dy){
        super.move(dx, dy);
        if(dx==0 && dy==-1){
            if(imageID==0){
                this.image = getPlayerImage(0,0);
                imageID = 1;
            }else if(imageID==1){
                this.image = getPlayerImage(0,1);
                imageID = 0;
            }
        }
        if(dx==0 && dy==1){
            if(imageID==0){
                this.image = getPlayerImage(1,0);
                imageID = 1;
            }else {
                this.image = getPlayerImage(1,1);
                imageID = 0;
            }
        }
        if(dx==-1 && dy==0){
            if(imageID==0){
                this.image = getPlayerImage(2,0);
                imageID = 1;
            }else {
                this.image = getPlayerImage(2,1);
                imageID = 0;
            }
        }
        if(dx==1 && dy==0){
            if(imageID==0){
                this.image = getPlayerImage(3,0);
                imageID = 1;
            }else  {
                this.image = getPlayerImage(3,1);
            }
        }
    }
}