package com.example.boxgame;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


import static com.example.boxgame.BoxGame.*;
import static com.example.boxgame.CharacterImages.*;
import static com.example.boxgame.Select.fromstartvip;
import static com.example.boxgame.Settings.showcontrols;
import static com.example.boxgame.Welcome.*;

public class Play {
    @FXML
    public Pane stoppane;
    @FXML
    public StackPane stopBtn;
    @FXML
    public AnchorPane bkg;
    @FXML
    public Canvas canvas;
    @FXML
    public Label timelabel;
    @FXML
    public Label steplabel;
    @FXML
    public Label scorelabel;
    public StackPane defeathintpane;//失败提示版
    public Label defeathint;//失败提示
    public StackPane successhintpane;//成功提示版
    public Label successhint;//成功提示
    public ImageView finalpane;
    public BorderPane baocun;
    public  TextField textField;
    public AnchorPane controlpane;

    @FXML
    private StringProperty timeS = new SimpleStringProperty(String.format("%02d:%02d", elapsedTime / 1000 %3600 / 60, (elapsedTime / 1000) % 60));
    @FXML
    private StringProperty stepS = new SimpleStringProperty(String.format("%d", step));
    @FXML
    private StringProperty scoreS = new SimpleStringProperty(String.format("%02d:%02d%%", score/100, score%100));
    
    
    @FXML
    public AnchorPane timeAndStepAnchorPane;
    @FXML
    public HBox timeHBox;
    @FXML
    public HBox stepHBox;
    @FXML
    public HBox scoreHBox;
    @FXML
    public Pane stopPane;
    @FXML
    public Region stopPanel;
    @FXML
    public HBox buttonHBox;
    @FXML
    public StackPane exitStackPane;
    @FXML
    public StackPane restartStackPane;
    @FXML
    public StackPane continueStackPane;

    public static Image playerImage = new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player1.png")));
    public static Image boxImage = new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/box1.png")));
    public static Image wallImage = new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/wall1.png")));
    public static Image targetImage = new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/target1.png")));

    public static int playerImageStage;
    public static int boxImageStage;

    public AnimationTimer timer;
    public Timer timer2 = new Timer();
    public Timer timer3 = new Timer();

    public final double[] groundInterval = {0.38,0.38};
    public final double[] playerInterval = {0.50,0.30};
    public final double[] targetInterval = {0.42,0.42};
    public final double[] boxInterval = {0.50,0.50};
    public final double[] wallInterval = {0.38,0.38};
    public final double[] shadowInterval = {0.50,0.40};

    public GridPane gridPane0 = new GridPane();
    public GridPane gridPane = new GridPane();
    public GridPane gridPane1 = new GridPane();
    public String Label1;
    private boolean continueOrNot = true;
    private boolean dies = true;
    private boolean success = true;
    private int checkTime = 0;
    private int turningStep = 0;

    double bkgvol;
    public static double playervol;
    public static double envvol;

