package com.example.boxgame;

import org.jetbrains.annotations.NotNull;

import static com.example.boxgame.CharacterImages.getBoxImage;

public class Box extends Character{
    public Box(int x, int y) {
        super(x, y);
        this.image = getBoxImage(0);
    }
    public Box(){
        super();
        this.image = getBoxImage(0);
    }

    @NotNull
    public static int[][] getBoxesPosition(Box[] t_boxes){
        int[][] BoxesPosition = new int[t_boxes.length][2];
        for (int i = 0; i < t_boxes.length; i++) {
            BoxesPosition[i][0] = t_boxes[i].getX();
            BoxesPosition[i][1] = t_boxes[i].getY();
        }
        return BoxesPosition;
    }
}