package com.example.boxgame;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;

import static com.example.boxgame.Welcome.*;

public class Userinfo {
    public ImageView avatar;
    public AnchorPane bkg;
    //以下为实时变动项目
    public Label showUserName;
    public Label showPlayCounts;
    public Label showUserid;
    public Label showRecentRecord1;//最近步数
    public Label showRecentRecord2;//最近关卡
    //以上实时变动
    Timer timer = new Timer(1000, e -> {
        showUserName.setText(name);
        showUserid.setText("#"+String.valueOf(id));
        showRecentRecord1.setText(String.valueOf(last_step));
        showPlayCounts.setText(String.valueOf(times));
        showRecentRecord2.setText(last);
    });

    public void initialize(){
        timer.start();
        showUserid.setText("#"+String.valueOf(id));
        showUserName.setText(name);
    }

    public void close(MouseEvent mouseEvent) {
        bkg.getScene().getWindow().hide();
    }

    public void logout(MouseEvent mouseEvent) {
        name = "Guest";
        int id = 0;
        int times = 0;//游玩次数
        int laststep=0;//上次游玩时步数
        last = "NeverPlayed";
        islogin = 0;
        bkg.getScene().getWindow().hide();
    }
}