    public void initvolume(){
        Reader reader;
        try {
            reader = new FileReader("json\\config.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JSONTokener jt = new JSONTokener(reader);
        JSONObject list = new JSONObject(jt);
        bkgvol = list.getDouble("bkgvol");
        envvol = list.getDouble("envvol");
        playervol = list.getDouble("playervol");
    }

    public void initialize(){
        initvolume();
        MusicManager.playBGM2(bkgvol);
        step = currentstep;
        initElement(N);
        defeathintpane.setVisible(false);
        successhintpane.setVisible(false);
        stoppane.setVisible(false);
        gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(PUDDING_SIZE, PUDDING_SIZE, PUDDING_SIZE, PUDDING_SIZE)); // 为GridPane添加内边距
        // 获取GraphicsContext
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // 将GridPane截图
        WritableImage snapshot = gridPane0.snapshot(new SnapshotParameters(), null);
        // 计算GridPane在Canvas上的居中位置
        double gridPaneX = (canvas.getWidth() - gridPane.getPrefWidth()) / 2;
        double gridPaneY = (canvas.getHeight() - gridPane.getPrefHeight()) / 2;
        // 将GridPane图像绘制到Canvas的居中位置
        gc.drawImage(snapshot, gridPaneX, gridPaneY);
        if(showcontrols ==0){
            controlpane.setVisible(true);
        }else{
            controlpane.setVisible(false);
        }

        //init labels
        setUpLabel();
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
        initializeGame();
        // 创建并启动AnimationTimer
        timer = new AnimationTimer() {
            private static long lastTime = System.nanoTime();
            private static long timer = System.nanoTime();
            @Override
            public void handle(long currentNanoTime) {
                // 获取GraphicsContext
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                // 设置文本属性
                gc.setFill(Color.WHITE); // 设置文本颜色
                gc.setFont(Font.font("Arial", 30)); // 设置字体和大小
                //绘制文本
                //gc.fillText(Label1, 140, 48);
                // 将GridPane截图
                WritableImage snapshot = gridPane0.snapshot(new SnapshotParameters(), null);
                //WritableImage snapshot2 = gridPane1.snapshot(new SnapshotParameters(), null);
                // 计算GridPane在Canvas上的居中位置
                double gridPaneX = (canvas.getWidth() - gridPane.getPrefWidth()) / 2;
                double gridPaneY = (canvas.getHeight() - gridPane.getPrefHeight()) / 2;
                // 将GridPane图像绘制到Canvas的居中位置
                gc.drawImage(snapshot, gridPaneX- (double) (GRID_SIZE * GRID_COUNT) /2, gridPaneY- (double) (GRID_SIZE * GRID_COUNT) /2);
                //gc.drawImage(snapshot2,50,100);

                Map t_map = new Map(_map);
                int[] t_playerPosition = t_map.getPlayerPosition();
                int[][] t_boxesPosition = t_map.getBoxesPosition();
                int[][] t_Position = t_map.getPosition();
                int[][] t_wallPosition = t_map.getWallPosition();
                for (int i = 0; i < GRID_COUNT; i++) {
                    for (int j = 0; j < GRID_COUNT; j++) {
                        gc.drawImage(CharacterImages.getGroundImage(i,j),getPixelPosition(new int[]{i,j},groundInterval[0],groundInterval[1])[0],getPixelPosition(new int[]{i,j},groundInterval[0],groundInterval[1])[1]);
                    }
                }
                for (int[] t_targets : t_Position) {
                    gc.drawImage(targetImage, getPixelPosition(t_targets,targetInterval[0],targetInterval[1])[0],getPixelPosition(t_targets,targetInterval[0],targetInterval[1])[1]);
                }
                for (Target t_target : targets){
                    gc.drawImage(t_target.image,getPixelPosition(new int[]{t_target.getX(),t_target.getY()},targetInterval[0],targetInterval[1])[0],getPixelPosition(new int[]{t_target.getX(),t_target.getY()},targetInterval[0],targetInterval[1])[1]);
                }
                for(int[] t_boxes : t_boxesPosition) {
                    gc.drawImage(boxImage, getPixelPosition(t_boxes,boxInterval[0],boxInterval[1])[0], getPixelPosition(t_boxes,boxInterval[0],boxInterval[1])[1]);
                }
                for (Box t_box : boxes){
                    gc.drawImage(t_box.image, getPixelPosition(new int[]{t_box.getX(),t_box.getY()},boxInterval[0],boxInterval[1])[0], getPixelPosition(new int[]{t_box.getX(),t_box.getY()},boxInterval[0],boxInterval[1])[1]);
                }
                for(int[] t_walls : t_wallPosition) {
                    gc.drawImage(wallImage, getPixelPosition(t_walls,wallInterval[0],wallInterval[1])[0], getPixelPosition(t_walls,wallInterval[0],wallInterval[1])[1]);
                }
                for (Wall t_wall : walls){
                    gc.drawImage(t_wall.image, getPixelPosition(new int[]{t_wall.getX(),t_wall.getY()},wallInterval[0],wallInterval[1])[0], getPixelPosition(new int[]{t_wall.getX(),t_wall.getY()},wallInterval[0],wallInterval[1])[1]);
                }
                gc.drawImage(getShadowImage(0),getPixelPosition(t_playerPosition,shadowInterval[0],shadowInterval[1])[0], getPixelPosition(t_playerPosition,shadowInterval[0],shadowInterval[1])[1]);
                //gc.drawImage(playerImage, getPixelPosition(t_playerPosition,playerInterval[0],playerInterval[1])[0], getPixelPosition(t_playerPosition,playerInterval[0],playerInterval[1])[1]);
                gc.drawImage(player.image, getPixelPosition(t_playerPosition,playerInterval[0],playerInterval[1])[0], getPixelPosition(t_playerPosition,playerInterval[0],playerInterval[1])[1]);
                if(checkAI){
                    drawBalls(gc, (int)getPixelPosition(t_playerPosition,playerInterval[0],playerInterval[1])[0], (int)getPixelPosition(t_playerPosition,playerInterval[0],playerInterval[1])[1],GRID_SIZE/10,2*Math.PI*(turningStep++)%24000/240.00);
                }

                if(checkRedo){
                    lastTime = System.nanoTime();
                    checkRedo = false;
                }
                if(checkWinCondition()){
                    if(fromstartvip==1){
                        from=1;
                        successhint.setText("你已通关该关卡 点此回到主界面");
                    }
                    if(N==5){
                        currentmap = new char[][]{
                                "######..".toCharArray(),
                                "#....###".toCharArray(),
                                "#...TT.#".toCharArray(),
                                "#.BBBP.#".toCharArray(),
                                "#..#.T.#".toCharArray(),
                                "########".toCharArray(),
                        };
                        last_step = step;
                        step = 0;
                        last = current;
                        successhint.setText("你已通关该章节 点此回到菜单");

                        from = 2;
                        if(islogin==1){
                            try {
                                save();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    MusicManager.stopBGM2();
                    if(success){
                        MusicManager.playSuccessSound(envvol);
                        success = false;
                    }
                    successhintpane.setVisible(true);
                    defeathintpane.setVisible(false);
                }
                if(checkGameOver&&checkFailCondition()){
                    MusicManager.pauseBGM2();
                    if(dies){
                        MusicManager.playDieSound(envvol);
                        dies = false;
                    }
                    defeathintpane.setVisible(true);
                }
                if(checkGameOver){
//                    from = 1;
//                    stoppane.setVisible(true);
//                    stopBtn.setVisible(true);
                    if(!Label1.contains("100.00%")&&checkWinCondition()){
                        Label1 = Label1.substring(0, Label1.length()-7) + "100.00%";
                    }
                    if(!Objects.equals(scoreS.get(), "100.00%") &&checkWinCondition()){
                        scoreS.set("100.00%");
                    }

                }
                if(checkGameOver||!continueOrNot){
                    String scoreString = String.format("Score: %02d.%02d%%", score/100, score%100);
                    scoreLabel.setText(scoreString);
                    return;
                }
                if(checkTime==2){
                    lastTime += currentNanoTime - timer;
                    checkTime = 0;
                }
                timer = currentNanoTime;
                elapsedTime = (long) ((currentNanoTime - lastTime) / 1_000_000.0) + currenttime;
                String timeString = String.format("Time: %02d:%02d", elapsedTime / 1000 %3600 / 60, (elapsedTime / 1000) % 60);
                String scoreString = String.format("Score: %02d.%02d%%", score/100, score%100);
                String stepString = String.format("Step: %d", step);
                String timeString2 = String.format("%02d:%02d", elapsedTime / 1000 %3600 / 60, (elapsedTime / 1000) % 60);
                String stepString2 = String.format("%d", step);
                String scoreString2 = String.format("%d%%", score/100);
                timeS.set(timeString2);
                stepS.set(stepString2);
                scoreS.set(scoreString2);
                timeLabel.setText(timeString);
                stepLabel.setText(stepString);
                scoreLabel.setText(scoreString);
                // 绘制文本
                Label1 = timeString +" "+ stepString +" "+ scoreString;
                //gc.fillText(Label1, 140, 48);
            }
        };
        timer.start(); // 启动定时器
        // 创建一个定时任务
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // 这里执行定时任务，例如更新UI
                if(playerImageStage>0){
                    int dx=player.getX()-player.getOldX();
                    int dy=player.getY()-player.getOldY();
                    if(dx==0 && dy==-1){
                        if(player.imageID==0){
                            player.image = getPlayerImage(0,0);
                            player.imageID = 2;
                        }else if(player.imageID==1){
                            player.image = getPlayerImage(0,1);
                            player.imageID = 0;
                        }else {
                            player.image = getPlayerImage(0,2);
                            player.imageID = 1;
                        }
                    }
                    if(dx==0 && dy==1){
                        if(player.imageID==0){
                            player.image = getPlayerImage(1,0);
                            player.imageID = 2;
                        }else if(player.imageID==1){
                            player.image = getPlayerImage(1,1);
                            player.imageID = 0;
                        }else {
                            player.image = getPlayerImage(1,2);
                            player.imageID = 1;
                        }
                    }
                    if(dx==-1 && dy==0){
                        if(player.imageID==0){
                            player.image = getPlayerImage(2,0);
                            player.imageID = 2;
                        }else if(player.imageID==1){
                            player.image = getPlayerImage(2,1);
                            player.imageID = 0;
                        }else {
                            player.image = getPlayerImage(2,2);
                            player.imageID = 1;
                        }
                    }
                    if(dx==1 && dy==0){
                        if(player.imageID==0){
                            player.image = getPlayerImage(3,0);
                            player.imageID = 2;
                        }else if(player.imageID==1){
                            player.image = getPlayerImage(3,1);
                            player.imageID = 0;
                        }else {
                            player.image = getPlayerImage(3,2);
                            player.imageID = 1;
                        }
                    }
                    playerImageStage--;
                }
                if (boxImageStage>0){
                    for(Box box : boxes){
                        box.imageID = (box.imageID+1)%4;
                        box.image = getBoxImage(box.imageID);
                    }
                    boxImageStage--;
                }
            }
        };
        // 创建一个定时器，每???毫秒执行一次任务
        timer2.scheduleAtFixedRate(task, 0, 300);
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                // 这里执行定时任务，例如更新UI
                if(calSuccess){
                    switch (solverPath.charAt(0)){
                        case 'u':
                            playsound(playervol);
                            movePlayer(0, -1, M);
                            Backup();
                            checkUndo = false;
                            break;
                        case 'd':
                            playsound(playervol);
                            movePlayer(0, 1, M);
                            Backup();
                            checkUndo = false;
                            break;
                        case 'l':
                            playsound(playervol);
                            movePlayer(-1, 0, M);
                            Backup();
                            checkUndo = false;
                            break;
                        case 'r':
                            playsound(playervol);
                            movePlayer(1, 0, M);
                            Backup();
                            checkUndo = false;
                            break;
                        default:
                            break;
                    }
                    if(solverPath.length()>1){
                        solverPath = solverPath.substring(3);
                    }else {
                            calSuccess = false;
                            calFinish = true;
                            solverPath = "";
                    }
                }
            }
        };
        // 创建一个定时器，每???毫秒执行一次任务
        timer3.scheduleAtFixedRate(task1, 0, 600);
    }

