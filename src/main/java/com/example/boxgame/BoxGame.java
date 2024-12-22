package com.example.boxgame;

import SokobanSolver.SokobanSolver;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static com.example.boxgame.Helper.*;
import static com.example.boxgame.Play.*;
import static com.example.boxgame.Welcome.*;

public class BoxGame{

    public static final int GRID_SIZE = 50;
    public static final int GRID_COUNT = 10;
    public static final int STROKE_SIZE = 1;
    public static final int PUDDING_SIZE = 7;
    public static final int MAX_STEP = 100;
    public static int N = 1;
    public static int M = 0;
    public static final Color PlayerColor = Color.LIGHTBLUE;
    public static final Color BoxColor = Color.ORANGE;
    public static final Color PositionColor = Color.LIGHTGREEN;
    public static final Color GroundColor = Color.WHITE;
    public static final Color WallColor = Color.LIGHTGRAY;
    public static final Color PressColor = Color.LIGHTCYAN;
    public static final Color StrokeColor = Color.GREY;
    public static Label timeLabel;
    public static Label stepLabel;
    public static Label scoreLabel;
    public static Rectangle[][] grid = new Rectangle[GRID_COUNT][GRID_COUNT];//Rectangle(x,y) int[y][x]
    private static Map map = new Map(N);

    public static char[][] _map;
    public static char[][] p_map;
    public static Player player = new Player();
    private static int[] playerPosition = map.getPlayerPosition();
    private static int[][] Position = map.getPosition();
    private static int[][] boxesPosition = map.getBoxesPosition();
    private static int[][] wallPosition = map.getWallPosition();
    public static Target[] targets = new Target[Position.length];
    public static Box[] boxes = new Box[boxesPosition.length];
    public static Wall[] walls = new Wall[wallPosition.length];
    private static ArrayList<Target> targetsList = new ArrayList<Target>();
    private static ArrayList<Box> boxesList = new ArrayList<Box>();
    private static ArrayList<Wall> wallsList = new ArrayList<Wall>();
    public static int step =0;
    public static int score =0;
    private static boolean check = true;
    private static boolean check2 = true;
    public static boolean checkUndo = false;
    public static boolean checkRedo = false;
    public static boolean checkGameOver = false;
    private static long startTime = 0; // 游戏开始时间
    public static long elapsedTime = 0; // 已过时间
    public static String solverPath;
    public static boolean calFinish = true;
    public static boolean calSuccess = false;
    public static boolean checkAI = false;
    public static boolean iceAndFire = true;

    public static ExecutorService executorService = Executors.newFixedThreadPool(8); // 创建固定大小的线程池
    static List<MyTask> tasks = new ArrayList<>();

