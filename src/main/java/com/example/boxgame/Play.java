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


import static com.example.boxgame.BoxGame.*;
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

    @FXML
    private StringProperty timeS = new SimpleStringProperty(String.format("%02d:%02d", elapsedTime / 1000 %3600 / 60, (elapsedTime / 1000) % 60));
    @FXML
    private StringProperty stepS = new SimpleStringProperty(String.format("%d", step));
    @FXML
    private StringProperty scoreS = new SimpleStringProperty(String.format("%02d:%02d%%", score/100, score%100));
    @FXML
    public TextField textField;
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


    public GridPane gridPane0 = new GridPane();
    public GridPane gridPane = new GridPane();
    public GridPane gridPane1 = new GridPane();
    public String Label1;
    private boolean continueOrNot = true;
    private int checkTime = 0;

    public void initialize(){
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
        AnimationTimer timer = new AnimationTimer() {
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
                if(checkRedo){
                    lastTime = System.nanoTime();
                    checkRedo = false;
                }
                if(checkWinCondition()){
                    successhintpane.setVisible(true);
                }
                if(checkGameOver&&checkFailCondition()){
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
                elapsedTime = (long) ((currentNanoTime - lastTime) / 1_000_000.0);
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
        stoppane.setVisible(true);
        stopBtn.setVisible(false);
        continueOrNot = !continueOrNot;
        checkTime++;
    }
    @FXML
    public void continueGame(){
        stoppane.setVisible(false);
        stopBtn.setVisible(true);
        continueOrNot = !continueOrNot;
        checkTime++;
    }
    @FXML
    public void exit(MouseEvent Event) throws IOException {
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
        defeathintpane.setVisible(false);
        initializeGame();
        stoppane.setVisible(false);
        stopBtn.setVisible(true);
        continueOrNot = true;
        checkTime = 0;
        checkRedo = true;
    }
    @FXML
    public void move(KeyEvent keyEvent) {
        //System.out.println(keyEvent.getCode());
        if(checkGameOver){
            return;
        }
        switch (keyEvent.getCode()) {
            case ESCAPE:
                if(stopBtn.isVisible()){
                    stoppane.setVisible(true);
                    stopBtn.setVisible(false);
                }else {
                    stoppane.setVisible(false);
                    stopBtn.setVisible(true);
                }
                continueOrNot = !continueOrNot;
                checkTime++;
                break;
            default:
                if(continueOrNot){
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
        init.put("last",last);
        init.put("current",current);
        init.put("isfinished",isfinished);
        init.put("currentmap",currentmap);
        FileOutputStream fos = new FileOutputStream(info);
        fos.write(init.toString().getBytes());
    }

    public void XiaYiGuan(MouseEvent Event) throws IOException {
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
}
