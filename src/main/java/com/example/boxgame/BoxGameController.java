package com.example.boxgame;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javafx.scene.input.KeyEvent;

public class BoxGameController extends Application {

    private static final int GRID_SIZE = 30;
    private static final int GRID_COUNT = 10;
    private Rectangle[][] grid = new Rectangle[GRID_COUNT][GRID_COUNT];
    private Player player;
    private Box box;

    @Override
    public void start(Stage primaryStage) {
        // ... 省略其他代码 ...

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP) {
                player.move(0, -1);
            } else if (event.getCode() == KeyCode.DOWN) {
                player.move(0, 1);
            } else if (event.getCode() == KeyCode.LEFT) {
                player.move(-1, 0);
            } else if (event.getCode() == KeyCode.RIGHT) {
                player.move(1, 0);
            }

            // 更新网格
            grid[player.getOldX()][player.getOldY()].setFill(Color.LIGHTGRAY);
            grid[player.getX()][player.getY()].setFill(Color.BLUE);

            // 检查箱子是否需要移动
            checkAndMoveBox();

            if (checkWinCondition()) {
                alert("Congratulations! You win!");
            }

        });
    }

    private void alert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void initializeGame() {
        // 假设玩家起始位置在(3,3)，箱子在(4,4)
        player = new Player(3, 3);
        box = new Box(4, 4);

        // 将玩家和箱子添加到网格中
        grid[player.getX()][player.getY()].setFill(Color.BLUE);
        grid[box.getX()][box.getY()].setFill(Color.GREEN);
    }

    private void checkAndMoveBox() {
        int playerDX = player.getX() - box.getX();
        int playerDY = player.getY() - box.getY();

        if (playerDX == 1 && playerDY == 0) { // 玩家在箱子的右侧
            box.move(1, 0);
        } else if (playerDX == -1 && playerDY == 0) { // 玩家在箱子的左侧
            box.move(-1, 0);
        } else if (playerDX == 0 && playerDY == 1) { // 玩家在箱子的下方
            box.move(0, 1);
        } else if (playerDX == 0 && playerDY == -1) { // 玩家在箱子的上方
            box.move(0, -1);
        }

        // 更新网格
        grid[box.getOldX()][box.getOldY()].setFill(Color.LIGHTGRAY);
        grid[box.getX()][box.getY()].setFill(Color.GREEN);
    }

    private boolean checkWinCondition() {
        // 假设目标位置是一个二维数组，表示每个箱子应该在的位置
        int[][] targetPositions = {
                {4, 4}, // 箱子1的目标位置
                // 可以添加更多箱子的目标位置
        };

        for (int i = 0; i < targetPositions.length; i++) {
            if (box.getX() != targetPositions[i][0] || box.getY() != targetPositions[i][1]) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}