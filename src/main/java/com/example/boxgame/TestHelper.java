package com.example.boxgame;

import static com.example.boxgame.Helper.cal;

public class TestHelper {
    public static void main(String[] args) {
        Map map = new Map(0);
        System.out.println(map);
        System.out.println(cal(map.toString()));
    }
}
