package com.example.boxgame;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;

import java.io.*;

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
    Timer timer = new Timer(100, e -> {
        if(avatarpath.isEmpty()){
            avatar.setImage(new Image(Welcome.class.getResourceAsStream("img/1.png")));
        }else{
            File imgfile = new File(avatarpath);
            avatar.setImage(new Image(imgfile.toURI().toString()));
        }
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
        if(avatarpath.isEmpty()){
            avatar.setImage(new Image(Welcome.class.getResourceAsStream("img/1.png")));
        }else{
            File imgfile = new File(avatarpath);
            avatar.setImage(new Image(imgfile.toURI().toString()));
        }
    }

    public void close(MouseEvent mouseEvent) {
        bkg.getScene().getWindow().hide();
    }

    public void logout(MouseEvent mouseEvent) {
        avatarpath = "";
        name = "Guest";
        id = 0;
        times = 0;//游玩次数
        last_step=0;//上次游玩时步数
        last = "NeverPlayed";
        isfinished=0;
        islogin = 0;
        bkg.getScene().getWindow().hide();
    }

    public void ChangeAvatar(MouseEvent mouseEvent) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择头像");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("jpg", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        // 检查用户是否选择了文件
        if (selectedFile != null) {
            // 打印文件的绝对路径
            System.out.println("Selected file absolute path: " + selectedFile.getAbsolutePath());
            String path = selectedFile.toURI().toString();
            avatarpath = path.substring(5);
            File info = new File("json\\playerdata",name+".json");
            JSONTokener jt = new JSONTokener(new FileReader(info));
            JSONObject list = new JSONObject(jt);
            list.put("avatarpath",avatarpath);
            FileOutputStream fos = new FileOutputStream(info);
            fos.write(list.toString().getBytes());
            System.out.println(path);
            File imgfile = new File(avatarpath);
            avatar.setImage(new Image(imgfile.toURI().toString()));
        } else {
            avatarpath = "";
            System.out.println("No file selected");
        }
    }
}
