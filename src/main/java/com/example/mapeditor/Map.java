package com.example.mapeditor;

import java.util.Arrays;


public class Map {
    private static final int GRID_COUNT = 10;
    //墙：#
    //空：.
    //玩家：P
    //箱子：B
    //目标：T
    //玩家在目标上：?
    //箱子在目标上：@
    private final char[][] imap = {
        ".T###.#..#".toCharArray(),
        "..###.#.B#".toCharArray(),
        "..#...#..#".toCharArray(),
        "#.....##.#".toCharArray(),
        "...##.##.#".toCharArray(),
        "##..#.#...".toCharArray(),
        "##.##.....".toCharArray(),
        ".......###".toCharArray(),
        ".#.#.#...#".toCharArray(),
        "P...##..#.".toCharArray(),
    };
    private char[][] map0 = new char[GRID_COUNT][GRID_COUNT];
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

//    public static char[][] getMap0(Rectangle[][] grid){
//        char[][] backupMap = new char[GRID_COUNT][GRID_COUNT];
//        Rectangle rect1 = new Rectangle(GRID_SIZE, GRID_SIZE, GroundColor);
//        Rectangle rect2 = new Rectangle(GRID_SIZE, GRID_SIZE, PlayerColor);
//        Rectangle rect3 = new Rectangle(GRID_SIZE, GRID_SIZE, BoxColor);
//        Rectangle rect4 = new Rectangle(GRID_SIZE, GRID_SIZE, PositionColor);
//        Rectangle rect5 = new Rectangle(GRID_SIZE, GRID_SIZE, WallColor);
//        Paint fill1 = rect1.getFill();
//        Paint fill2 = rect2.getFill();
//        Paint fill3 = rect3.getFill();
//        Paint fill4 = rect4.getFill();
//        Paint fill5 = rect5.getFill();
//        for(int i=0;i<GRID_COUNT;i++){
//            for(int j=0;j<GRID_COUNT;j++){
//                Paint fill = grid[j][i].getFill();
//                if(fill==fill1){
//                    backupMap[i][j] = '.';
//                }
//                else if(fill==fill2){
//                    backupMap[i][j] = 'P';
//                }
//                else if(fill==fill3){
//                    backupMap[i][j] = 'B';
//                }
//                else if(fill==fill4){
//                    backupMap[i][j] = 'T';
//                }
//                else if(fill==fill5){
//                    backupMap[i][j] = '#';
//                }
//            }
//        }
//        return backupMap;
//    }
//    public static char[][] getMap0(Rectangle[][] grid, int[][] t_Position){
//        char[][] backupMap = getMap0(grid);
//        for(int[] target : t_Position){
//            if(backupMap[target[1]][target[0]]=='P'){
//                backupMap[target[1]][target[0]] = '?';
//            }
//            if(backupMap[target[1]][target[0]]=='B'){
//                backupMap[target[1]][target[0]] = '@';
//            }
//        }
//        return backupMap;
//    }
    public static char[][] getMap1(char[][] t_map){
        char[][] tempMap = t_map.clone();
        for(char[] row : tempMap){
            for(char col : row){
                if(col=='@'){
                    col = '*';
                }
                if(col=='?'){
                    col = '+';
                }
                if(col=='P'){
                    col = '@';
                }
                if(col=='B'){
                    col = '$';
                }
                if(col=='.'){
                    col = ' ';
                }
                if(col=='T'){
                    col = '.';
                }
            }
        }
        return tempMap;
    }
    public String toString(int x,int y){
        StringBuilder str= new StringBuilder();
        str.append(x);
        str.append(" ");
        str.append(y);
        str.append('\n');
        for(char[] chars : map0){
            str.append(chars);
            str.append('\n');
        }
        return str.toString();
    }
    public String toString(boolean b){
        char[][] t_map = map0.clone();
        if(b){
            t_map = getMap1(map0);
        }
        StringBuilder str= new StringBuilder();
        for(char[] chars : t_map){
            str.append(chars);
            str.append('\n');
        }
        return str.toString();
    }
}