    private void drawBalls(GraphicsContext gc, int dx, int dy, int ballRadius, double angle) {
        // 绘制蓝色球（中心球）
        gc.setFill(new Color(0, 0, 1, 0.5));
        gc.fillOval(dx - 1.5*ballRadius, dy - 1.5*ballRadius, 2 * ballRadius, 2 * ballRadius);
        for (int i = 1; i <= 20; i++){
            gc.fillOval(dx - 1.5*ballRadius, dy - 1.5*ballRadius, 2 * ballRadius * (1+ (double) i /20), 2 * ballRadius * (1+ (double) i /20));
            gc.setFill(new Color(0, 0, 1, 0.1*(1- (double) i /20)));
        }

        // 计算红色球的位置
        int redX = dx + (int) (Math.cos(angle) * ballRadius * 3);
        int redY = dy + (int) (Math.sin(angle) * ballRadius * 3);

        // 绘制红色球（绕着蓝色球转）
        gc.setFill(Color.RED);
        gc.fillOval(redX - ballRadius, redY - ballRadius, 2 * ballRadius, 2 * ballRadius);

        // 绘制拖尾效果
        gc.setFill(new Color(1, 0, 0, 0.5)); // 半透明红色
        for (int i = 1; i <= 40; i++) {
            double oldAngle = angle - (Math.PI / 20) * i;
            int oldRedX = dx + (int) (Math.cos(oldAngle) * ballRadius * 3);
            int oldRedY = dy + (int) (Math.sin(oldAngle) * ballRadius * 3);
            //gc.fillRect(oldRedX - ballRadius, oldRedY - ballRadius, 2*ballRadius*(1- (double) i /40), 2*ballRadius*(1- (double) i /40)); // 绘制小方块作为拖尾
            gc.fillOval(oldRedX - ballRadius, oldRedY - ballRadius, 2*ballRadius*(1- (double) i /40), 2*ballRadius*(1- (double) i /40)); // 绘制小圆圈作为拖尾
            gc.setFill(new Color(1, 0, 0, 0.5*(1- (double) i /40)));
        }

    }

