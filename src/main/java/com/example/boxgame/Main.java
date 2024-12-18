package com.example.boxgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) throws IOException {
        String rootPath = "json";
        File rootdir = new File(rootPath);
        if (!rootdir.exists()){
            rootdir.mkdir();
        }
        String directoryPath = "json\\playerdata";
        File directory = new File(directoryPath);
        if (!directory.exists()||!directory.isDirectory()){
            directory.mkdir();
        }
        File config = new File("json", "config.json");
        if(!config.exists()){
            config.createNewFile();
        }
        FileInputStream fis0 = new FileInputStream(config);
        String conf = new String(fis0.readAllBytes());
        if(conf.isEmpty()){
            FileOutputStream fos = new FileOutputStream(config);
            String b = "{\"envvol\":100,\"playervol\":100,\"bkgvol\":100,\"showcontrols\":0}";
            fos.write(b.getBytes());
        }
        File userdata = new File("json", "userdata.json");
        if (!userdata.exists()){
            userdata.createNewFile();
        }
        FileInputStream fis = new FileInputStream(userdata);
        String json = new String(fis.readAllBytes());
        if(json.isEmpty()){
            FileOutputStream fos = new FileOutputStream(userdata);
            String b = "{\"awa\":{\"id\":0,\"pwd\":\"e3dd05a04907692729b83b3b6bc44769\"}}";
            fos.write(b.getBytes());
        }
        File awa = new File("json\\playerdata", "awa.json");
        if (!awa.exists()){
            awa.createNewFile();
        }
        FileInputStream fisw = new FileInputStream(awa);
        String jsonw = new String(fisw.readAllBytes());
        if(jsonw.isEmpty()){
            FileOutputStream fosw = new FileOutputStream(awa);
            String b = "{\"currentstep\":0,\"times\":114,\"current\":\"awa\",\"laststep\":514,\"last\":\"Level 114514\",\"currentmap\":\"10 10\\n..........\\n..........\\n..######..\\n..#P...#..\\n..#..BT#..\\n..#.TB.#..\\n..######..\\n..........\\n..........\\n..........\\n\",\"isfinished\":1,\"avatarpath\":\"\",\"M\":0,\"N\":0}";
            fosw.write(b.getBytes());
        }
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("fonts/Minecraft.ttf"), 14);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/1.png"))));
        stage.setResizable(false);
        stage.setTitle("BoxGame");
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
    }
}
