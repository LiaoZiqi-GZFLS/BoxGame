package com.example.boxgame;

import com.leewyatt.rxcontrols.controls.RXPasswordField;
import com.leewyatt.rxcontrols.controls.RXTextField;
import com.leewyatt.rxcontrols.controls.RXToggleButton;
import com.leewyatt.rxcontrols.event.RXActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
                int wid = list.length()+1;
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
}
