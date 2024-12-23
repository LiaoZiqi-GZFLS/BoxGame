package com.example.mapeditor;

import com.leewyatt.rxcontrols.controls.RXTextField;
import com.leewyatt.rxcontrols.controls.RXToggleButton;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import com.example.mapeditor.Map;

public class Mapeditor {
    public Slider linebar;
    public Label linevolume;
    public Slider columnbar;
    public Label columnvolume;
    public Canvas canvas;
    public RXToggleButton paint1;
    public RXToggleButton paint2;
    public RXToggleButton paint3;
    public RXToggleButton paint4;
    public RXToggleButton paint5;
    static Image ground1 = new Image(Mapeditor.class.getResourceAsStream("/img/ground1.png"));
    public ImageView crview;
    public Label label1;
    public RXTextField introductionlabel;
    public AnchorPane daochupane;
    public RXToggleButton w1;
    public RXToggleButton w2;
    public RXToggleButton w3;
    public RXToggleButton w4;
    public RXTextField zuozhe;
    public RXTextField guanqiaminchen;
    public Label success;
    Image ground2 = new Image(getClass().getResourceAsStream("/img/ground2.png"));
    Image wall1 = new Image(getClass().getResourceAsStream("/img/wall.png"));
    Image box = new Image(getClass().getResourceAsStream("/img/box.png"));
    Image player = new Image(getClass().getResourceAsStream("/img/player.png"));
    Image target = new Image(getClass().getResourceAsStream("/img/target.png"));
    char[][] map = new char[10][10];
    public static int ishold = 0;
    public static int x0 = 10;
    public static int y0 = 10;
    int playernum = 0;
    int boxnum =0;
    int targetnum =0;
    int mode = 0;
    String author = "";
    String mapname = "";
    String intro = "";

    public void initmap(){
        success.setVisible(false);
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[i].length;j++){
                map[i][j] = '.';
            }
        }
    }
    public void initialize(){
        crview.setImage(ground1);
        initmap();
        clearmap();
    }

    public void bindlinevolume(MouseEvent mouseEvent) {
        linevolume.setText(String.valueOf((int)linebar.getValue()));
    }

    public void bindcolumnvolume(MouseEvent mouseEvent) {
        columnvolume.setText(String.valueOf((int)columnbar.getValue()));
    }

    public void changesize(ActionEvent mouseEvent) {
        clearmap();
        }


    public void setmap(MouseEvent mouseEvent) {
        boolean v0 = false;
        boolean v1 = false;
        int getx = (int)mouseEvent.getX()/50;
        int gety = (int)mouseEvent.getY()/50;
        System.out.println(getx+" "+gety);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(ground1,getx*50,gety*50,50,50);
        gc.drawImage(cr,getx*50,gety*50,50,50);
        if(cr0==2&&map[getx][gety]=='.'){
            gc.drawImage(ground1,getx*50,gety*50,50,50);
            gc.drawImage(player,getx*50,gety*50,50,50);
        }
        if(cr0==2&&map[getx][gety]=='T'){
            gc.drawImage(target,getx*50,gety*50,50,50);
            gc.drawImage(player,getx*50,gety*50,50,50);
            v0 = true;
        }
        if(cr0==3&&map[getx][gety]=='.'){
            gc.drawImage(ground1,getx*50,gety*50,50,50);
            gc.drawImage(box,getx*50,gety*50,50,50);
        }
        if(cr0==3&&map[getx][gety]=='T'){
            gc.drawImage(target,getx*50,gety*50,50,50);
            gc.drawImage(box,getx*50,gety*50,50,50);
            v1 = true;
        }
        char c = switch (cr0) {
            case 0 -> '.';
            case 1 -> '#';
            case 2 -> 'P';
            case 3 -> 'B';
            case 4 -> 'T';
            default -> '.';
        };
        switch (c){
            case 'P':
                playernum++;
                break;
            case 'B':
                boxnum++;
                break;
            case '@':
                targetnum++;
                break;
        }
        map[getx][gety] = c;
        if(v0){
            map[getx][gety] = '?';
        }
        if(v1){
            map[getx][gety] = '@';
        }
        System.out.println(Arrays.deepToString(map));
    }

