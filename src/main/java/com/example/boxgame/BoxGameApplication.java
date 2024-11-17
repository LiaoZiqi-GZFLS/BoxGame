package com.example.boxgame;

import javafx.application.Application;
import javafx.scene.Scene;
//import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.NotNull;

public class BoxGameApplication extends Application {

    private static final int GRID_SIZE = 50;
    private static final int GRID_COUNT = 10;
    private final Rectangle[][] grid = new Rectangle[GRID_COUNT][GRID_COUNT];
    private final Map map = new Map();
    private Player player;
    private int[] playerPosition = {3, 3};
    private int[][] Position = {{2,2},{5,7}};
    private int[][] boxesPosition = {{4,4},{3,6}};
    private final Box[] boxes = new Box[boxesPosition.length];
    private int N = 0;
    private int step = 0;
    private boolean check = true;

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        for (int i = 0; i < GRID_COUNT; i++) {
            for (int j = 0; j < GRID_COUNT; j++) {
                Rectangle rect = new Rectangle(GRID_SIZE, GRID_SIZE, Color.WHITE);
                gridPane.add(rect, i, j);
                grid[i][j] = rect; // 初始化 grid 数组
            }
        }

        Scene scene = new Scene(gridPane, GRID_COUNT * GRID_SIZE, GRID_COUNT * GRID_SIZE);
        primaryStage.setTitle("Sokoban Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // 禁止用户调整窗口大小
        primaryStage.setResizable(false);

        initializeGame();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }

    private void handleKeyPress(@NotNull KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                movePlayer(0, -1);
                break;
            case DOWN:
                movePlayer(0, 1);
                break;
            case LEFT:
                movePlayer(-1, 0);
                break;
            case RIGHT:
                movePlayer(1, 0);
                break;
            default:
                break;
        }

        // 检查是否胜利
        if (checkWinCondition()&&check) {
            alert("Congratulations! You win!");
            check = false;
        }
        //步数检测
        if(step > 30&&check) {
            alert("Game Over","You lost!");
            check = false;
        }

    }

    private void initializeGame() {
        //初始化地图
        map.initMap(N);
        playerPosition = map.getPlayerPosition();
        boxesPosition = map.getBoxesPosition();
        Position = map.getPosition();
        char[][] _map = map.getMap();
        for (int i = 0; i < GRID_COUNT; i++) {
            for (int j = 0; j < GRID_COUNT; j++) {
                //grid[i][j] = new Rectangle(GRID_SIZE, GRID_SIZE, Color.WHITE);
                if(_map[i][j]=='#') {
                    grid[j][i].setFill(Color.LIGHTGRAY);
                }
                if(_map[i][j]=='.') {
                    grid[j][i].setFill(Color.WHITE);
                }
                if(_map[i][j]=='P'||_map[i][j]=='?') {
                    grid[j][i].setFill(Color.LIGHTBLUE);
                }
                if(_map[i][j]=='B'||_map[i][j]=='@') {
                    grid[j][i].setFill(Color.ORANGE);
                }
                if(_map[i][j]=='T') {
                    grid[j][i].setFill(Color.LIGHTGREEN);
                }
            }
        }

        // 假设玩家在起始位置，箱子在起始位置
        player = new Player(playerPosition[0], playerPosition[1]);
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new Box(boxesPosition[i][0], boxesPosition[i][1]);
        }


        // 将玩家和箱子添加到网格中
        grid[player.getX()][player.getY()].setFill(Color.LIGHTBLUE);
        for(Box box : boxes) {
            grid[box.getX()][box.getY()].setFill(Color.ORANGE);
        }
        for(int[] targetPosition : Position ) {
            grid[targetPosition[0]][targetPosition[1]].setFill(Color.LIGHTGREEN);
        }
    }

    private void movePlayer(int dx, int dy) {
        step++;
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;

        // 检查是否越界
        if (newX < 0 || newX >= GRID_COUNT || newY < 0 || newY >= GRID_COUNT) {
            return;
        }

        // 检查是否移动到墙上
        if (grid[newX][newY].getFill() == Color.LIGHTGRAY) {
            return;
        }

        //刷新目标点
        for(int[] targetPosition : Position ) {
            grid[targetPosition[0]][targetPosition[1]].setFill(Color.LIGHTGREEN);
        }

        // 检查是否推箱子
        for(Box box : boxes) {
            if (newX == box.getX() && newY == box.getY()) {
                // 检查是否越界
                if (newX+dx < 0 || newX+dx >= GRID_COUNT || newY+dy < 0 || newY+dy >= GRID_COUNT) {
                    return;
                }
                // 检查是否移动到墙上
                if (grid[newX+dx][newY+dy].getFill() == Color.LIGHTGRAY) {
                    return;
                }
                // 检查是否移动到箱子上
                if (grid[newX+dx][newY+dy].getFill() == Color.ORANGE) {
                    return;
                }
                // 移动箱子
                box.move(dx, dy);
                grid[box.getOldX()][box.getOldY()].setFill(Color.WHITE);
                grid[box.getX()][box.getY()].setFill(Color.ORANGE);
            }
        }


        // 移动玩家
        player.move(dx, dy);
        grid[player.getOldX()][player.getOldY()].setFill(Color.WHITE);
        for(int[] targetPosition : Position ) {
            grid[targetPosition[0]][targetPosition[1]].setFill(Color.LIGHTGREEN);//重绘
        }
        for(Box box : boxes) {
            grid[box.getX()][box.getY()].setFill(Color.ORANGE);//重绘
        }
        grid[player.getX()][player.getY()].setFill(Color.LIGHTBLUE);
    }

    private boolean checkWinCondition() {
        // 假设目标位置是一个二维数组，表示每个箱子应该在的位置
        int[][][] targetPositions = {
                Position // 箱子1的目标位置
        };
        int num = 0;
        for (int[][] targetPosition : targetPositions) {
            for(int[] target : targetPosition) {
                here:
                for(Box box : boxes) {
                    if (box.getX() == target[0] && box.getY() == target[1]) {
                        num++;
                        break here;
                    }
                }

            }

        }
        return (num == boxesPosition.length);
    }

    private void alert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Finished!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void alert(String title,String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}