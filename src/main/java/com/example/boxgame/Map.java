package com.example.boxgame;

import java.util.Arrays;

public class Map {
    //墙：#
    //空：.
    //玩家：P
    //箱子：B
    //目标：T
    //玩家在目标上：?
    //箱子在目标上：@
    private final char[][] map0 = new char[10][10];
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
            map1, map2, map3, map4, map5,
    };
    public Map(int n) {
        for(int i = 0; i < 10; i++) {
            Arrays.fill(map0[i], '.');
        }
        int y=5-map[n].length/2-map[n].length%2;
        int x=5-map[n][0].length/2-map[n][0].length%2;
        for (int i = 0; i < map[n].length; i++) {
            for (int j = 0; j < map[n][0].length; j++) {
                map0[i+y][j+x] = map[n][i][j];
            }
        }
    }
    public char[][] getMap() {
        return map0;
    }
    public int[] getPlayerPosition(){
        int x=0;
        int y=0;
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
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
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                if(map0[i][j]=='B'||map0[i][j]=='@'){
                    num++;
                }
            }
        }
        int[][] ints = new int[num][2];
        int n=0;
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
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
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                if(map0[i][j]=='T'||map0[i][j]=='@'||map0[i][j]=='?'){
                    num++;
                }
            }
        }
        int[][] ints = new int[num][2];
        int n=0;
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                if(map0[i][j]=='T'||map0[i][j]=='@'||map0[i][j]=='?'){
                    ints[n][0]=j;
                    ints[n][1]=i;
                    n++;
                }
            }
        }
        return ints;
    }
}
