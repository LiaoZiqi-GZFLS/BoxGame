package com.example.boxgame;

import org.jetbrains.annotations.NotNull;

public class Target extends Character{
    public Target(int x, int y) {
        super(x, y);
    }
    public Target(){
        super();
    }

    @NotNull
    public static int[][] getTargetPosition(Target[] t_positions){
        int[][] Positions = new int[t_positions.length][2];
        for (int i = 0; i < t_positions.length; i++) {
            Positions[i][0] = t_positions[i].getX();
            Positions[i][1] = t_positions[i].getY();
        }
        return Positions;
    }
}