    public double[] getPixelPosition(int[] tempPosition, double x, double y){
        double dx = (canvas.getWidth()-gridPane.getPrefWidth()-gridPane.getWidth())/2+(tempPosition[0]+x)*(GRID_SIZE+STROKE_SIZE);
        double dy = (canvas.getHeight()-gridPane.getPrefHeight()-gridPane.getHeight())/2+(tempPosition[1]+y)*(GRID_SIZE+STROKE_SIZE);
        return new double[]{dx,dy};
    }

    public void setUpLabel(){
        // 创建时间Label
        timeLabel = new Label();
        timeLabel.setText("Time: 00:00"); // 初始化时间显示
        timelabel.textProperty().bind(timeS);
        // 创建步数Label
        stepLabel = new Label();
        stepLabel.setText("Step: 0"); // 初始化时间显示
        steplabel.textProperty().bind(stepS);
        // 创建分数Label
        scoreLabel = new Label();
        scoreLabel.setText("Score: 00.00%"); // 初始化时间显示
        scorelabel.textProperty().bind(scoreS);
    }

    @FXML
    public void stop(MouseEvent mouseEvent) {
        MusicManager.pauseBGM2();
        MusicManager.playSound03(bkgvol);
        stoppane.setVisible(true);
        stopBtn.setVisible(false);
        continueOrNot = !continueOrNot;
        checkTime++;
    }
    @FXML
    public void continueGame(){
        MusicManager.continueBGM2();
        stoppane.setVisible(false);
        stopBtn.setVisible(true);
        continueOrNot = !continueOrNot;
        checkTime++;
    }
    @FXML
    public void exit(MouseEvent Event) throws IOException {
        timer.stop();
        timer2.cancel();
        timer3.cancel();
        MusicManager.stopBGM2();
        currentmap = _map;
        currentstep = step;
        currenttime = elapsedTime;
        if(islogin==1){
            save();
        }
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome.fxml")));
        if(from==2){
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("select.fxml")));
        }
        Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void restart(MouseEvent mouseEvent) {
        //timer.stop();
        //timer2.cancel();
        MusicManager.playSound02(bkgvol);
        dies = true;
        success = true;
        MusicManager.playBGM2(bkgvol);
        defeathintpane.setVisible(false);
        initializeGame();
        stoppane.setVisible(false);
        stopBtn.setVisible(true);
        continueOrNot = true;
        checkTime = 0;
        checkRedo = true;
        currenttime = 0;
    }
    @FXML
    public void move(KeyEvent keyEvent) throws IOException, InterruptedException {
        //System.out.println(keyEvent.getCode());
        if(checkGameOver){
            return;
        }
        switch (keyEvent.getCode()) {
            case ESCAPE:
                if(stopBtn.isVisible()){
                    MusicManager.pauseBGM2();
                    MusicManager.playSound03(bkgvol);
                    stoppane.setVisible(true);
                    stopBtn.setVisible(false);
                }else {
                    MusicManager.continueBGM2();
                    stoppane.setVisible(false);
                    stopBtn.setVisible(true);
                }
                continueOrNot = !continueOrNot;
                checkTime++;
                break;
            default:
                if(continueOrNot&&calFinish){
                    handleKeyPress(keyEvent);
                }
                break;
        }
    }

    public void save() throws IOException {
        File info = new File("json\\playerdata",name+".json");
        JSONTokener jt = new JSONTokener(new FileReader(info));
        JSONObject init = new JSONObject(jt);
        init.put("times",times);
        init.put("laststep",last_step);
        init.put("currentstep",currentstep);
        init.put("currenttime",currenttime);
        init.put("last",last);
        init.put("current",current);
        init.put("isfinished",isfinished);
        init.put("M",M);
        init.put("N",N);
        init.put("currentmap",(new Map(currentmap)).toString());
        FileOutputStream fos = new FileOutputStream(info);
        fos.write(init.toString().getBytes());
    }

    public void XiaYiGuan(MouseEvent Event) throws IOException {//通关
        timer.stop();
        timer2.cancel();
        timer3.cancel();
        last = current;
        last_step = step;
        currentstep=0;
        if(islogin==1){
            try {
                save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(N==5||fromstartvip==1){
            if(isfinished==0){
                finalpane.setVisible(true);
            }else{
                fromstartvip=0;
                exit(Event);
            }
        }else{
            MusicManager.playBGM2(bkgvol);
            N+=1;
            fromcontinuebtn=0;
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("play.fxml")));
            Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
            //start(stage);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("TimeStyle.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("StepStyle.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("ScoreStyle.css")).toExternalForm());
            // 设置关闭事件处理
            stage.setOnCloseRequest(event -> {
                // 创建一个确认对话框
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "您确定要退出程序吗？");
                alert.setTitle("退出确认");
                alert.setHeaderText(null);
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // 用户选择“是”时，关闭窗口
                        System.exit(0);
                        return;
                    } else {
                        // 用户选择“否”时，不执行任何操作，窗口保持打开状态
                        event.consume();
                    }
                });
            });
            stage.setScene(scene);
            stage.show();
        }
        current = M+1+"-"+N;
    }

    public void unlockisfinished(MouseEvent mouseEvent) throws IOException {
        isfinished = 1;
        from = 1;
        exit(mouseEvent);
    }

    public void save0(MouseEvent mouseEvent) throws IOException {
        MusicManager.stopBGM2();
        exit(mouseEvent);
    }

    public void nosave(MouseEvent mouseEvent) throws IOException {
        MusicManager.stopBGM2();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome.fxml")));
        if(from==2){
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("select.fxml")));
        }
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void exit0(MouseEvent mouseEvent) {
        baocun.setVisible(true);
    }

    public void up(MouseEvent actionEvent) {
        playsound(playervol);
        movePlayer(0, -1, M);
        Backup();
        checkUndo = true;
    }

    public void left(MouseEvent actionEvent) {
        playsound(playervol);
        movePlayer(-1, 0, M);
        Backup();
        checkUndo = true;
    }

    public void right(MouseEvent actionEvent) {
        playsound(playervol);
        movePlayer(1, 0, M);
        Backup();
        checkUndo = true;
    }

    public void down(MouseEvent actionEvent) {
        playsound(playervol);
        movePlayer(0, 1, M);
        Backup();
        checkUndo = true;
    }
}
