package com.example.boxgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BoxGameController extends Application {

    private static final int GRID_SIZE = 30;
    private static final int GRID_COUNT = 10;
    private final Rectangle[][] grid = new Rectangle[GRID_COUNT][GRID_COUNT];
    private Player player;
    private Box box;

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

        initializeGame();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }

    private void handleKeyPress(KeyEvent event) {
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

        // 检查箱子是否需要移动
        checkAndMoveBox();

        // 检查是否胜利
        if (checkWinCondition()) {
            alert("Congratulations! You win!");
        }
    }

    private void initializeGame() {
        // 假设玩家起始位置在(3,3)，箱子在(4,4)
        player = new Player(3, 3);
        box = new Box(4, 4);

        // 将玩家和箱子添加到网格中
        grid[player.getX()][player.getY()].setFill(Color.BLUE);
        grid[box.getX()][box.getY()].setFill(Color.GREEN);
    }

    private void movePlayer(int dx, int dy) {
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

        // 检查是否推箱子
        if (newX == box.getX() && newY == box.getY()) {
            int boxNewX = box.getX() + dx;
            int boxNewY = box.getY() + dy;

            // 检查箱子是否越界
            if (boxNewX < 0 || boxNewX >= GRID_COUNT || boxNewY < 0 || boxNewY >= GRID_COUNT) {
                return;
            }

            // 检查箱子是否移动到墙上
            if (grid[boxNewX][boxNewY].getFill() == Color.BLACK) {
                return;
            }

            // 移动箱子
            grid[box.getOldX()][box.getOldY()].setFill(Color.LIGHTGRAY);
            box.move(dx, dy);
            grid[box.getX()][box.getY()].setFill(Color.GREEN);
        }

        // 移动玩家
        grid[player.getOldX()][player.getOldY()].setFill(Color.LIGHTGRAY);
        player.move(dx, dy);
        grid[player.getX()][player.getY()].setFill(Color.BLUE);
    }

    private void checkAndMoveBox() {
        // 这个方法已经在 handleKeyPress 中处理
    }

    private boolean checkWinCondition() {
        // 假设目标位置是一个二维数组，表示每个箱子应该在的位置
        int[][] targetPositions = {
                {2, 2} // 箱子1的目标位置
        };

        for (int[] targetPosition : targetPositions) {
            if (box.getX() != targetPosition[0] || box.getY() != targetPosition[1]) {
                return false;
            }
        }

        return true;
    }

    private void alert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Finished!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}