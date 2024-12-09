package com.example.boxgame;

import com.leewyatt.rxcontrols.controls.RXPasswordField;
import com.leewyatt.rxcontrols.controls.RXTextField;
import com.leewyatt.rxcontrols.controls.RXToggleButton;
import com.leewyatt.rxcontrols.event.RXActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

import static com.example.boxgame.Welcome.*;
import static com.example.boxgame.Welcome.id;

public class Login {

    public RXTextField usernameField;
    public RXPasswordField passwordField;
    public Label error;
    public Button login;
    public RXToggleButton d2;
    public RXToggleButton d1;
    public AnchorPane bkg;

    public void clearuserfield(RXActionEvent rxActionEvent) {
        usernameField.setText("");
    }

    public void login(MouseEvent mouseEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Reader reader = null;
        try {
            reader = new FileReader("json\\userdata.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // 使用JSONTokener来解析文件中的JSON
        JSONTokener jt = new JSONTokener(reader);
        JSONObject list = new JSONObject(jt);
        boolean findname = false;
        String uname = "";
        for(String name : list.keySet()) {
            if(name.equals(username)) {
                findname = true;
                uname = name;
            }
        }

        if(d1.isSelected()){
            if(findname){
                System.out.println("login");
                System.out.println(username);
                System.out.println(password);
                JSONObject info = list.getJSONObject(uname);
                String pwd = info.getString("pwd");
                int id1= info.getInt("id");

                if(pwd.equals(password)){
                    error.setText("登录成功");
                    error.setVisible(true);
                    error.setStyle("-fx-text-fill: green;");
                    islogin=1;
                    name = uname;
                    id = id1;
            }
                try {
                    LoadPlayerStatistic(name);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                error.setText("登录失败 用户名或密码错误");
                error.setVisible(true);
                error.setStyle("-fx-text-fill: red;");
            }

        }else if(d2.isSelected()){
            System.out.println("register");
            System.out.println(username);
            System.out.println(password);
            if(!findname){
                JSONObject newuser = new JSONObject();
                int wid = list.length();
                newuser.put("id", wid);
                newuser.put("pwd", password);
                list.put(username, newuser);
                islogin=1;
                name = username;
                id = wid;
                error.setText("注册成功 已经自动登录");
                error.setVisible(true);
                error.setStyle("-fx-text-fill: green;");
                FileOutputStream fos;
                try {
                    createplayer(username);
                    fos = new FileOutputStream("json\\userdata.json");
                    fos.write(list.toString().getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }else{
                error.setText("注册失败 已有同名用户");
                error.setVisible(true);
                error.setStyle("-fx-text-fill: red;");
            }

        }
    }

    public void close(MouseEvent mouseEvent) {
        bkg.getScene().getWindow().hide();
    }

    public void createplayer(String name) throws IOException {
        File player = new File("json\\playerdata", name+".json");
        if(player.exists()){
            player.delete();
        }
        player.createNewFile();
        FileOutputStream fos = new FileOutputStream(player);
        JSONObject init = new JSONObject();
        init.put("times",times);
        init.put("laststep",last_step);
        init.put("currentstep",currentstep);
        init.put("last",last);
        init.put("current",current);
        init.put("isfinished",isfinished);
        init.put("currentmap",new Map(currentmap));

        fos.write(init.toString().getBytes());
    }

    public void LoadPlayerStatistic(String name) throws IOException {
        File folder = new File("json\\playerdata");
        File info = new File("json\\playerdata",name+".json");
        JSONTokener jt = new JSONTokener(new FileReader(info));
        JSONObject list = new JSONObject(jt);
        times = list.getInt("times");//游玩次数
        last_step = list.getInt("laststep");//上次游玩的步数
        last = list.getString("last");//上次游玩的关卡名
        currentstep = list.getInt("currentstep");//正在游玩的步数
        current = list.getString("current");//正在游玩的关卡名
        isfinished = list.getInt("isfinished");//是否完成前五关
        String maps = list.getString("currentmap");//加载数据
        String[] maps2 = maps.split("\n");
        char[][] map3 = new char[maps2.length-1][];
        for(int i=1;i<maps2.length;i++){
            map3[i-1] = maps2[i].toCharArray();
        }
        currentmap = map3;
        //加载地图
    }
}
