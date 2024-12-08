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

import java.io.IOException;
import java.util.Objects;


import static com.example.boxgame.Welcome.from;
import static com.example.boxgame.BoxGame.*;

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
    @FXML
    private StringProperty timeS = new SimpleStringProperty(String.format("%02d:%02d:%02d", elapsedTime / 1000 / 3600, elapsedTime / 1000 %3600 / 60, (elapsedTime / 1000) % 60));
    @FXML
    private StringProperty stepS = new SimpleStringProperty(String.format("%02d", step));
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
                if(checkGameOver){
                    from = 1;
                    stoppane.setVisible(true);
                    stopBtn.setVisible(true);
                    if(!Label1.contains("100.00%")&&checkWinCondition()){
                        Label1 = Label1.substring(0, Label1.length()-7) + "100.00%";
                    }
                    if(scoreS.get()!="100.00%"&&checkWinCondition()){
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
                String timeString = String.format("Time: %02d:%02d:%02d", elapsedTime / 1000 / 3600, elapsedTime / 1000 %3600 / 60, (elapsedTime / 1000) % 60);
                String scoreString = String.format("Score: %02d.%02d%%", score/100, score%100);
                String stepString = String.format("Step: %02d", step);
                String timeString2 = String.format("%02d:%02d:%02d", elapsedTime / 1000 / 3600, elapsedTime / 1000 %3600 / 60, (elapsedTime / 1000) % 60);
                String stepString2 = String.format("%02d", step);
                String scoreString2 = String.format("%02d:%02d%%", score/100, score%100);
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
        timeLabel.setText("Time: 00:00:00"); // 初始化时间显示
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
    private void setupHBox(HBox hBox, AnchorPane anchorPane) {
        // 将HBox添加到AnchorPane
        anchorPane.getChildren().add(hBox);

        // 设置HBox在AnchorPane中的位置
        anchorPane.setRightAnchor(hBox, 30.0);
        anchorPane.setTopAnchor(hBox, 20.0);

        // 设置HBox的属性
        hBox.setSpacing(5); // 设置HBox中组件之间的间距
    }
    private void setupStackPane(StackPane stackPane, Pane pane) {
        // 将StackPane添加到Pane
        pane.getChildren().add(stackPane);

        // 设置StackPane在Pane中的位置
        stackPane.setLayoutX(390.0);
        stackPane.setLayoutY(308.0);

        // 设置StackPane的属性
        stackPane.setPrefHeight(150.0);
        stackPane.setPrefWidth(190.0);
    }
    private void setupRegion(Region region, StackPane stackPane) {
        // 将Region添加到StackPane
        stackPane.getChildren().add(region);

        // 设置Region的属性
        region.setPrefHeight(200.0);
        region.setPrefWidth(200.0);

        // 设置StackPane的边距
        Insets insets = new Insets(20.0, 27.0, 20.0, 27.0); // top, right, bottom, left
        stackPane.setMargin(region, insets);
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
}
