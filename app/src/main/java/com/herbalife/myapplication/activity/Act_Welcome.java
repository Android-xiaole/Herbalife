package com.herbalife.myapplication.activity;

import android.os.Bundle;

import com.herbalife.myapplication.MyApplication;
import com.herbalife.myapplication.R;
import com.sitsmice.herbalife_jar.base.BaseActivity;

/**
 * Created by sahara on 2016/12/13.
 */

public class Act_Welcome extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_welcome);
        MyApplication.post(new Runnable() {
            @Override
            public void run() {
                toActivity(Act_Home.class);
                finish();
            }
        },2000);
    }
}