    public static void start(Stage primaryStage) {

        GridPane gridPane0 = new GridPane();
        GridPane gridPane = new GridPane();
        GridPane gridPane1 = new GridPane();
        gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(PUDDING_SIZE, PUDDING_SIZE, PUDDING_SIZE, PUDDING_SIZE)); // 为GridPane添加内边距
        // 创建时间Label
        timeLabel = new Label();
        timeLabel.setId("timeLabel"); // 设置ID，方便CSS样式设置
        timeLabel.setText("Time: 00:00:00"); // 初始化时间显示
        // 创建步数Label
        stepLabel = new Label();
        stepLabel.setId("stepLabel"); // 设置ID，方便CSS样式设置
        stepLabel.setText("Step: 0"); // 初始化时间显示
        // 创建分数Label
        scoreLabel = new Label();
        scoreLabel.setId("scoreLabel"); // 设置ID，方便CSS样式设置
        scoreLabel.setText("Score: 00.00%"); // 初始化时间显示
        // 将Label添加到GridPane
        gridPane1.add(timeLabel, 0, 0); // 将Label放在左上角
        gridPane1.add(stepLabel, 1, 0);
        gridPane1.add(scoreLabel, 2, 0);
        gridPane0.add(gridPane1, 0, 0); // 将Label放在左上角
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
        scene.getStylesheets().add(Objects.requireNonNull(BoxGame.class.getResource("TimeStyle.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(BoxGame.class.getResource("StepStyle.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(BoxGame.class.getResource("ScoreStyle.css")).toExternalForm());
        // 设置关闭事件处理
        primaryStage.setOnCloseRequest(event -> {
            // 创建一个确认对话框
            Alert alert = new Alert(AlertType.CONFIRMATION, "您确定要退出程序吗？");
            alert.setTitle("退出确认");
            alert.setHeaderText(null);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // 用户选择“是”时，关闭窗口
                    return;
                } else {
                    // 用户选择“否”时，不执行任何操作，窗口保持打开状态
                    event.consume();
                }
            });
        });
        primaryStage.setScene(scene);
        primaryStage.show();

        initializeGame();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, BoxGame::handleKeyPress);
        // 创建并启动AnimationTimer
        AnimationTimer timer = new AnimationTimer() {
            private static long lastTime = System.nanoTime();
            @Override
            public void handle(long currentNanoTime) {
                if(checkRedo){
                    lastTime = System.nanoTime();
                    checkRedo = false;
                }
                if(checkGameOver){
                    String scoreString = String.format("Score: %02d.%02d%%", score/100, score%100);
                    scoreLabel.setText(scoreString);
                    return;
                }
                double seconds = (currentNanoTime - lastTime) / 1_000_000_000.0;
                //timeLabel.setText(String.format("Time: %.2f", seconds));
                elapsedTime = (long) seconds*1000;
                String timeString = String.format("Time: %02d:%02d:%02d", elapsedTime / 1000 / 3600, elapsedTime / 1000 %3600 / 60, (elapsedTime / 1000) % 60);
                String scoreString = String.format("Score: %02d.%02d%%", score/100, score%100);
                timeLabel.setText(timeString);
                stepLabel.setText("Step: " + step);
                scoreLabel.setText(scoreString);
            }
        };
        timer.start(); // 启动定时器
    }

    public static void handleKeyPress(@NotNull KeyEvent event) {
        // 更新已过时间
        elapsedTime = System.currentTimeMillis() - startTime;
        //System.out.println(event.getCode());
        // 检查是否按下了Ctrl+Z
        if(event.isControlDown()){
            switch (event.getCode()) {
                case KeyCode.I:
                    initializeGame();
                    break;
                case KeyCode.Z:
                    if(checkUndo){
                        undoGame();
                        checkUndo = false;
                    }
                    break;
                case KeyCode.H:
                    if(calFinish){
                        calFinish = false;
                        checkAI = true;
                        submitTask();
                    }
                    //System.out.println(cal(new Map(_map).toString()));
                    break;
                default:
                    break;
            }
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
                playsound(playervol);
                movePlayer(0, -1, M);
                Backup();
                checkUndo = true;
                break;
            case DOWN, KeyCode.S:
                playsound(playervol);
                movePlayer(0, 1, M);
                Backup();
                checkUndo = true;
                break;
            case LEFT, KeyCode.A:
                playsound(playervol);
                movePlayer(-1, 0, M);
                Backup();
                checkUndo = true;
                break;
            case RIGHT, KeyCode.D:
                playsound(playervol);
                movePlayer(1, 0, M);
                Backup();
                checkUndo = true;
                break;
            default:
                if(iceAndFire){
                    playsound(playervol);
                    movePlayer(0, 0, M);
                    Backup();
                    checkUndo = true;
                }
                break;
        }
        //检测条件
        checkCondition();
    }

    static int psound = 1;
    public static void playsound(double s){
        if(playerskin0==1){
            if(M==1){
                switch (psound){
                    case 1:
                        MusicManager.playSound12(s);
                        psound+=1;
                        break;
                    case 2:
                        MusicManager.playSound14(s);
                        psound+=1;
                        break;
                    case 3:
                        MusicManager.playSound11(s);
                        psound+=1;
                        break;
                    case 4:
                        MusicManager.playSound11(s);
                        psound=1;
                        break;
                }
            }else{
                switch (psound){
                    case 1:
                        MusicManager.playSound11(s);
                        psound+=1;
                        break;
                    case 2:
                        MusicManager.playSound12(s);
                        psound+=1;
                        break;
                    case 3:
                        MusicManager.playSound13(s);
                        psound+=1;
                        break;
                    case 4:
                        MusicManager.playSound14(s);
                        psound=1;
                        break;
                }
            }

        }else if(playerskin0==0){
            MusicManager.playSound01(s);
        }

    }
    public static void renderGame(Map tMap){
        played = true;
        times++;

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
                if(t_map[i][j]=='B'||t_map[i][j]=='@') {
                    grid[j][i].setFill(BoxColor);
                }
                if(t_map[i][j]=='P'||t_map[i][j]=='?') {
                    grid[j][i].setFill(PlayerColor);
                }
            }
        }
        //初始化
        initializeList(t_playerPosition ,t_Position, t_boxesPosition, t_wallPosition);

        // 将玩家和箱子和目标点添加到网格中
        Refresh();
    }

    public static void initElement(int n){
        map = new Map(n);
        grid = new Rectangle[GRID_COUNT][GRID_COUNT];
        Player player = new Player();
        playerPosition = map.getPlayerPosition();
        Position = map.getPosition();
        boxesPosition = map.getBoxesPosition();
        wallPosition = map.getWallPosition();
        targets = new Target[Position.length];
        boxes = new Box[boxesPosition.length];
        walls = new Wall[wallPosition.length];
        targetsList = new ArrayList<Target>();
        boxesList = new ArrayList<Box>();
        wallsList = new ArrayList<Wall>();
        step = 0;
        score = 0;
        check = true;
        check2 = true;
        checkUndo = false;
        checkRedo = false;
        checkGameOver = false;
        startTime = 0; // 游戏开始时间
        elapsedTime = 0; // 已过时间
        calFinish = true;
        calSuccess = false;
        checkAI = false;
        if(fromcontinuebtn==1){
            map = new Map(currentmap);//读档
        }
    }

    public static void initializeList(int[] t_playerPosition, int[][] t_Position, int[][] t_boxesPosition, int[][] t_wallPosition) {
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

    public static void initializeGame() {
        //初始化数据
        step = currentstep;
        score = 0;
        check = true;
        check2 = true;
        checkUndo = false;
        checkRedo = true;
        checkGameOver = false;
        calFinish = true;
        calSuccess = false;
        checkAI = false;
        playerImageStage = 0;
        boxImageStage = 0;
        // 初始化计时器
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
        //初始化地图
        _map = map.getMap();

        // 假设玩家在起始位置，箱子在起始位置
        initializeList(playerPosition, Position, boxesPosition, wallPosition);
        // 将玩家和箱子和目标点添加到网格中
        renderGame(new Map(_map));
        setScore();
        //备份
        Backup();
    }

    public static void Backup(){
        //Backup
        p_map = _map.clone();
        _map = Map.getMap0(grid,Target.getTargetPosition(targets));
    }

    public static void undoGame() {
        step--;
        renderGame(new Map(p_map));
        Backup();
    }

    public static void Refresh() {
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

    private static boolean checkEdge(int x, int y) {
        return (x < 0 || x >= GRID_COUNT || y < 0 || y >= GRID_COUNT);
    }
    private static boolean checkWall(int x, int y) {
        return (grid[x][y].getFill() == WallColor);
    }
    private static boolean checkBox(int x, int y) {
        return (grid[x][y].getFill() == BoxColor);
    }
    private static boolean frontOfPlayer(int dx, int dy) {
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;
        // 检查是否越界
        if (checkEdge(newX, newY)) {
            return false;
        }
        // 检查是否移动到墙上
        if (checkWall(newX, newY)) {
            return false;
        }
        // 检查是否推箱子
        if(checkBox(newX, newY)) {
            for(Box box : boxes) {
                if (newX == box.getX() && newY == box.getY()) {
                    // 检查是否越界
                    if (checkEdge(newX+dx, newY+dy)) {
                        return false;
                    }
                    // 检查是否移动到墙上
                    if (checkWall(newX+dx, newY+dy)) {
                        return false;
                    }
                    // 检查是否移动到箱子上
                    if (checkBox(newX+dx, newY+dy)) {
                        return false;
                    }
                    // 移动箱子
                    box.move(dx, dy);
                    break;
                }
            }
        }
        return true;
    }
    private static boolean backOfPlayer(int dx, int dy) {
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;
        // 检查是否越界
        if (checkEdge(newX, newY)) {
            return false;
        }
        // 检查是否移动到墙上
        if (checkWall(newX, newY)) {
            return false;
        }
        //检查是否移动到箱子上
        if(checkBox(newX, newY)) {
            return false;
        }
        // 检查是否拉箱子
        if(checkBox(newX-dx*2, newY-dy*2)) {
            for(Box box : boxes) {
                if (newX-dx*2 == box.getX() && newY-dy*2 == box.getY()) {
                    // 移动箱子
                    box.move(dx, dy);
                    break;
                }
            }
        }
        return true;
    }
    private static boolean leftOfPlayer(int dx, int dy) {
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;
        // 检查是否越界
        if (checkEdge(newX, newY)) {
            return false;
        }
        // 检查是否移动到墙上
        if (checkWall(newX, newY)) {
            return false;
        }
        int tx = newX-dx-Math.abs(dy);
        int ty = newY-dy-Math.abs(dx);
        // 检查是否侧滑箱子
        if(checkBox(tx, ty)) {
            for(Box box : boxes) {
                if (tx == box.getX() && ty == box.getY()) {
                    // 检查是否越界
                    if (checkEdge(tx+dx, ty+dy)) {
                        return false;
                    }
                    // 检查是否移动到墙上
                    if (checkWall(tx+dx, ty+dy)) {
                        return false;
                    }
                    // 检查是否移动到箱子上
                    if (checkBox(tx+dx, ty+dy)) {
                        return false;
                    }
                    // 移动箱子
                    box.move(dx, dy);
                    break;
                }
            }
        }
        return true;
    }
    private static boolean rightOfPlayer(int dx, int dy) {
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;
        // 检查是否越界
        if (checkEdge(newX, newY)) {
            return false;
        }
        // 检查是否移动到墙上
        if (checkWall(newX, newY)) {
            return false;
        }
        // 检查是否侧滑箱子
        int tx = newX-dx+Math.abs(dy);
        int ty = newY-dy+Math.abs(dx);
        if(checkBox(tx, ty)) {
            for(Box box : boxes) {
                if (tx == box.getX() && ty == box.getY()) {
                    // 检查是否越界
                    if (checkEdge(tx+dx, ty+dy)) {
                        return false;
                    }
                    // 检查是否移动到墙上
                    if (checkWall(tx+dx, ty+dy)) {
                        return false;
                    }
                    // 检查是否移动到箱子上
                    if (checkBox(tx+dx, ty+dy)) {
                        return false;
                    }
                    // 移动箱子
                    box.move(dx, dy);
                    break;
                }
            }
        }
        return true;
    }
    private static boolean transPosition(int dx, int dy) {
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;
        // 检查是否越界
        if (checkEdge(newX, newY)) {
            return false;
        }
        // 检查是否移动到墙上
        if (checkWall(newX, newY)) {
            return false;
        }
        // 检查是否换箱子
        if(checkBox(newX, newY)) {
            for(Box box : boxes) {
                if (newX == box.getX() && newY == box.getY()) {
                    // 移动箱子
                    box.move(-dx, -dy);
                    break;
                }
            }
        }
        return true;
    }

    public static void movePlayer(int dx, int dy, int method) {
        int p_step = step;
        if(iceAndFire){
            double angleNum = ((turningStep) / 240.00) % 1;
            dx=0;
            dy=0;
            System.out.println(angleNum);
            double[] nums = new double[4];
            double min = 1.0;
            for (int i = 0; i < 4; i++){
                nums[i] = Math.abs(angleNum-0.25*i);
                min = Math.min(min, nums[i]);
            }
            if(min<0.2){
                if(nums[0]==min){
                    dx=1;
                    dy=0;
                }else if(nums[1]==min){
                    dx=0;
                    dy=1;
                }else if (nums[2]==min){
                    dx=-1;
                    dy=0;
                }else if (nums[3]==min){
                    dx=0;
                    dy=-1;
                }else {
                    return;
                }
            }
        }
        step++;
        playerImageStage=6;
        if(method==0){//正常推动
            if(frontOfPlayer(dx,dy)) {
                player.move(dx, dy);
            }else{
                step--;
            }
        }
        if(method==1){//正常拉动
            if(backOfPlayer(dx,dy)) {
                player.move(dx, dy);
            }else{
                step--;
            }
        }
        if(method==2){//移形换位
            if(transPosition(dx,dy)) {
                player.move(dx, dy);
            }else{
                step--;
            }
        }
        if(method==3){//粘黏
            if(leftOfPlayer(dx,dy)&&rightOfPlayer(dx,dy)&&frontOfPlayer(dx,dy)) {
                backOfPlayer(dx,dy);
                player.move(dx, dy);
            }else{
                step--;
            }
        }
        if(p_step+1 == step){
            turnBall=!turnBall;
            turningStep+=120;
        }
        //刷新界面
        Refresh();
        //刷新分数
        setScore();
    }

    private static void checkCondition(){
        //完成
        if(checkWinCondition()){
            for(Box box : boxes) {
                box.image = CharacterImages.getBoxImage(2);
                box.imageID = 2;
                boxImageStage+=8;
            }
        }
        //检测是否完成
        if (step > MAX_STEP&&checkWinCondition()&&check2&&!check) {
            check2 = false;
            checkGameOver = true;
            alert("Game Over","You finished the game!");
            alert("Waring: Too many steps!");
        }
        // 检查是否胜利
        if (checkWinCondition()&&check&&step<=MAX_STEP) {
            check = false;
            checkGameOver = true;
//            alert("Victory","Congratulations! You win!");
        }
        //步数检测
        if(step > MAX_STEP&&check) {
            check = false;
            alert("Failure","You lost!");
            alert("Reason: Too many steps!");
        }
        //失败
        if(checkFailCondition()&&!checkGameOver){
            check = false;
            checkGameOver = true;
//            alert("Failure","You lost!");
//            alert("Reason: You can't get to the position!");
        }
    }

    public static boolean checkWinCondition() {
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

    public static boolean checkFailCondition() {
        if(M!=0){
            return false;
        }
        char[][] t_map = map.getMap();
        for (Box box : boxes) {
            int y = box.getX();
            int x = box.getY();
            if(t_map[x][y]=='@'||t_map[x][y]=='T'){
                continue;
            }
            if(t_map[x-1][y]=='#'){
                if(t_map[x][y-1]=='#'||t_map[x][y+1]=='#'){
                    return true;
                }
            }
            if(t_map[x+1][y]=='#'){
                if(t_map[x][y-1]=='#'||t_map[x][y+1]=='#'){
                    return true;
                }
            }
            if(t_map[x][y-1]=='#'){
                if(t_map[x-1][y]=='#'||t_map[x+1][y]=='#'){
                    return true;
                }
            }
            if(t_map[x][y+1]=='#'){
                if(t_map[x-1][y]=='#'||t_map[x+1][y]=='#'){
                    return true;
                }
            }
        }
        return false;
    }

    private static void setScore(){
        int num1= new Map(map.getMap()).getPosition().length;
        int num2 = 0;
        for(Target target : targets) {
            if(grid[target.getX()][target.getY()].getFill() == BoxColor) {
                num2++;
            }
        }
        score = num2*10000/num1;
    }

    private static void alert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game exited!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private static void alert(String title,String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void alert(AlertType type,String title,String message) {
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
    public static class MyTask extends Task<Void> {
        public String[] args;
        public int time;
        public MyTask(String[] args) {
            // 这里是多线程执行的代码
            this.args = args;
        }
        @Override
        protected Void call() throws Exception {
            if(args[0]!="-b"){
                Thread.sleep(1000);
                if(args[0]!="-d"){
                    Thread.sleep(1000);
                }
            }
            // 这里是多线程执行的代码
            SokobanSolver.parseArguments(args);
            calSuccess = true;
            for (MyTask task : tasks) {
                if (!task.isDone()&&this!=task) {
                    task.cancel();
                }
            }
            if(!checkWinCondition()){
                args[0] = "-b";
                SokobanSolver.parseArguments(args);
                calSuccess = false;
            }
            calSuccess = true;
            calFinish = true;
            return null;
        }
    }

    public static void submitTask() {
        String[] strs = {"-b","-d","-u","-ab","-gb","-am","-gm"};
        for(int i = 0; i < strs.length; i++){
            MyTask task = new MyTask(new String[]{strs[i],"-q"});
            tasks.add(task);
            executorService.submit(task);
        }
    }

}
