package com.herbalife.myapplication.camera.model;

import java.io.Serializable;

/**
 * 本地简单使用,实际项目中与贴纸相关的属性可以添加到此类中
 */
public class Addon implements Serializable{
    public int id;//解锁后的ID
    public int id2;//未解锁的ID
    public boolean isLight;//true:解锁 flase:未解锁

    //JSON用到
    public Addon() {

    }

    public Addon(int id, int id2, boolean isLight) {
        this.id = id;
        this.id2 = id2;
        this.isLight = isLight;
    }

    public Addon(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
