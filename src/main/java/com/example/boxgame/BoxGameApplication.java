package com.example.boxgame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
//import javafx.scene.input.KeyCode;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class BoxGameApplication extends Application {

    public static final int GRID_SIZE = 50;
    public static final int GRID_COUNT = 10;
    public static final int STROKE_SIZE = 1;
    public static final int PUDDING_SIZE = 7;
    public static final int N = 5;
    public static final int M = 0;
    public static final Color PlayerColor = Color.LIGHTBLUE;
    public static final Color BoxColor = Color.ORANGE;
    public static final Color PositionColor = Color.LIGHTGREEN;
    public static final Color GroundColor = Color.WHITE;
    public static final Color WallColor = Color.LIGHTGRAY;
    public static final Color PressColor = Color.LIGHTCYAN;
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
    private Target[] targets = new Target[Position.length];
    private Box[] boxes = new Box[boxesPosition.length];
    private Wall[] walls = new Wall[wallPosition.length];
    private final ArrayList<Target> targetsList = new ArrayList<Target>();
    private final ArrayList<Box> boxesList = new ArrayList<Box>();
    private final ArrayList<Wall> wallsList = new ArrayList<Wall>();
    private int step = 0;
    private boolean check = true;
    private boolean check2 = true;
    private boolean checkUndo = false;
    private boolean checkRedo = false;
    private long startTime = 0; // 游戏开始时间
    private long elapsedTime = 0; // 已过时间
    private Label timeLabel;

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane0 = new GridPane();
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(PUDDING_SIZE, PUDDING_SIZE, PUDDING_SIZE, PUDDING_SIZE)); // 为GridPane添加内边距
        // 创建时间Label
        timeLabel = new Label();
        timeLabel.setId("timeLabel"); // 设置ID，方便CSS样式设置
        timeLabel.setText("Time: 00:00"); // 初始化时间显示
        // 将时间Label添加到GridPane
        gridPane0.add(timeLabel, 0, 0); // 将Label放在左上角
        gridPane0.add(gridPane, 0, 1);

        for (int i = 0; i < GRID_COUNT; i++) {
            for (int j = 0; j < GRID_COUNT; j++) {
                Rectangle rect = new Rectangle(GRID_SIZE, GRID_SIZE, GroundColor);
                rect.setStroke(StrokeColor);
                rect.setStrokeWidth(STROKE_SIZE);
                gridPane.add(rect, i, j);
                grid[i][j] = rect; // 初始化 grid 数组
            }
        }

        Scene scene = new Scene(gridPane0, GRID_COUNT * (GRID_SIZE+STROKE_SIZE)+STROKE_SIZE+PUDDING_SIZE*2, GRID_COUNT * (GRID_SIZE+STROKE_SIZE)+STROKE_SIZE+PUDDING_SIZE*2+40);
        primaryStage.setTitle("Sokoban Game");
        primaryStage.setResizable(false);// 禁止用户调整窗口大小
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("TimeStyle.css")).toExternalForm());
        // 设置关闭事件处理
        primaryStage.setOnCloseRequest(event -> {
            // 创建一个确认对话框
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "您确定要退出程序吗？");
            alert.setTitle("退出确认");
            alert.setHeaderText(null);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    event.consume(); // 用户选择“是”时，不关闭窗口
                } else {
                    // 用户选择“否”时，不执行任何操作，窗口保持打开状态
                }
            });
        });
        primaryStage.setScene(scene);
        primaryStage.show();

        initializeGame();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        // 创建并启动AnimationTimer
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = System.nanoTime();
            @Override
            public void handle(long currentNanoTime) {
                if(checkRedo){
                    lastTime = System.nanoTime();
                    checkRedo = false;
                }
                double seconds = (currentNanoTime - lastTime) / 1_000_000_000.0;
                //timeLabel.setText(String.format("Time: %.2f", seconds));
                elapsedTime = (long) seconds*1000;
                String timeString = String.format("Time: %02d:%02d:%02d", elapsedTime / 1000 / 3600, elapsedTime / 1000 %3600 / 60, (elapsedTime / 1000) % 60);
                timeLabel.setText(timeString);
            }
        };
        timer.start(); // 启动定时器
    }

    @Override
    public void init() {
        System.out.println("这是初始化方法");
    }

    @Override
    public void stop() throws Exception {
        System.out.println("这个是stop()方法");
    }

    private void handleKeyPress(@NotNull KeyEvent event) {
        // 更新已过时间
        elapsedTime = System.currentTimeMillis() - startTime;
        //System.out.println(event.getCode());
        // 检查是否按下了Ctrl+Z
        if(event.isControlDown()){
            if (event.getCode() == KeyCode.Z) {
                if(checkUndo){
                    undoGame();
                    checkUndo = false;
                }
            }
            if (event.getCode() == KeyCode.I) {
                initializeGame();
            }
        }

        //移动操作
        switch (event.getCode()) {
            case UP, KeyCode.W:
                movePlayer(0, -1, M);
                Backup();
                checkUndo = true;
                break;
            case DOWN, KeyCode.S:
                movePlayer(0, 1, M);
                Backup();
                checkUndo = true;
                break;
            case LEFT, KeyCode.A:
                movePlayer(-1, 0, M);
                Backup();
                checkUndo = true;
                break;
            case RIGHT, KeyCode.D:
                movePlayer(1, 0, M);
                Backup();
                checkUndo = true;
                break;
            default:
                break;
        }
        //检测条件
        checkCondition();
    }

    private void renderGame(Map tMap){
        //初始化地图
        char[][] t_map = tMap.getMap();
        int[] t_playerPosition = tMap.getPlayerPosition();
        int[][] t_Position = tMap.getPosition();
        int[][] t_boxesPosition = tMap.getBoxesPosition();
        int[][] t_wallPosition = tMap.getWallPosition();
        for (int i = 0; i < GRID_COUNT; i++) {
            for (int j = 0; j < GRID_COUNT; j++) {
                if(t_map[i][j]=='#') {
                    grid[j][i].setFill(WallColor);
                }
                if(t_map[i][j]=='.') {
                    grid[j][i].setFill(GroundColor);
                }
                if(t_map[i][j]=='T') {
                    grid[j][i].setFill(PositionColor);
                }
                if(t_map[i][j]=='B'||_map[i][j]=='@') {
                    grid[j][i].setFill(BoxColor);
                }
                if(t_map[i][j]=='P'||_map[i][j]=='?') {
                    grid[j][i].setFill(PlayerColor);
                }
            }
        }
        //初始化
        initializeList(t_playerPosition ,t_Position, t_boxesPosition, t_wallPosition);

        // 将玩家和箱子和目标点添加到网格中
        Refresh();
    }

    private void initializeList(int[] t_playerPosition, int[][] t_Position, int[][] t_boxesPosition, int[][] t_wallPosition) {
        player = new Player(t_playerPosition[0],t_playerPosition[1]);// 假设玩家在起始位置，箱子在起始位置
        boxes = new Box[t_boxesPosition.length];
        walls = new Wall[t_wallPosition.length];
        targets = new Target[t_Position.length];
        boxesList.clear();
        wallsList.clear();
        targetsList.clear();
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new Box(t_boxesPosition[i][0],t_boxesPosition[i][1]);
            //ArrayList Version
            boxesList.add(new Box(t_boxesPosition[i][0],t_boxesPosition[i][1]));
        }
        for(int i = 0; i < walls.length; i++) {
            walls[i] = new Wall(t_wallPosition[i][0],t_wallPosition[i][1]);
            //ArrayList Version
            wallsList.add(new Wall(t_wallPosition[i][0],t_wallPosition[i][1]));
        }
        for(int i = 0; i < targets.length; i++) {
            targets[i] = new Target(t_Position[i][0],t_Position[i][1]);
            //ArrayList Version
            targetsList.add(new Target(t_Position[i][0],t_Position[i][1]));
        }
    }

    private void initializeGame() {
        //初始化数据
        step = 0;
        check = true;
        check2 = true;
        checkUndo = false;
        checkRedo = true;
        // 初始化计时器
        startTime = System.currentTimeMillis();

        //初始化地图
        _map = map.getMap();

        // 假设玩家在起始位置，箱子在起始位置
        initializeList(playerPosition, Position, boxesPosition, wallPosition);
        // 将玩家和箱子和目标点添加到网格中
        renderGame(new Map(_map));

        //备份
        Backup();
    }

    private void Backup(){
        //Backup
        p_map = _map.clone();
        _map = Map.getMap0(grid,Target.getTargetPosition(targets));
    }

    private void undoGame() {
        renderGame(new Map(p_map));
        Backup();
    }

    private void Refresh() {
        // 将玩家和箱子和目标点添加到网格中
        for(int i = 0; i < GRID_COUNT; i++) {
            for(int j = 0; j < GRID_COUNT; j++) {
                grid[j][i].setFill(GroundColor);
            }
        }
        for(Target target : targets) {
            grid[target.getX()][target.getY()].setFill(PositionColor);
        }
        for(Box box : boxes) {
            grid[box.getX()][box.getY()].setFill(BoxColor);
        }
        for(Wall wall : walls) {
            grid[wall.getX()][wall.getY()].setFill(WallColor);
        }
        grid[player.getX()][player.getY()].setFill(PlayerColor);
    }

    private void movePlayer(int dx, int dy, int method) {
        step++;
        if(method==0){//正常推动
            int newX = player.getX() + dx;
            int newY = player.getY() + dy;

            // 检查是否越界
            if (newX < 0 || newX >= GRID_COUNT || newY < 0 || newY >= GRID_COUNT) {
                step--;
                return;
            }

            // 检查是否移动到墙上
            if (grid[newX][newY].getFill() == WallColor) {
                step--;
                return;
            }

            // 检查是否推箱子
            if(grid[newX][newY].getFill() == BoxColor) {
                for(Box box : boxes) {
                    if (newX == box.getX() && newY == box.getY()) {
                        // 检查是否越界
                        if (newX+dx < 0 || newX+dx >= GRID_COUNT || newY+dy < 0 || newY+dy >= GRID_COUNT) {
                            step--;
                            return;
                        }
                        // 检查是否移动到墙上
                        if (grid[newX+dx][newY+dy].getFill() == WallColor) {
                            step--;
                            return;
                        }
                        // 检查是否移动到箱子上
                        if (grid[newX+dx][newY+dy].getFill() == BoxColor) {
                            step--;
                            return;
                        }
                        // 移动箱子
                        box.move(dx, dy);
                        break;
                    }
                }
            }

            // 移动玩家
            player.move(dx, dy);
        }
        if(method==1){//正常拉动
            int newX = player.getX() + dx;
            int newY = player.getY() + dy;

            // 检查是否越界
            if (newX < 0 || newX >= GRID_COUNT || newY < 0 || newY >= GRID_COUNT) {
                step--;
                return;
            }

            // 检查是否移动到墙上
            if (grid[newX][newY].getFill() == WallColor) {
                step--;
                return;
            }

            //检查是否移动到箱子上
            if(grid[newX][newY].getFill() == BoxColor) {
                step--;
                return;
            }

            // 检查是否拉箱子
            if(grid[newX-dx*2][newY-dy*2].getFill() == BoxColor) {
                for(Box box : boxes) {
                    if (newX-dx*2 == box.getX() && newY-dy*2 == box.getY()) {
                        // 移动箱子
                        box.move(dx, dy);
                        break;
                    }
                }
            }

            // 移动玩家
            player.move(dx, dy);
        }
        if(method==2){//移形换位
            int newX = player.getX() + dx;
            int newY = player.getY() + dy;

            // 检查是否越界
            if (newX < 0 || newX >= GRID_COUNT || newY < 0 || newY >= GRID_COUNT) {
                step--;
                return;
            }

            // 检查是否移动到墙上
            if (grid[newX][newY].getFill() == WallColor) {
                step--;
                return;
            }

            // 检查是否推箱子
            if(grid[newX][newY].getFill() == BoxColor) {
                for(Box box : boxes) {
                    if (newX == box.getX() && newY == box.getY()) {
                        // 移动箱子
                        box.move(-dx, -dy);
                        break;
                    }
                }
            }

            // 移动玩家
            player.move(dx, dy);
        }
        if(method==3){//粘黏
            int newX = player.getX() + dx;
            int newY = player.getY() + dy;

            // 检查是否越界
            if (newX < 0 || newX >= GRID_COUNT || newY < 0 || newY >= GRID_COUNT) {
                step--;
                return;
            }

            // 检查是否移动到墙上
            if (grid[newX][newY].getFill() == WallColor) {
                step--;
                return;
            }

            // 检查是否推箱子
            if(grid[newX][newY].getFill() == BoxColor) {
                for(Box box : boxes) {
                    if (newX == box.getX() && newY == box.getY()) {
                        // 检查是否越界
                        if (newX+dx < 0 || newX+dx >= GRID_COUNT || newY+dy < 0 || newY+dy >= GRID_COUNT) {
                            step--;
                            return;
                        }
                        // 检查是否移动到墙上
                        if (grid[newX+dx][newY+dy].getFill() == WallColor) {
                            step--;
                            return;
                        }
                        // 检查是否移动到箱子上
                        if (grid[newX+dx][newY+dy].getFill() == BoxColor) {
                            step--;
                            return;
                        }
                        // 移动箱子
                        box.move(dx, dy);
                        break;
                    }
                }
            }
            // 检查是否拉箱子
            if(grid[newX-dx*2][newY-dy*2].getFill() == BoxColor) {
                for(Box box : boxes) {
                    if (newX-dx*2 == box.getX() && newY-dy*2 == box.getY()) {
                        // 移动箱子
                        box.move(dx, dy);
                        break;
                    }
                }
            }
            // 检查是否侧滑右箱子
            if(grid[newX-dx+dy][newY-dy+dx].getFill() == BoxColor) {
                for(Box box : boxes) {
                    if (newX-dx+dy == box.getX() && newY-dy+dx == box.getY()) {
                        // 检查是否越界
                        if (newX+dy < 0 || newX+dx >= GRID_COUNT || newY+dx < 0 || newY+dy >= GRID_COUNT) {
                            step--;
                            return;
                        }
                        // 检查是否移动到墙上
                        if (grid[newX+dy][newY+dx].getFill() == WallColor) {
                            step--;
                            return;
                        }
                        // 检查是否移动到箱子上
                        if (grid[newX+dy][newY+dx].getFill() == BoxColor) {
                            step--;
                            return;
                        }
                        // 移动箱子
                        box.move(dx, dy);
                        break;
                    }
                }
            }
            // 检查是否侧滑左箱子
            if(grid[newX-dx-dy][newY-dy-dx].getFill() == BoxColor) {
                for(Box box : boxes) {
                    if (newX-dx-dy == box.getX() && newY-dy-dx == box.getY()) {
                        // 检查是否越界
                        if (newX-dy < 0 || newX-dy >= GRID_COUNT || newY-dx < 0 || newY-dx >= GRID_COUNT) {
                            step--;
                            return;
                        }
                        // 检查是否移动到墙上
                        if (grid[newX-dy][newY-dx].getFill() == WallColor) {
                            step--;
                            return;
                        }
                        // 检查是否移动到箱子上
                        if (grid[newX-dy][newY-dx].getFill() == BoxColor) {
                            step--;
                            return;
                        }
                        // 移动箱子
                        box.move(dx, dy);
                        break;
                    }
                }
            }
            // 移动玩家
            player.move(dx, dy);
        }
        //刷新界面
        Refresh();
    }

    private void checkCondition(){
        //检测是否完成
        if (step > 100&&checkWinCondition()&&check2&&!check) {
            alert("Game Over","You finished the game!");
            alert("Waring: Too many steps!");
            check2 = false;
        }
        // 检查是否胜利
        if (checkWinCondition()&&check&&step<=100) {
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
        int num = 0;
        for(Target target : targets) {
            for (Box box : boxes) {
                if (box.getX() == target.getX() && box.getY() == target.getY()) {
                    num++;
                    break;
                }
            }
        }
        return (num == boxes.length);
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
    public void alert(AlertType type,String title,String message) {
        // 创建一个确认对话框
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        // 显示对话框并等待用户响应
        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

        // 如果用户选择了"是"，则关闭程序
        if (result == ButtonType.YES) {
            System.exit(0); // 关闭程序
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}