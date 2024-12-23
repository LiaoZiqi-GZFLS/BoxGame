package com.example.boxgame;

import javafx.scene.image.Image;

import java.util.Objects;

import static com.example.boxgame.Welcome.boxskin0;
import static com.example.boxgame.Welcome.playerskin0;

public class CharacterImages {
    public CharacterImages() {}
    public static Image[][][] playerImages = {
            {{new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player1.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player9.png"))),},
            {new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player3.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player4.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player10.png"))),},
            {new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player5.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player6.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player11.png"))),},
            {new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player7.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player8.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player12.png"))),},},

            {{new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player1-2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player2-2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player9-2.png"))),},
            {new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player3-2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player4-2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player10-2.png"))),},
            {new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player5-2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player6-2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player11-2.png"))),},
            {new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player7-2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player8-2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player12-2.png"))),},},
    };
    public static Image[][] boxImages = {
            {new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/box1-2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/box2-2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/box3-2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/box4-2.png"))),},
            {new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/box1.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/box2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/box3.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/box4.png"))),},
    };
    public static Image[] targetImages = {
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/target2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/target1.png"))),
    };
    public static Image[] wallImages = {
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/wall1.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/wall2.png"))),
    };
    public static Image[] groundImages = {
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/ground1.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/ground2.png"))),
    };
    public static Image[] shadowImages = {
            //new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/ground1.png"))),
            //new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/ground2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/shadow1.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/shadow2.png"))),
    };
    public static Image getPlayerImage(int a, int b) {
        return playerImages[playerskin0][a][b];
    }
    public static Image getBoxImage(int a) {
        return boxImages[boxskin0][a];
    }
    public static Image getTargetImage(int a) {
        return targetImages[a];
    }
    public static Image getWallImage(int a) {
        return wallImages[a];
    }
    public static Image getGroundImage(int a) {
        return groundImages[a];
    }
    public static Image getGroundImage(int x, int y) {
        if((x%2==0&&y%2==0)||(x%2==1&&y%2==1)) {
            return getGroundImage(0);
        }else {
            return getGroundImage(1);
        }
    }
    public static Image getShadowImage(int a) {
        return shadowImages[a];
    }
}
