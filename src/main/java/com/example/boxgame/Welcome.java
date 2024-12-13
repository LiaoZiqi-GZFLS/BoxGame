package com.example.boxgame;

import com.leewyatt.rxcontrols.controls.RXTranslationButton;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

import static com.example.boxgame.BoxGame.*;
import static java.lang.Thread.sleep;

public class Welcome {

    public static int from = 0;
    public static int islogin = 0;
    public static int fromcontinuebtn = 0;
    public static String avatarpath = "";

    public Label currentlevel;
    public AnchorPane background;
    public RXTranslationButton start;
    public ImageView touxiang;
    public Label showusername;
    public BorderPane bkg;


    //玩家数据
    public static String name = "Guest";
    public static int isfinished = 0;
    public static int id = 0;
    public static int times = 0;//游玩次数
    public static int last_step =0;//上次游玩时步数
    public static String last = "NeverPlayed";//上次游玩的关卡
    public static int currentstep = 0;//现在游玩关卡的步数
    public static String current = "1-1";
    public static boolean played = false;
    public static char[][] currentmap = {
            "######".toCharArray(),
            "#P...#".toCharArray(),
            "#..BT#".toCharArray(),
            "#.TB.#".toCharArray(),
            "######".toCharArray(),
    };

    double bkgvol;
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
    }

    public void initialize() {
        initvolume();
        MusicManager.playBGM1(bkgvol);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), event -> {
                    if(avatarpath.isEmpty()){
                        touxiang.setImage(new Image(Welcome.class.getResourceAsStream("img/1.png")));
                    }else{
                        File imgfile = new File(avatarpath);
                        touxiang.setImage(new Image(imgfile.toURI().toString()));
                    }
                    currentlevel.setText(current);
                    if(!played&&islogin==0){
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
        initUserInfo();
    }
    public void stopbgm() {
        MusicManager.stopBGM1();
    }

    //登录和用户界面
    ContextMenu userinfo = new ContextMenu();
    Parent userInfoRoot = null;
    ContextMenu login = new ContextMenu();
    Parent loginRoot = null;
    public void initUserInfo(){
        try {
            userInfoRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("userinfo.fxml")));
            loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        login.getScene().setRoot(loginRoot);
        userinfo.getScene().setRoot(userInfoRoot);
    }

    public void startAction(MouseEvent Event) throws IOException {
        stopbgm();
        fromcontinuebtn = 1;
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
        from = 1;
    }

    public void exit(MouseEvent mouseEvent) {
        stopbgm();
        System.exit(0);
    }

    public void key(KeyEvent keyEvent) {
    }

    public void select(MouseEvent Event) throws IOException {
        fromcontinuebtn = 0;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("select.fxml")));
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("settings.fxml")));
        Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
