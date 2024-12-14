package com.example.boxgame;

import javafx.animation.PauseTransition;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

public class MusicManager {
    private static MediaPlayer bgmPlayer1;
    private static MediaPlayer bgmPlayer2;
    private static MediaPlayer die;
    private static MediaPlayer success;
    private static MediaPlayer sound01;
    public static MediaPlayer sound02;
    public static MediaPlayer sound03;

    public static void playSound01(double p) {
        URL resourceUrl = MusicManager.class.getResource("/com/example/boxgame/bgm/sound01.mp3");
        Media Sound = new Media(resourceUrl.toString());
        sound01 = new MediaPlayer(Sound);
        sound01.setVolume(p/100);
        sound01.play();
    }
    public static void playSound02(double p) {
        URL resourceUrl = MusicManager.class.getResource("/com/example/boxgame/bgm/sound02.mp3");
        Media Sound = new Media(resourceUrl.toString());
        sound02 = new MediaPlayer(Sound);
        sound02.setVolume(p/100);
        sound02.play();
    }
    public static void playSound03(double p) {
        URL resourceUrl = MusicManager.class.getResource("/com/example/boxgame/bgm/sound03.mp3");
        Media Sound = new Media(resourceUrl.toString());
        sound03 = new MediaPlayer(Sound);
        sound03.setVolume(p/100);
        sound03.play();
    }

    public static void playSuccessSound(double p) {
        URL resourceUrl = MusicManager.class.getResource("/com/example/boxgame/bgm/success.wav");
        Media Sound = new Media(resourceUrl.toString());
        success = new MediaPlayer(Sound);
        success.setVolume(p/100);
        success.play();
    }

    public static void playDieSound(double p) {
        URL resourceUrl = MusicManager.class.getResource("/com/example/boxgame/bgm/die.wav");
        Media dieSound = new Media(resourceUrl.toString());
        die = new MediaPlayer(dieSound);
        die.setVolume(p/100);
        die.play();

    }

    // 播放 1.wav
    public static void playBGM1(double volume) {
        if (bgmPlayer1 != null && bgmPlayer1.getStatus() == MediaPlayer.Status.PLAYING) {
            return; // 如果已经在播放，则不重复播放
        }

        URL resourceUrl = MusicManager.class.getResource("/com/example/boxgame/bgm/1.mp3");
        if (resourceUrl != null) {
            Media bgm = new Media(resourceUrl.toString());
            bgmPlayer1 = new MediaPlayer(bgm);
            bgmPlayer1.setVolume(volume/100);
            bgmPlayer1.setCycleCount(MediaPlayer.INDEFINITE); // 循环播放
            bgmPlayer1.play();
        }
    }

    // 暂停 1.wav
    public static void pauseBGM1() {
        if (bgmPlayer1 != null && bgmPlayer1.getStatus() == MediaPlayer.Status.PLAYING) {
            bgmPlayer1.pause();
        }
    }

    // 停止 1.wav
    public static void stopBGM1() {
        if (bgmPlayer1 != null) {
            bgmPlayer1.stop();
            bgmPlayer1.dispose();
            bgmPlayer1 = null; // 释放资源
        }
    }

    public static void setBGM1volume(double volume) {
        if (bgmPlayer1 != null) {
            bgmPlayer1.setVolume(volume/100);
        }
    }

    // 播放 2.wav
    public static void playBGM2(double volume) {
        if (bgmPlayer2 != null && bgmPlayer2.getStatus() == MediaPlayer.Status.PLAYING) {
            return; // 如果已经在播放，则不重复播放
        }

        URL resourceUrl = MusicManager.class.getResource("/com/example/boxgame/bgm/2.mp3");
        if (resourceUrl != null) {
            Media bgm = new Media(resourceUrl.toString());
            bgmPlayer2 = new MediaPlayer(bgm);
            bgmPlayer2.setVolume(volume/100);
            bgmPlayer2.setCycleCount(MediaPlayer.INDEFINITE); // 循环播放

            // 创建 PauseTransition 对象，设置持续时间为0.5秒
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(event -> bgmPlayer2.play()); // 在暂停结束后播放音乐
            pause.play(); // 开始暂停
        }
    }

    // 暂停 2.wav
    public static void pauseBGM2() {
        if (bgmPlayer2 != null && bgmPlayer2.getStatus() == MediaPlayer.Status.PLAYING) {
            bgmPlayer2.pause();
        }
    }
    public static void continueBGM2() {
        if (bgmPlayer2 != null && bgmPlayer2.getStatus() == MediaPlayer.Status.PAUSED) {
            bgmPlayer2.play();
        }
    }

    // 停止 2.wav
    public static void stopBGM2() {
        if (bgmPlayer2 != null) {
            bgmPlayer2.stop();
            bgmPlayer2.dispose();
            bgmPlayer2 = null; // 释放资源
        }
    }
}
