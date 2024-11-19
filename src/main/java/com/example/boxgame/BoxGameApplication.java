package com.example.boxgame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
//import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.NotNull;

public class BoxGameApplication extends Application {

    public static final int GRID_SIZE = 50;
    public static final int GRID_COUNT = 10;
    public static final int STROKE_SIZE = 1;
    public static final int PUDDING_SIZE = 7;
    public static final int N = 4;
    public static final Color PlayerColor = Color.LIGHTBLUE;
    public static final Color BoxColor = Color.ORANGE;
    public static final Color PositionColor = Color.LIGHTGREEN;
    public static final Color GroundColor = Color.WHITE;
    public static final Color WallColor = Color.LIGHTGRAY;
    public static final Color StrokeColor = Color.GREY;
    private final Rectangle[][] grid = new Rectangle[GRID_COUNT][GRID_COUNT];
    private final Map map = new Map(N);
    private char[][] _map;
    private char[][] p_map;
    private Player player;
    private final int[] playerPosition = map.getPlayerPosition();
    private final int[][] Position = map.getPosition();
    private final int[][] boxesPosition = map.getBoxesPosition();
    private final int[][] wallPosition = map.getWallPosition();
    private final Box[] boxes = new Box[boxesPosition.length];
    private final Wall[] walls = new Wall[wallPosition.length];
    private int step = 0;
    private boolean check = true;
    private boolean check2 = true;

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(PUDDING_SIZE, PUDDING_SIZE, PUDDING_SIZE, PUDDING_SIZE)); // 为GridPane添加内边距

        for (int i = 0; i < GRID_COUNT; i++) {
            for (int j = 0; j < GRID_COUNT; j++) {
                Rectangle rect = new Rectangle(GRID_SIZE, GRID_SIZE, GroundColor);
                rect.setStroke(StrokeColor);
                rect.setStrokeWidth(STROKE_SIZE);
                gridPane.add(rect, i, j);
                grid[i][j] = rect; // 初始化 grid 数组
            }
        }

        Scene scene = new Scene(gridPane, GRID_COUNT * (GRID_SIZE+STROKE_SIZE)+STROKE_SIZE+PUDDING_SIZE*2, GRID_COUNT * (GRID_SIZE+STROKE_SIZE)+STROKE_SIZE+PUDDING_SIZE*2);
        primaryStage.setTitle("Sokoban Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // 禁止用户调整窗口大小
        primaryStage.setResizable(false);

        initializeGame();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }

    private void handleKeyPress(@NotNull KeyEvent event) {
        //System.out.println(event.getCode());
        // 检查是否按下了Ctrl+Z
        if (event.isControlDown() && event.getCode() == KeyCode.Z) {
            undoGame();
        }
        //移动操作
        switch (event.getCode()) {
            case UP:
                movePlayer(0, -1);
                Backup();
                break;
            case DOWN:
                movePlayer(0, 1);
                Backup();
                break;
            case LEFT:
                movePlayer(-1, 0);
                Backup();
                break;
            case RIGHT:
                movePlayer(1, 0);
                Backup();
                break;
            default:
                break;
        }

        //检测条件
        checkCondition();
    }

    private void initializeGame() {
        //初始化地图
        _map = map.getMap();
        //System.out.println("Initial game...");
        for (int i = 0; i < GRID_COUNT; i++) {
            for (int j = 0; j < GRID_COUNT; j++) {
                //grid[i][j] = new Rectangle(GRID_SIZE, GRID_SIZE, GroundColor);
                if(_map[i][j]=='#') {
                    grid[j][i].setFill(WallColor);
                }
                if(_map[i][j]=='.') {
                    grid[j][i].setFill(GroundColor);
                }
                if(_map[i][j]=='T') {
                    grid[j][i].setFill(PositionColor);
                }
                if(_map[i][j]=='B'||_map[i][j]=='@') {
                    grid[j][i].setFill(BoxColor);
                }
                if(_map[i][j]=='P'||_map[i][j]=='?') {
                    grid[j][i].setFill(PlayerColor);
                }
            }
        }

        // 假设玩家在起始位置，箱子在起始位置
        player = new Player(playerPosition[0], playerPosition[1]);
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new Box(boxesPosition[i][0], boxesPosition[i][1]);
        }
        for(int i = 0; i < walls.length; i++) {
            walls[i] = new Wall(wallPosition[i][0], wallPosition[i][1]);
        }


        // 将玩家和箱子和目标点添加到网格中
        Refresh();
    }

    private void Backup(){
        //Backup
        p_map = _map.clone();
        _map = Map.getMap0(grid);
    }

    private void undoGame() {
        Map tMap = new Map(p_map);
        int[] t_playerPosition = tMap.getPlayerPosition();
        int[][] t_boxesPosition = tMap.getBoxesPosition();
        int[][] t_wallPosition = tMap.getWallPosition();
        for (int i = 0; i < GRID_COUNT; i++) {
            for (int j = 0; j < GRID_COUNT; j++) {

                if(p_map[i][j]=='#') {
                    grid[j][i].setFill(WallColor);
                }
                if(p_map[i][j]=='.') {
                    grid[j][i].setFill(GroundColor);
                }
                if(p_map[i][j]=='T') {
                    grid[j][i].setFill(PositionColor);
                }
                if(p_map[i][j]=='B'||_map[i][j]=='@') {
                    grid[j][i].setFill(BoxColor);
                }
                if(p_map[i][j]=='P'||_map[i][j]=='?') {
                    grid[j][i].setFill(PlayerColor);
                }
            }
        }

        // 假设玩家在起始位置，箱子在起始位置
        player.setX(t_playerPosition[0]);
        player.setY(t_playerPosition[1]);
        player.setOldX(player.getX());
        player.setOldY(player.getY());
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].setX(t_boxesPosition[i][0]);
            boxes[i].setY(t_boxesPosition[i][1]);
            boxes[i].setOldX(boxes[i].getX());
            boxes[i].setOldY(boxes[i].getY());
        }
        for(int i = 0; i < walls.length; i++) {
            walls[i].setX(t_wallPosition[i][0]);
            walls[i].setY(t_wallPosition[i][1]);
            walls[i].setOldX(walls[i].getX());
            walls[i].setOldY(walls[i].getY());
        }

        // 将玩家和箱子和目标点添加到网格中
        Refresh();
    }

    private void Refresh() {
        // 将玩家和箱子和目标点添加到网格中
        for(int i = 0; i < GRID_COUNT; i++) {
            for(int j = 0; j < GRID_COUNT; j++) {
                grid[j][i].setFill(GroundColor);
            }
        }
        for(int[] targetPosition : Position ) {
            grid[targetPosition[0]][targetPosition[1]].setFill(PositionColor);
        }
        for(Box box : boxes) {
            grid[box.getX()][box.getY()].setFill(BoxColor);
        }
        for(Wall wall : walls) {
            grid[wall.getX()][wall.getY()].setFill(WallColor);
        }
        grid[player.getX()][player.getY()].setFill(PlayerColor);
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
        if (grid[newX][newY].getFill() == WallColor) {
            return;
        }

        // 检查是否推箱子
        if(grid[newX][newY].getFill() == BoxColor) {
            for(Box box : boxes) {
                if (newX == box.getX() && newY == box.getY()) {
                    // 检查是否越界
                    if (newX+dx < 0 || newX+dx >= GRID_COUNT || newY+dy < 0 || newY+dy >= GRID_COUNT) {
                        return;
                    }
                    // 检查是否移动到墙上
                    if (grid[newX+dx][newY+dy].getFill() == WallColor) {
                        return;
                    }
                    // 检查是否移动到箱子上
                    if (grid[newX+dx][newY+dy].getFill() == BoxColor) {
                        return;
                    }
                    // 移动箱子
                    box.move(dx, dy);
                    //grid[box.getOldX()][box.getOldY()].setFill(GroundColor);
                    //grid[box.getX()][box.getY()].setFill(BoxColor);
                }
            }
        }



        // 移动玩家
        player.move(dx, dy);
        //grid[player.getOldX()][player.getOldY()].setFill(GroundColor);
        Refresh();
    }

    private void checkCondition(){
        //检测是否完成
        if (step > 100&&checkWinCondition()&&check2&&!check) {
            alert("Game Over","You finished the game!");
            alert("Waring: Too many steps!");
            check = false;
            check2 = false;
        }
        // 检查是否胜利
        if (checkWinCondition()&&check) {
            alert("Victory","Congratulations! You win!");
            check = false;
        }
        //步数检测
        if(step > 100&&check) {
            alert("Failure","You lost!");
            alert("Reason: Too many steps!");
            check = false;
        }
    }

    private boolean checkWinCondition() {
        // 假设目标位置是一个二维数组，表示每个箱子应该在的位置
        int[][][] targetPositions = {
                Position // 箱子1的目标位置
        };
        int num = 0;
        for (int[][] targetPosition : targetPositions) {
            for(int[] target : targetPosition) {
                for(Box box : boxes) {
                    if (box.getX() == target[0] && box.getY() == target[1]) {
                        num++;
                        break;
                    }
                }

            }

        }
        return (num == boxesPosition.length);
    }

    private void alert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game exited!");
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