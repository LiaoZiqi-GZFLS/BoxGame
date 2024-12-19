package com.example.boxgame;

import java.util.ArrayList;
import java.util.Random;

import static SokobanSolver.SokobanSolver.checkPossibility;

public class SokobanGenerator {
    public int row;
    public int col;
    public int numberOfBoxes;
    public char[][] map;
    public boolean[][] visited;
    public boolean[][] visited2;
    private int[][] list0 = new int[][]{{-1,0},{0,-1},{1,0},{0,1}};
    private final Random rand = new Random();
    private final int MAX_SIZE = 10;
    public SokobanGenerator(){
        row = rand.nextInt(MAX_SIZE/2)+MAX_SIZE/2;
        col = rand.nextInt(MAX_SIZE/2)+MAX_SIZE/2;
        numberOfBoxes = rand.nextInt(3)+1;
        map = new char[row][col];
        visited = new boolean[row][col];
        visited2 = new boolean[row][col];
    }
    public SokobanGenerator(int row, int col, int numberOfBoxes){
        this.row = row;
        this.col = col;
        this.numberOfBoxes = numberOfBoxes;
        map = new char[row][col];
        visited = new boolean[row][col];
        visited2 = new boolean[row][col];
    }
    public void initMap(){
        for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                map[i][j] = '.';
            }
        }
    }
    public void initVisited(boolean tf){
        for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                if(tf){
                    visited[i][j] = false;
                }else {
                    visited2[i][j] = false;
                }
            }
        }
    }
    public void buildWall(){
        for (int i=0; i<row; i++){
            map[i][0]=map[i][col-1]='#';
        }
        for (int j=0; j<col; j++){
            map[0][j]=map[row-1][j]='#';
        }
    }
    public void buildObject(){
        int playerX = 2;
        int playerY = 2;
        int n = row*col;
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0; i<n; i++){
            list.add(i);
        }
        while(!list.isEmpty()){
            int index = rand.nextInt(list.size());
            int x = list.get(index)/col;
            int y = list.get(index)/row;
            if(!((x==0||x==row-1)||(y==0||y==col-1))&&map[x][y]=='.'){
                playerX = x;
                playerY = y;
                map[playerX][playerY] = 'P';
                break;
            }else {
                list.remove(index);
            }
        }
        visited[playerX][playerY] = true;
        System.out.println("Player: "+playerX+" "+playerY);
        int boxX = 1;
        int boxY = 1;
        for(int i=1; i<=numberOfBoxes; i++){
            list = new ArrayList<>();
            for (int j=0; j<n; j++){
                list.add(j);
            }
            while (!list.isEmpty()){
                int index = rand.nextInt(list.size());
                int x = list.get(index)/col;
                int y = list.get(index)/row;
                if(!((x<=1||x>=row-2)&&(y<=1||y>=col-2))&&map[x][y]=='.'){
                    map[x][y] = 'B';
                    boxX = x;
                    boxY = y;
                    System.out.println("Box "+i+" : "+x+" "+y);
                    break;
                }else {
                    list.remove(index);
                }
            }
            visited[boxX][boxY] = true;
            int len = rand.nextInt((row+col)/2-2)+1;
            while(len-->0){
                ArrayList<int[]> list2 = new ArrayList<>();
                for(int[] l: list0){
                    list2.add(l);
                }
                while (!list2.isEmpty()){
                    int index = rand.nextInt(list2.size());
                    int[] l = list2.get(index);
                    int tx1 = boxX+l[0];
                    int ty1 = boxY+l[1];
                    int tx2 = boxX-l[0];
                    int ty2 = boxY-l[1];
                    if(map[tx1][ty1]=='.'&&map[tx2][ty2]=='.'&& !visited[tx1][ty1]){
                        initVisited(false);
                        if(playerGetHere(playerX,playerY,tx2,ty2)){
                            visited[tx1][ty1] = true;
                            boxX = tx1;
                            boxY = ty1;
                            playerX = tx2;
                            playerY = ty2;
                            break;
                        }else {
                            list2.remove(index);
                        }
                    }else{
                        list2.remove(index);
                    }
                }
                if (list2.isEmpty()){
                    break;
                }
            }
            if(map[boxX][boxY]=='.'){
                map[boxX][boxY] = 'T';
            }else if(map[boxX][boxY]=='B'){
                map[boxX][boxY] = '@';
            }else if(map[boxX][boxY]=='P'){
                map[boxX][boxY] = '?';
            }else {
                System.out.println("Error: "+map[boxX][boxY]);
            }

        }
    }
    public boolean playerGetHere(int x1,int y1,int x2,int y2){
        visited2[x1][y1] = true;
        if(x1==x2&&y1==y2){
            visited[x1][y1] = true;
            return true;
        }
        for(int[] l: list0){
            int tx = x1+l[0];
            int ty = y1+l[1];
            if(map[tx][ty]=='.'&& !visited2[tx][ty]){
                if(playerGetHere(tx,ty,x2,y2)){
                    visited[x1][y1] = true;
                    return true;
                }
            }
        }
        visited2[x1][y1] = false;
        return false;
    }
    public void generateWalls(){
        for (int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                if(map[i][j]=='.'&&!visited[i][j]){
                    int tf = rand.nextInt(100);
                    if(tf>rand.nextInt(50)+40){
                        map[i][j] = '#';
                    }
                }
            }
        }
    }
    public void printMap(){
        for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
    public char[][] getMap(){
        return map;
    }
    public void printVisited(){
        for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                if(visited[i][j]){
                    System.out.print("1");
                }else {
                    System.out.print("0");
                }
            }
            System.out.println();
        }
    }
    public void generate(){
        initMap();
        initVisited(true);
        initVisited(false);
        buildWall();
        buildObject();
        generateWalls();
        printMap();
        printVisited();
    }
    public void mainGenerator(){
        int attempt = 0;
        do{
            generate();
        }while (!checkPossibility(map)&&attempt++<50);
        if(attempt>=20){
            System.out.println("Error: can't generate a valid map");
        }
    }
}
