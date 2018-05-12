package com.example.administrator.essim.response;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class HitokotoType {
    private static String hitoType;

    public static String getType(String type) {
        switch (type) {
            case "a":
                hitoType = "Anime - 动画";
                break;
            case "b":
                hitoType = "Comic – 漫画";
                break;
            case "c":
                hitoType = "Game – 游戏";
                break;
            case "d":
                hitoType = "Novel – 小说";
                break;
            case "e":
                hitoType = "Myself – 原创";
                break;
            case "f":
                hitoType = "Internet – 来自网络";
                break;
            case "g":
                hitoType = "Other – 其他";
                break;
        }
        return hitoType;
    }
}
