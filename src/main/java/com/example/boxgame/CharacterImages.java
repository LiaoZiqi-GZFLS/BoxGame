package com.example.boxgame;

import javafx.scene.image.Image;

import java.util.Objects;

public class CharacterImages {
    public CharacterImages() {}
    public static Image[][] playerImages = {
            {new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player1.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player2.png"))),},
            {new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player3.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player4.png"))),},
            {new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player5.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player6.png"))),},
            {new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player7.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/player8.png"))),},
    };
    public static Image[] boxImages = {
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/box1.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/box2.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/box3.png"))),
    };
    public static Image[] wallImages = {
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/wall1.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/wall2.png"))),
    };
    public static Image[] groundImages = {
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/ground1.png"))),
            new Image(Objects.requireNonNull(Play.class.getResourceAsStream("img/ground2.png"))),
    };
    public static Image getPlayerImage(int a, int b) {
        return playerImages[a][b];
    }
    public static Image getBoxImage(int a) {
        return boxImages[a];
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
}
