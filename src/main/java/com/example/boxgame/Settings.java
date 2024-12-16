package com.example.boxgame;

import com.leewyatt.rxcontrols.controls.RXToggleButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import static com.example.boxgame.Welcome.*;
import static com.example.boxgame.Welcome.last;

public class Settings {
    public Slider bkgvolumebar;
    public Label bkgvolume;
    public Label playervolume;
    public Slider playervolumebar;
    public Label environmentvolume;
    public Slider environmentvolumebar;
    public VBox pane1;//音频
    public VBox pane2;//皮肤
    public AnchorPane pane3;//关于
    public RXToggleButton playerskinx;//隐藏皮肤按钮
    public RXToggleButton boxskinx;//隐藏皮肤按钮
    public ImageView playerskin;//玩家皮肤图片
    public ImageView boxskin;//箱子皮肤图片

    public void initialize(){
        loadconfig();
        initskin();
    }

    public void initskin(){
        switch (isfinished){
            case 0:
                playerskinx.setDisable(true);
                boxskinx.setDisable(true);
                break;
            case 1:
                playerskinx.setDisable(false);
                boxskinx.setDisable(false);
                playerskinx.setText("隐藏皮肤");
                boxskinx.setText("隐藏箱子皮肤");
                break;
        }
    }

    public void exit(MouseEvent Event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void bindbkgvolume(MouseEvent mouseEvent) {
        try {
            bkgvolume.textProperty().bind(bkgvolumebar.valueProperty().asString("%.0f%%"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void bindenvironmentvolume(MouseEvent mouseEvent) {
        try {
            environmentvolume.textProperty().bind(environmentvolumebar.valueProperty().asString("%.0f%%"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void bindplayervolume(MouseEvent mouseEvent) {
        try {
            playervolume.textProperty().bind(playervolumebar.valueProperty().asString("%.0f%%"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clear(){
        pane1.setVisible(false);
        pane2.setVisible(false);
        pane3.setVisible(false);
    }
    public void btn1(MouseEvent mouseEvent) {
        clear();
        pane1.setVisible(true);
    }

    public void btn2(MouseEvent mouseEvent) {
        clear();
        pane2.setVisible(true);
    }

    public void btn3(MouseEvent mouseEvent) {
        clear();
        pane3.setVisible(true);
    }

    public void changeplayerskin(MouseEvent mouseEvent) {
        if(playerskinx.isSelected()){
            playerskin0 = 1;
            playerskin.setImage(new Image(getClass().getResourceAsStream("img/2.png")));//玩家皮肤2
        }else{
            playerskin0 = 0;
            playerskin.setImage(new Image(getClass().getResourceAsStream("img/1-1.png")));//玩家皮肤1
        }
    }

    public void changeboxskin(MouseEvent mouseEvent) {
        if(boxskinx.isSelected()){
            boxskin0 = 1;
            boxskin.setImage(new Image(getClass().getResourceAsStream("img/box02.png")));//箱子皮肤2
        }else{
            boxskin0 = 0;
            boxskin.setImage(new Image(getClass().getResourceAsStream("img/box01.png")));//箱子皮肤1
        }
    }

    public void key(KeyEvent Event) throws IOException {
        switch (Event.getCode()) {
            case ESCAPE:
                Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
                Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                break;
        }
    }

    public void loadconfig(){
        Reader reader;
        try {
            reader = new FileReader("json\\config.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JSONTokener jt = new JSONTokener(reader);
        JSONObject list = new JSONObject(jt);
        double bkgvol = list.getDouble("bkgvol");
        bkgvolumebar.setValue(bkgvol);
        double envvol = list.getDouble("envvol");
        environmentvolumebar.setValue(envvol);
        double playervol = list.getDouble("playervol");
        playervolumebar.setValue(playervol);

        bkgvolume.textProperty().bind(bkgvolumebar.valueProperty().asString("%.0f%%"));
        playervolume.textProperty().bind(playervolumebar.valueProperty().asString("%.0f%%"));
        environmentvolume.textProperty().bind(environmentvolumebar.valueProperty().asString("%.0f%%"));
    }

    public void config(MouseEvent mouseEvent) {
        double bkgvol = bkgvolumebar.getValue();
        MusicManager.setBGM1volume(bkgvol);
        double playervol = playervolumebar.getValue();
        double envvol = environmentvolumebar.getValue();
        Reader reader;
        try {
            reader = new FileReader("json\\config.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JSONTokener jt = new JSONTokener(reader);
        JSONObject list = new JSONObject(jt);
        list.put("bkgvol", bkgvol);
        list.put("playervol", playervol);
        list.put("envvol", envvol);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("json\\config.json");
            fos.write(list.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void opengithub1(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/LiaoZiqi-GZFLS"));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void opengithub2(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/liaoshao"));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void opengithub(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/LiaoZiqi-GZFLS/BoxGame"));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
