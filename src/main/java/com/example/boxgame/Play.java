package com.example.boxgame;

import javafx.animation.AnimationTimer;
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
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

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
    public GridPane gridPane0 = new GridPane();
    public GridPane gridPane = new GridPane();
    public GridPane gridPane1 = new GridPane();
    //public Label timeLabel = new Label();
    //public Label scoreLabel = new Label();
    //public Label stepLabel = new Label();

    public void initialize(){
        stoppane.setVisible(false);
        System.out.println("Starting...");
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
        initializeGame();
        // 创建并启动AnimationTimer
        AnimationTimer timer = new AnimationTimer() {
            private static long lastTime = System.nanoTime();
            @Override
            public void handle(long currentNanoTime) {
                // 获取GraphicsContext
                GraphicsContext gc = canvas.getGraphicsContext2D();
                // 将GridPane截图
                WritableImage snapshot = gridPane0.snapshot(new SnapshotParameters(), null);
                WritableImage snapshot2 = gridPane1.snapshot(new SnapshotParameters(), null);
                // 计算GridPane在Canvas上的居中位置
                double gridPaneX = (canvas.getWidth() - gridPane.getPrefWidth()) / 2;
                double gridPaneY = (canvas.getHeight() - gridPane.getPrefHeight()) / 2;
                // 将GridPane图像绘制到Canvas的居中位置
                gc.drawImage(snapshot, gridPaneX- (double) (GRID_SIZE * GRID_COUNT) /2, gridPaneY- (double) (GRID_SIZE * GRID_COUNT) /2);
                //gc.drawImage(snapshot2,0,0);
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

    public void stop(MouseEvent mouseEvent) {
        stoppane.setVisible(true);
        stopBtn.setVisible(false);
    }
    public void continueGame(){
        stoppane.setVisible(false);
        stopBtn.setVisible(true);
    }

    public void exit(MouseEvent Event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        if(from==2){
            root = FXMLLoader.load(getClass().getResource("select.fxml"));
        }
        Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void restart(MouseEvent mouseEvent) {
        initializeGame();
    }

    public void move(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
        switch (keyEvent.getCode()) {
            case ESCAPE:
                if(stopBtn.isVisible()){
                    stoppane.setVisible(true);
                    stopBtn.setVisible(false);
                }else {
                    stoppane.setVisible(false);
                    stopBtn.setVisible(true);
                }
                break;
            default:
                handleKeyPress(keyEvent);
                break;
        }
    }
}
