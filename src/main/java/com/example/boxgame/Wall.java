package com.example.boxgame;

import org.jetbrains.annotations.NotNull;

import static com.example.boxgame.CharacterImages.getWallImage;

public class Wall extends Character{
    public Wall(int x, int y) {
        super(x, y);
        this.image = getWallImage(0);
    }
    public Wall(){
        super();
        this.image = getWallImage(0);
    }

    @NotNull
    public static int[][] getWallsPosition(Wall[] t_walls){
        int[][] WallsPosition = new int[t_walls.length][2];
        for (int i = 0; i < t_walls.length; i++) {
            WallsPosition[i][0] = t_walls[i].getX();
            WallsPosition[i][1] = t_walls[i].getY();
        }
        return WallsPosition;
    }
}
