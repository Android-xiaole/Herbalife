package com.herbalife.myapplication.zxing.activity;

import android.os.Bundle;
import android.view.View;

import com.herbalife.myapplication.activity.Act_Home;
import com.herbalife.myapplication.R;
import com.sitsmice.herbalife_jar.base.BaseActivity;

/**
 * Created by sahara on 2016/11/28.
 */

public class Act_ScanOk extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_scanok);
    }

    @Override
    public void onClick(View v) {
        toActivity(Act_Home.class);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
