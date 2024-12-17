package com.example.boxgame;

import java.util.Random;

import static SokobanSolver.SokobanSolver.checkPossibility;

public class SokobanGenerator {

    private static final int MAX_SIZE = 10;
    private static final char WALL = '#';
    private static final char EMPTY = '.';
    private static final char PLAYER = 'P';
    private static final char BOX = 'B';
    private static final char TARGET = 'T';
    private static final char PLAYER_ON_TARGET = '?';
    private static final char BOX_ON_TARGET = '@';

    private char[][] map;
    private int size;
    private static Random random = new Random();

    public SokobanGenerator(int size) {
        this.size = size;
        this.map = new char[size][size];
        random = new Random();
    }

    public SokobanGenerator() {
        this(5+random.nextInt(MAX_SIZE-5));
        int numberOfBoxes = random.nextInt(3) + 1; // Randomly choose 1, 2, or 3 boxes
        this.generateMap(numberOfBoxes);
        this.printMap();
    }

    public char[][] getMap() {
        return map;
    }

    private void initializeMap() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = EMPTY;
            }
        }
    }

    private void placeWalls() {
        // Place walls around the map
        for (int i = 0; i < size; i++) {
            map[0][i] = map[size - 1][i] = WALL;
            map[i][0] = map[i][size - 1] = WALL;
        }
    }

    private boolean placePlayerAndTarget(int numberOfBoxes) {
        int attempts = 0;
        while (attempts < 100) { // Limit attempts to avoid infinite loop
            int playerX = random.nextInt(size - 2) + 1;
            int playerY = random.nextInt(size - 2) + 1;

            if (map[playerX][playerY] == EMPTY) {
                map[playerX][playerY] = PLAYER;

                for (int i = 0; i < numberOfBoxes; i++){
                    int targetX = random.nextInt(size - 2) + 1;
                    int targetY = random.nextInt(size - 2) + 1;
                    while (targetX == playerX && targetY == playerY) {
                        targetX = random.nextInt(size - 2) + 1;
                        targetY = random.nextInt(size - 2) + 1;
                    }

                    if (map[targetX][targetY] == EMPTY) {
                        map[targetX][targetY] = TARGET;
                    }else{
                        i--;
                    }
                }
                return true;
            }
            attempts++;
        }
        return false;
    }

    private boolean placeOrNot(int dx,int dy){
        if(map[dx][dy]!=EMPTY){
            return false;
        }
        if(map[dx+1][dy]==WALL&&map[dx][dy+1]==WALL||map[dx-1][dy]==WALL&&map[dx][dy-1]==WALL||map[dx-1][dy]==WALL&&map[dx][dy+1]==WALL||map[dx+1][dy]==WALL&&map[dx][dy-1]==WALL){
            return false;
        }
        if(dx==1||dx==size-2||dy==1||dy==size-2){
            return false;
        }
        return true;
    }

    private boolean placeBoxes(int numberOfBoxes) {
        for (int i = 0; i < numberOfBoxes; i++) {
            int attempts = 0;
            while (attempts < 100) {
                attempts++;
                int boxX = random.nextInt(size - 2) + 1;
                int boxY = random.nextInt(size - 2) + 1;

                if (placeOrNot(boxX, boxY)) {
                    map[boxX][boxY] = BOX;
                    break; // Break the loop as we found a valid position
                }
                attempts++;
            }
        }
        return true;
    }

    public void generateMap(int numberOfBoxes) {
        initializeMap();
        placeWalls();
        if (!placePlayerAndTarget(numberOfBoxes)) {
            System.out.println("Failed to place player and target.");
            generateMap(1);
            return;
        }
        if (!placeBoxes(numberOfBoxes)) {
            System.out.println("Failed to place boxes.");
            generateMap(1);
            return;
        }
        generateWalls();

    }

    public void generateWalls() {
        int n = random.nextInt(8) + 3;
        int x,y;

        here:
        for (int i = 0; i < n; i++) {
            do{
                x=random.nextInt(size - 2) + 1;
                y=random.nextInt(size - 2) + 1;
            }while (map[x][y]!=EMPTY);
            if(map[x][y]==EMPTY){
                map[x][y] = WALL;
            }else {
                i--;
            }
            int tf = random.nextInt(4);
            int oldX = x;
            int oldY = y;
            switch (tf){
                case 0:
                    x++;
                    break;
                case 1:
                    x--;
                    break;
                case 2:
                    y++;
                    break;
                case 3:
                    y--;
                    break;
            }
            if(map[x][y]==EMPTY){
                map[x][y] = WALL;
            }else {
                x = oldX;
                y = oldY;
                for(int k=1;k<4;k++){
                    int tf2 = (tf+k)%4;
                    switch (tf2){
                        case 0:
                            x++;
                            break;
                        case 1:
                            x--;
                            break;
                        case 2:
                            y++;
                            break;
                        case 3:
                            y--;
                            break;
                    }
                    if(map[x][y]==EMPTY){
                        map[x][y] = WALL;
                        continue;
                    }else {
                        x = oldX;
                        y = oldY;
                        break here;
                    }
                }
            }
            if(!checkPossibility(new Map(map).getMap())){
                map[x][y] = EMPTY;
            }
        }
    }

    public void printMap() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void mainGenerator() {
        SokobanGenerator generator = new SokobanGenerator(5+random.nextInt(MAX_SIZE-5));
        int numberOfBoxes = random.nextInt(3) + 1; // Randomly choose 1, 2, or 3 boxes
        generator.generateMap(numberOfBoxes);
        generator.printMap();
    }
}
