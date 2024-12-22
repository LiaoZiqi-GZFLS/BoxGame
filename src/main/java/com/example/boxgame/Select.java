package com.example.boxgame;

import com.leewyatt.rxcontrols.controls.RXToggleButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import static com.example.boxgame.BoxGame.N;
import static com.example.boxgame.BoxGame.M;
import static com.example.boxgame.BoxGame.iceAndFire;
import static com.example.boxgame.Welcome.*;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

public class Select {

    public AnchorPane background;
    public BorderPane page3;
    public BorderPane page2;
    public BorderPane page1;
    public BorderPane page4;
    public RXToggleButton b1;
    public RXToggleButton b2;
    public RXToggleButton b3;
    public RXToggleButton b4;
    public RXToggleButton b5;
    public BorderPane page5;
    public Label namelabel;
    public Label youxiaolabel;
    public Label mapsizelabel;
    public Label modelabel;
    public Label introlabel;
    public Label authorlabel;
    public StackPane startbtn;
    public HBox infobox;
    public Button suijiguanqia;

    public void startplus(MouseEvent Event) throws IOException {
        MusicManager.playSound02(bkgvol);
        MusicManager.stopBGM1();
        from = 2;
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
    }

    public void initialize(){
        if(isfinished==0){
            suijiguanqia.setDisable(true);
            b2.setDisable(true);
            b3.setDisable(true);
            b4.setDisable(true);
            b5.setDisable(true);
        }else{
            suijiguanqia.setDisable(false);
            b2.setDisable(false);
            b3.setDisable(false);
            b4.setDisable(false);
            b5.setDisable(false);
        }
    }

    public void start0(MouseEvent mouseEvent) throws IOException {
        iceAndFire = true;
        N=0;
        M=0;
        currentstep=0;
        startplus(mouseEvent);
    }
    public void start1(MouseEvent Event) throws IOException {
        N = 1;
        M=0;
        currentstep=0;
        current = "1-1";
        startplus(Event);
    }
    public void start2(MouseEvent Event) throws IOException {
        N = 2;
        M=0;
        currentstep=0;
        current = "1-2";
        startplus(Event);
    }
    public void start3(MouseEvent Event) throws IOException {
        N = 3;
        M=0;
        currentstep=0;
        current = "1-3";
        startplus(Event);
    }
    public void start4(MouseEvent Event) throws IOException {
        N = 4;
        M=0;
        currentstep=0;
        current = "1-4";
        startplus(Event);
    }
    public void start5(MouseEvent Event) throws IOException {
        N = 5;
        M=0;
        currentstep=0;
        current = "1-5";
        startplus(Event);
    }
    public void start21(MouseEvent Event) throws IOException {
        N = 1;
        M = 1;
        currentstep=0;
        current = "2-1";
        startplus(Event);
    }
    public void start22(MouseEvent Event) throws IOException {
        N = 2;
        M = 1;
        currentstep=0;
        current = "2-2";
        startplus(Event);
    }
    public void start23(MouseEvent Event) throws IOException {
        N = 3;
        M = 1;
        currentstep=0;
        current = "2-3";
        startplus(Event);
    }
    public void start24(MouseEvent Event) throws IOException {
        N = 4;
        M = 1;
        currentstep=0;
        current = "2-4";
        startplus(Event);
    }
    public void start25(MouseEvent Event) throws IOException {
        N = 5;
        M = 1;
        currentstep=0;
        current = "2-5";
        startplus(Event);
    }

