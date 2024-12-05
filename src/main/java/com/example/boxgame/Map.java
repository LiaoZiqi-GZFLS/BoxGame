package com.example.boxgame;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.Arrays;

import static com.example.boxgame.BoxGameApplication.*;

public class Map {
    //墙：#
    //空：.
    //玩家：P
    //箱子：B
    //目标：T
    //玩家在目标上：?
    //箱子在目标上：@
    //压力传感器：Y
    //灯泡：L
    private final char[][] imap = {
        "##########".toCharArray(),
        "#L#L#L#.T#".toCharArray(),
        "#######..#".toCharArray(),
        "#Y#Y#Y#.B#".toCharArray(),
        "#........#".toCharArray(),
        "#P.......#".toCharArray(),
        "#Y#Y#Y#Y##".toCharArray(),
        "##########".toCharArray(),
        "#L#L#L#L##".toCharArray(),
        "##########".toCharArray(),
    };
    private final char[][] map0 = new char[GRID_COUNT][GRID_COUNT];
    private final char[][] map1 = {
            "######".toCharArray(),
            "#P...#".toCharArray(),
            "#..BT#".toCharArray(),
            "#.TB.#".toCharArray(),
            "######".toCharArray(),
    };
    private final char[][] map2 = {
            "######.".toCharArray(),
            "#P...##".toCharArray(),
            "#.BB..#".toCharArray(),
            "#.#T.T#".toCharArray(),
            "#.....#".toCharArray(),
            "#######".toCharArray(),
    };
    private final char[][] map3 = {
            "..####.".toCharArray(),
            "###..#.".toCharArray(),
            "#P.TB##".toCharArray(),
            "#...B.#".toCharArray(),
            "#.#T..#".toCharArray(),
            "#.....#".toCharArray(),
            "#######".toCharArray(),
    };
    private final char[][] map4 = {
            ".#####.".toCharArray(),
            "##P..##".toCharArray(),
            "#..#..#".toCharArray(),
            "#.B@B.#".toCharArray(),
            "#..T..#".toCharArray(),
            "##.T.##".toCharArray(),
            ".#####.".toCharArray(),
    };
    private final char[][] map5 = {
            "######..".toCharArray(),
            "#....###".toCharArray(),
            "#...TT.#".toCharArray(),
            "#.BBBP.#".toCharArray(),
            "#..#.T.#".toCharArray(),
            "########".toCharArray(),
    };
    private final char[][][] map = {
            imap,map1, map2, map3, map4, map5,
    };
    public Map() {
        int n=N;
        for(int i = 0; i < GRID_COUNT; i++) {
            Arrays.fill(map0[i], '.');
        }
        if(map[n].length <= GRID_COUNT && map[n][0].length <= GRID_COUNT) {
            int y=GRID_COUNT/2-map[n].length/2-map[n].length%2;
            int x=GRID_COUNT/2-map[n][0].length/2-map[n][0].length%2;
            for (int i = 0; i < map[n].length; i++) {
                for (int j = 0; j < map[n][0].length; j++) {
                    map0[i+y][j+x] = map[n][i][j];
                }
            }
        }else{
            int y=map[n].length/2-GRID_COUNT/2;
            int x=map[n][0].length/2-GRID_COUNT/2;
            for(int i = 0; i < GRID_COUNT; i++){
                for(int j = 0; j < GRID_COUNT; j++){
                    map0[i][j] = map[n][i+y][j+x];
                }
            }
        }
    }
    public Map(int n) {
        for(int i = 0; i < GRID_COUNT; i++) {
            Arrays.fill(map0[i], '.');
        }
        if(map[n].length <= GRID_COUNT && map[n][0].length <= GRID_COUNT) {
            int y=GRID_COUNT/2-map[n].length/2-map[n].length%2;
            int x=GRID_COUNT/2-map[n][0].length/2-map[n][0].length%2;
            for (int i = 0; i < map[n].length; i++) {
                for (int j = 0; j < map[n][0].length; j++) {
                    map0[i+y][j+x] = map[n][i][j];
                }
            }
        }else{
            int y=map[n].length/2-GRID_COUNT/2;
            int x=map[n][0].length/2-GRID_COUNT/2;
            for(int i = 0; i < GRID_COUNT; i++){
                for(int j = 0; j < GRID_COUNT; j++){
                    map0[i][j] = map[n][i+y][j+x];
                }
            }
        }
    }
    public Map(char[][] _map_) {
        for(int i = 0; i < GRID_COUNT; i++) {
            Arrays.fill(map0[i], '.');
        }
        if(_map_.length <= GRID_COUNT && _map_[0].length <= GRID_COUNT) {
            int y=GRID_COUNT/2-_map_.length/2-_map_.length%2;
            int x=GRID_COUNT/2-_map_[0].length/2-_map_[0].length%2;
            for (int i = 0; i < _map_.length; i++) {
                for (int j = 0; j < _map_[0].length; j++) {
                    map0[i+y][j+x] = _map_[i][j];
                }
            }
        }else{
            int y=_map_.length/2-GRID_COUNT/2;
            int x=_map_[0].length/2-GRID_COUNT/2;
            for(int i = 0; i < GRID_COUNT; i++){
                for(int j = 0; j < GRID_COUNT; j++){
                    map0[i][j] = _map_[i+y][j+x];
                }
            }
        }
    }
    public char[][] getMap() {
        return map0;
    }
    public int[] getPlayerPosition(){
        int x=-1;
        int y=-1;
        for(int i=0;i<GRID_COUNT;i++){
            for(int j=0;j<GRID_COUNT;j++){
                if(map0[i][j]=='P'||map0[i][j]=='?'){
                    x=j;
                    y=i;
                    break;
                }
            }
        }
        return new int[]{x,y};
    }
    public int[][] getBoxesPosition(){
        int num=0;
        for(int i=0;i<GRID_COUNT;i++){
            for(int j=0;j<GRID_COUNT;j++){
                if(map0[i][j]=='B'||map0[i][j]=='@'){
                    num++;
                }
            }
        }
        int[][] ints = new int[num][2];
        int n=0;
        for(int i=0;i<GRID_COUNT;i++){
            for(int j=0;j<GRID_COUNT;j++){
                if(map0[i][j]=='B'||map0[i][j]=='@'){
                    ints[n][0]=j;
                    ints[n][1]=i;
                    n++;
                }
            }
        }
        return ints;
    }
    public int[][] getPosition(){
        int num=0;
        for(int i=0;i<GRID_COUNT;i++){
            for(int j=0;j<GRID_COUNT;j++){
                if(map0[i][j]=='T'||map0[i][j]=='@'||map0[i][j]=='?'){
                    num++;
                }
            }
        }
        int[][] ints = new int[num][2];
        int n=0;
        for(int i=0;i<GRID_COUNT;i++){
            for(int j=0;j<GRID_COUNT;j++){
                if(map0[i][j]=='T'||map0[i][j]=='@'||map0[i][j]=='?'){
                    ints[n][0]=j;
                    ints[n][1]=i;
                    n++;
                }
            }
        }
        return ints;
    }
    public int[][] getWallPosition(){
        int num=0;
        for(int i=0;i<GRID_COUNT;i++){
            for(int j=0;j<GRID_COUNT;j++){
                if(map0[i][j]=='#'){
                    num++;
                }
            }
        }
        int[][] ints = new int[num][2];
        int n=0;
        for(int i=0;i<GRID_COUNT;i++){
            for(int j=0;j<GRID_COUNT;j++){
                if(map0[i][j]=='#'){
                    ints[n][0]=j;
                    ints[n][1]=i;
                    n++;
                }
            }
        }
        return ints;
    }

