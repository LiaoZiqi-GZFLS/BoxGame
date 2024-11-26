package com.example.boxgame;

public class Box extends Character{
    public Box(int x, int y) {
        super(x, y);
    }
    public Box(){
        super();
    }

    public static int[][] getBoxesPosition(Box[] t_boxes){
        int[][] BoxesPosition = new int[t_boxes.length][2];
        for (int i = 0; i < t_boxes.length; i++) {
            BoxesPosition[i][0] = t_boxes[i].getX();
            BoxesPosition[i][1] = t_boxes[i].getY();
        }
        return BoxesPosition;
    }
}