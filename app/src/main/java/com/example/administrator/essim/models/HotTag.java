package com.example.administrator.essim.models;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class HotTag
{
    public int r;
    public int g;
    public int b;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String name;
    public String count;
}
