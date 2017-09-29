package com.herbalife.myapplication;

import java.io.Serializable;

/**
 * Created by sahara on 2016/11/18.
 */

public class ImageRes implements Serializable{
    public String code;
    public int score;
    public int res_false;
    public int res_true;
    public String name;
    public String englishName;
    public String description;
    public boolean isLight;

    public ImageRes(String code,int score,int res_false,int res_true,String name,String englishName,String description, boolean isLight) {
        this.code = code;
        this.score = score;
        this.res_false = res_false;
        this.res_true = res_true;
        this.name = name;
        this.englishName = englishName;
        this.description = description;
        this.isLight = isLight;
    }

    public ImageRes( String name, int res_true,boolean isLight) {
        this.res_true = res_true;
        this.name = name;
        this.isLight = isLight;
    }
}
