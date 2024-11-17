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

public class BoxGameController extends Application {

    private static final int GRID_SIZE = 50;
    private static final int GRID_COUNT = 10;
    private final Rectangle[][] grid = new Rectangle[GRID_COUNT][GRID_COUNT];
    private Player player;
    private final int[] playerPosition = {3, 3};
    private final int[][] Position = {{2,2},{5,7}};
    private final int[][] boxsPosition = {{4,4},{3,6}};
    private final Box[] boxes = new Box[boxsPosition.length];
    private int step = 0;
    private boolean check = true;

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        for (int i = 0; i < GRID_COUNT; i++) {
            for (int j = 0; j < GRID_COUNT; j++) {
                Rectangle rect = new Rectangle(GRID_SIZE, GRID_SIZE, Color.LIGHTGRAY);
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
        // 假设玩家在起始位置，箱子在起始位置
        player = new Player(playerPosition[0], playerPosition[1]);
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new Box(boxsPosition[i][0], boxsPosition[i][1]);
        }


        // 将玩家和箱子添加到网格中
        grid[player.getX()][player.getY()].setFill(Color.BLUE);
        for(Box box : boxes) {
            grid[box.getX()][box.getY()].setFill(Color.GRAY);
        }
        for(int[] targetPosition : Position ) {
            grid[targetPosition[0]][targetPosition[1]].setFill(Color.GREEN);
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
        if (grid[newX][newY].getFill() == Color.BLACK) {
            return;
        }

        //刷新目标点
        for(int[] targetPosition : Position ) {
            grid[targetPosition[0]][targetPosition[1]].setFill(Color.GREEN);
        }

        // 检查是否推箱子
        for(Box box : boxes) {
            if (newX == box.getX() && newY == box.getY()) {
                // 检查是否越界
                if (newX+dx < 0 || newX+dx >= GRID_COUNT || newY+dy < 0 || newY+dy >= GRID_COUNT) {
                    return;
                }
                // 检查是否移动到墙上
                if (grid[newX+dx][newY+dy].getFill() == Color.BLACK) {
                    return;
                }
                // 检查是否移动到箱子上
                if (grid[newX+dx][newY+dy].getFill() == Color.GRAY) {
                    return;
                }
                // 移动箱子
                grid[box.getOldX()][box.getOldY()].setFill(Color.LIGHTGRAY);
                box.move(dx, dy);
                grid[box.getX()][box.getY()].setFill(Color.GRAY);
            }
        }


        // 移动玩家
        player.move(dx, dy);
        grid[player.getOldX()][player.getOldY()].setFill(Color.LIGHTGRAY);
        for(int[] targetPosition : Position ) {
            grid[targetPosition[0]][targetPosition[1]].setFill(Color.GREEN);//重绘
        }
        for(Box box : boxes) {
            grid[box.getX()][box.getY()].setFill(Color.GRAY);//重绘
        }
        grid[player.getX()][player.getY()].setFill(Color.BLUE);
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
        if(num== boxsPosition.length){
            return true;
        }else{
            return false;
        }
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