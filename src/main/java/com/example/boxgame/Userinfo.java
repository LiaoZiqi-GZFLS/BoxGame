package com.example.boxgame;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;

import static com.example.boxgame.Welcome.*;

public class Userinfo {
    public ImageView touxiang;
    public AnchorPane bkg;
    //以下为实时变动项目
    public Label showusername ;
    public Label showplaycounts;
    public Label showuserid;
    public Label showrecentrecord1;//最近步数
    public Label showrecentrecord2;//最近关卡
    //以上实时变动
    Timer timer = new Timer(1000, e -> {
        showusername.setText(name);
        showuserid.setText("#"+String.valueOf(id));
        showrecentrecord1.setText(String.valueOf(laststep));
        showplaycounts.setText(String.valueOf(times));
        showrecentrecord2.setText(last);
    });

    public void initialize(){
        timer.start();
        showuserid.setText("#"+String.valueOf(id));
        showusername.setText(name);
    }

    public void close(MouseEvent mouseEvent) {
        bkg.getScene().getWindow().hide();
    }

    public void logout(MouseEvent mouseEvent) {
        name = "UNLOGIN";
        int id = 0;
        int times = 0;//游玩次数
        int laststep=0;//上次游玩时步数
        last = "NeverPlayed";
        islogin = 0;
        bkg.getScene().getWindow().hide();
    }
}
