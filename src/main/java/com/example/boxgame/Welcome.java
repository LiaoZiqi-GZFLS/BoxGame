package com.example.boxgame;

import com.leewyatt.rxcontrols.controls.RXTranslationButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Welcome {
    public static int from = 0;
    public static int islogin = 0;

    public Label currentlevel;
    public AnchorPane background;
    public RXTranslationButton start;
    public ImageView touxiang;
    public Label showusername;
    public BorderPane bkg;

    //玩家数据
    public static String name = "UNLOGIN";
    public static int id = 0;
    public static int times = 0;//游玩次数
    public static int laststep=0;//上次游玩时步数
    public static String last = "NeverPlayed";//上次游玩的关卡
    public static String current = "Level 1";

    public void initialize() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), event -> {
                    if(last.equals("NeverPlayed")){
                        start.setText("开始游戏");
                        currentlevel.setText(current);
                    }else{
                        start.setText("继续游戏");
                        currentlevel.setText(current);
                    }
                    showusername.setText(name);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        iniuserinfo();
    }

    //登录和用户界面
    ContextMenu userinfo = new ContextMenu();
    Parent userinforoot = null;
    ContextMenu login = new ContextMenu();
    Parent loginroot = null;
    public void iniuserinfo(){
        try {
            userinforoot = FXMLLoader.load(getClass().getResource("userinfo.fxml"));
            loginroot = FXMLLoader.load(getClass().getResource("login.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        login.getScene().setRoot(loginroot);
        userinfo.getScene().setRoot(userinforoot);
    }

    public void startAction(MouseEvent Event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("play.fxml"));
        Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        from = 1;
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void key(KeyEvent keyEvent) {
    }

    public void select(MouseEvent Event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("select.fxml"));
        Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void login(MouseEvent mouseEvent) {
        Stage stage = (Stage) background.getScene().getWindow();
        Bounds bounds = background.localToScreen(background.getBoundsInLocal());
        if(islogin==0){
            login.show(stage,bounds.getMinX()+290,bounds.getMinY()+185);
        } else if (islogin==1) {
            userinfo.show(stage,bounds.getMinX()+290,bounds.getMinY()+185);
        }

    }

    public void settings(MouseEvent Event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
        Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
