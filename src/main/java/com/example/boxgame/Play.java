package com.example.boxgame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.boxgame.Welcome.from;

public class Play {
    public Pane stoppane;
    public StackPane stopbtn;
    public AnchorPane bkg;
    public Canvas canvas;

    public void initialize(){
        stoppane.setVisible(false);
    }

    public void stop(MouseEvent mouseEvent) {
        stoppane.setVisible(true);
        stopbtn.setVisible(false);
    }
    public void continu(){
        stoppane.setVisible(false);
        stopbtn.setVisible(true);
    }

    public void exit(MouseEvent Event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        if(from==2){
            root = FXMLLoader.load(getClass().getResource("select.fxml"));
        }
        Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void restart(MouseEvent mouseEvent) {
    }

    public void move(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
        switch (keyEvent.getCode()) {
            case UP:
                System.out.println("UP");
                break;
            case ESCAPE:
                if(stopbtn.isVisible()){
                    stoppane.setVisible(true);
                    stopbtn.setVisible(false);
                }else {
                    stoppane.setVisible(false);
                    stopbtn.setVisible(true);
                }
                break;
        }
    }
}