public void clearmap(){
    int x = (int)columnbar.getValue();
    int y = (int)linebar.getValue();
    map = new char[x][y];
    initmap();
    canvas.setWidth(x*50);
    canvas.setHeight(y*50);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    for(int i=0;i<x;i++){
        for(int j=0;j<y;j++){
            gc.setFill(Color.WHITE);
            gc.drawImage(ground1,i*50,j*50,50,50);
        }
    }
    for (int i=0;i<x;i++){
        gc.drawImage(wall1,i*50,0,50,50);
        map[i][0] = '#';
        gc.drawImage(wall1,i*50,y*50-50,50,50);
        map[i][y-1] = '#';
    }
    for (int j=0;j<y;j++){
        gc.drawImage(wall1,0,j*50,50,50);
        map[0][j] = '#';
        map[x-1][j] = '#';
        gc.drawImage(wall1,x*50-50,j*50,50,50);
    }
    canvas.setLayoutX(540- (double) (x * 50) /2);
    canvas.setLayoutY(360- (double) (y * 50) /2);
}

public static Image cr = ground1;
    int cr0 = 0;
    public void p1(ActionEvent actionEvent) {
        if(paint1.isSelected()){
            cr = ground1;
            crview.setImage(ground1);
            cr0=0;
        }else if(paint2.isSelected()){
            cr = wall1;
            crview.setImage(wall1);
            cr0=1;
        }else if(paint3.isSelected()){
            cr = player;
            crview.setImage(player);
            cr0=2;
        }
        else if(paint4.isSelected()){
            cr = box;
            crview.setImage(box);
            cr0=3;
        }else if(paint5.isSelected()){
            cr = target;
            crview.setImage(target);
            cr0=4;
        }

    }


    public void setmap1(MouseEvent mouseEvent) {
        if(ishold==1){
            setmap(mouseEvent);
        }
    }

    public void mouseevent(MouseEvent mouseEvent) {
        switch (mouseEvent.getButton()){
            case SECONDARY:
                if(ishold==1){ishold=0;label1.setText("连续放置已关闭");}
                else if(ishold==0){
                    ishold=1;
                    label1.setText("连续放置已开启");
                }
        }
    }
    public void exportmap(){
        String newmap = (new Map(map)).toString(map[0].length,map.length);
        System.out.println(newmap);
    }

    public void exit(MouseEvent mouseEvent) {
        daochupane.setVisible(false);
    }

    public void showexportpage(ActionEvent actionEvent) {
        daochupane.setVisible(true);
    }

    public void changemode(ActionEvent actionEvent) {
        if (w1.isSelected()){
            mode = 0;
        }else if (w2.isSelected()){
            mode = 1;
        }else if (w3.isSelected()){
            mode = 2;
        }else if (w4.isSelected()){
            mode = 3;
        }
    }

    public char[][] rotate(char[][] map){
        int originalWidth = map.length;
        int originalHeight = map[0].length;
        char[][] rotatedMap = new char[originalHeight][originalWidth];
        for (int i = 0; i < originalWidth; i++) {
            for (int j = 0; j < originalHeight; j++) {
                rotatedMap[j][originalWidth - 1 - i] = map[i][j];
            }
        }
        return rotatedMap;
    }


    public void export(ActionEvent actionEvent) throws IOException {
        success.setVisible(true);
        map = rotate(map);
        map = rotate(map);
        map = rotate(map);
        int width = map.length;
        int height = map[0].length;
        char[][] flippedMap = new char[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                flippedMap[width - 1 - i][j] = map[i][j];
            }
        }

        map = flippedMap;
        author=zuozhe.getText();
        mapname=guanqiaminchen.getText();
        intro=introductionlabel.getText();
        File levelfile = new File(mapname+".json");
        if(levelfile.exists()){
            levelfile.delete();
        }
        levelfile.createNewFile();
        JSONObject j = new JSONObject();
        j.put("name",mapname);
        j.put("introduction",intro);
        j.put("mode",mode);
        j.put("map",(new Map(map)).toString(map.length,map[0].length));
        j.put("author",author);
        FileOutputStream fos = new FileOutputStream(levelfile);
        fos.write(j.toString().getBytes());
    }
}
