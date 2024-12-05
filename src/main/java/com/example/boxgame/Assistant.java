package com.example.boxgame;

import java.util.ArrayList;
import java.util.Scanner;

public class Assistant {
    public static ArrayList<Integer> steps = new ArrayList<Integer>();
    public static void main(String[] args) {
        Map map = new Map(1);
        char[][] map1 = map.getMap();
        int[] playerPosition = map.getPlayerPosition();
        int[][] boxPosition = map.getBoxesPosition();
        int[][] targetPosition = map.getPosition();
        map1[playerPosition[0]][playerPosition[1]] = '.';
        for (int i = 0; i < map1.length; i++) {
            for (int j = 0; j < map1[i].length; j++) {
                if (map1[i][j] == 'T') {
                    map1[i][j] = '.';
                }
                if(map1[i][j]!='.'){
                    map1[i][j] = '#';
                }
            }
        }
        int[] target = new int[2];
        Scanner sc = new Scanner(System.in);
        target[0] = sc.nextInt();
        target[1] = sc.nextInt();
        sc.close();
        map1[playerPosition[0]][playerPosition[1]] = 'P';
        map1[target[0]][target[1]] = 'T';
        for (char[] chars : map1) {
            System.out.println(chars);
        }
        map1[playerPosition[0]][playerPosition[1]] = '.';
        map1[target[0]][target[1]] = '.';
        boolean result = findWays(map1, playerPosition, target);
        System.out.println(result);
        System.out.println(steps.size());
        if(result){
            for (Integer _step : steps) {
                switch (_step) {
                    case 1:
                        System.out.println("Up");
                        break;
                    case 2:
                        System.out.println("Down");
                        break;
                    case 3:
                        System.out.println("Left");
                        break;
                    case 4:
                        System.out.println("Right");
                        break;
                    default:
                        System.out.println("Wrong step");
                        break;
                }
            }
        }
        return;
    }
    public static boolean findWays(char[][] t_map, int[] player, int[] target) {
        if(player[0]==target[0]&&player[1]==target[1]){
            return true;
        }
        if(t_map[target[0]][target[1]]!='.'){
            return false;
        }
        t_map[player[0]][player[1]] = '*';
        if(t_map[player[0]-1][player[1]]=='.'){
            boolean bl=findWays(t_map, new int[]{player[0] - 1, player[1]}, target);
            if(bl){
                steps.add(0,1);
                System.out.println(1);
            }
            return bl;
        }else if(t_map[player[0]+1][player[1]]=='.'){
            boolean bl=findWays(t_map, new int[]{player[0] + 1, player[1]}, target);
            if(bl){
                steps.add(0,2);
            }
            return bl;
        }else if(t_map[player[0]][player[1]-1]=='.'){
            boolean bl=findWays(t_map, new int[]{player[0], player[1] - 1}, target);
            if(bl){
                steps.add(0,3);
            }
            return bl;
        }else if(t_map[player[0]][player[1]+1]=='.'){
            boolean bl=findWays(t_map, new int[]{player[0], player[1] + 1}, target);
            if(bl){
                steps.add(0,4);
            }
            return bl;
        }else{
            return false;
        }
    }
}
