package com.example.bankinfo.common;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class MP3Player {
    private String filename;
    private Player player;
    public MP3Player(String filename) {
        this.filename = filename;
    }

    /**
     * 播放
     */
    public void play() {
        try {
            BufferedInputStream buffer = new BufferedInputStream(
                    new FileInputStream(filename));
            player = new Player(buffer);
            player.play();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        MP3Player mp3 = new MP3Player("E:\\MY\\missLi\\精华音乐\\最新\\80000.mp3");
        mp3.play();
    }

}