    public void exit(KeyEvent Event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome.fxml")));
        Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void exit1(MouseEvent Event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome.fxml")));
        Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void key(KeyEvent keyEvent) throws IOException {
        switch (keyEvent.getCode()) {
            case ESCAPE:
                exit(keyEvent);
        }
    }

    public void shiftpage(MouseEvent mouseEvent) {
        page1.setVisible(false);
        page2.setVisible(false);
        page3.setVisible(false);
        page4.setVisible(false);
        page5.setVisible(false);
        if(b1.isSelected()){
            page1.setVisible(true);
        }
        if(b2.isSelected()){
            page2.setVisible(true);
        }
        if(b3.isSelected()){
            page3.setVisible(true);
        }
        if(b4.isSelected()){
            page4.setVisible(true);
        }
        if(b5.isSelected()){
            page5.setVisible(true);
        }
    }


    public void start31(MouseEvent mouseEvent) throws IOException {
        N = 1;
        M = 2;
        currentstep=0;
        current = "3-1";
        startplus(mouseEvent);
    }
    public void start32(MouseEvent mouseEvent) throws IOException {
        N = 2;
        M = 2;
        currentstep=0;
        current = "3-2";
        startplus(mouseEvent);
    }public void start33(MouseEvent mouseEvent) throws IOException {
        N = 3;
        M = 2;
        currentstep=0;
        current = "3-3";
        startplus(mouseEvent);
    }public void start34(MouseEvent mouseEvent) throws IOException {
        N = 4;
        M = 2;
        currentstep=0;
        current = "3-4";
        startplus(mouseEvent);
    }public void start35(MouseEvent mouseEvent) throws IOException {
        N = 5;
        M = 2;
        currentstep=0;
        current = "3-5";
        startplus(mouseEvent);
    }
    public void start41(MouseEvent mouseEvent) throws IOException {
        N = 1;
        M = 3;
        currentstep=0;
        current = "4-1";
        startplus(mouseEvent);
    }
    public void start42(MouseEvent mouseEvent) throws IOException {
        N = 2;
        M = 3;
        currentstep=0;
        current = "4-2";
        startplus(mouseEvent);
    }
    public void start43(MouseEvent mouseEvent) throws IOException {
        N = 3;
        M = 3;
        currentstep=0;
        current = "4-3";
        startplus(mouseEvent);
    }
    public void start44(MouseEvent mouseEvent) throws IOException {
        N = 4;
        M = 3;
        currentstep=0;
        current = "4-4";
        startplus(mouseEvent);
    }
    public void start45(MouseEvent mouseEvent) throws IOException {
        N = 5;
        M = 3;
        currentstep=0;
        current = "4-5";
        startplus(mouseEvent);
    }

    String name = "awa";
    int mode = 0;
    char[][] map3 = new char[][]{
            "######..".toCharArray(),
            "#....###".toCharArray(),
            "#...TT.#".toCharArray(),
            "#.BBBP.#".toCharArray(),
            "#..#.T.#".toCharArray(),
            "########".toCharArray(),
    };

    public static int fromstartvip = 0;
    public void startvip(MouseEvent mouseEvent) throws IOException {
        current = name;
        currentmap = map3.clone();
        currentstep=0;
        N = 0;
        M = mode;
        fromcontinuebtn = 1;
        fromstartvip=1;
        startplus(mouseEvent);
    }

    public void daoru(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择地图");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile!=null){
            FileReader reader = null;
            try {
                reader = new FileReader(selectedFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            JSONTokener jt = new JSONTokener(reader);
            JSONObject list = new JSONObject(jt);
            if(list.has("map")&&list.has("mode")&&
                    list.has("name")&&list.has("author")&&
                    list.has("introduction")&&list.getInt("mode")<5){
                infobox.setVisible(true);
                name = list.getString("name");
                String map = list.getString("map");
                String[] map1 = map.split("\n");
                map3 = new char[map1.length-1][];
                for(int i=1;i<map1.length;i++){
                    map3[i-1] = map1[i].toCharArray();
                }
                mode = list.getInt("mode");
                String author = list.getString("author");
                String introduction = list.getString("introduction");
                startbtn.setVisible(true);
                youxiaolabel.setText("有效");
                youxiaolabel.setTextFill(GREEN);
                youxiaolabel.setVisible(true);
                namelabel.setText(name);
                mapsizelabel.setText(map3.length+"x"+map3[0].length);
                modelabel.setText(mode+"");
                authorlabel.setText(author);
                introlabel.setText(introduction);

            }else{
                youxiaolabel.setText("无效");
                youxiaolabel.setTextFill(RED);
                youxiaolabel.setVisible(true);
            }

        }else{
            youxiaolabel.setVisible(false);
            namelabel.setText("导入一个有效的json文件");
        }
    }

    public void start01(MouseEvent mouseEvent) throws IOException {
        N = -1;
        M = 0;
        currentstep=0;
        fromstartvip=1;
        startplus(mouseEvent);
    }
}

