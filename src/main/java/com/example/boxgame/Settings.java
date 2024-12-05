package com.example.boxgame;

import com.leewyatt.rxcontrols.controls.RXToggleButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

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
}