    public static char[][] getMap0(Rectangle[][] grid){
        char[][] backupMap = new char[GRID_COUNT][GRID_COUNT];
        Rectangle rect1 = new Rectangle(GRID_SIZE, GRID_SIZE, GroundColor);
        Rectangle rect2 = new Rectangle(GRID_SIZE, GRID_SIZE, PlayerColor);
        Rectangle rect3 = new Rectangle(GRID_SIZE, GRID_SIZE, BoxColor);
        Rectangle rect4 = new Rectangle(GRID_SIZE, GRID_SIZE, PositionColor);
        Rectangle rect5 = new Rectangle(GRID_SIZE, GRID_SIZE, WallColor);
        Paint fill1 = rect1.getFill();
        Paint fill2 = rect2.getFill();
        Paint fill3 = rect3.getFill();
        Paint fill4 = rect4.getFill();
        Paint fill5 = rect5.getFill();
        for(int i=0;i<GRID_COUNT;i++){
            for(int j=0;j<GRID_COUNT;j++){
                Paint fill = grid[j][i].getFill();
                if(fill==fill1){
                    backupMap[i][j] = '.';
                }
                else if(fill==fill2){
                    backupMap[i][j] = 'P';
                }
                else if(fill==fill3){
                    backupMap[i][j] = 'B';
                }
                else if(fill==fill4){
                    backupMap[i][j] = 'T';
                }
                else if(fill==fill5){
                    backupMap[i][j] = '#';
                }
            }
        }
        return backupMap;
    }
    public static char[][] getMap0(Rectangle[][] grid, int[][] t_Position){
        char[][] backupMap = getMap0(grid);
        for(int[] target : t_Position){
            if(backupMap[target[1]][target[0]]=='P'){
                backupMap[target[1]][target[0]] = '?';
            }
            if(backupMap[target[1]][target[0]]=='B'){
                backupMap[target[1]][target[0]] = '@';
            }
        }
        return backupMap;
    }
}
