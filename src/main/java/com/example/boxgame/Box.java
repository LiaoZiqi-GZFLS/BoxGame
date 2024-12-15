package com.example.boxgame;

import org.jetbrains.annotations.NotNull;

import static com.example.boxgame.CharacterImages.getBoxImage;
import static com.example.boxgame.BoxGame._map;

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

    public void move(int dx, int dy){
        super.move(dx, dy);
        //System.out.println(_map[y][x]);
        if(_map[y][x]=='T'||_map[y][x]=='@'){
            if(imageID==0){
                image=getBoxImage(1);
                imageID=1;
            }
        }
        if(_map[y][x]=='.'){
            if(imageID==1){
                image=getBoxImage(0);
                imageID=0;
            }
        }
    }
}