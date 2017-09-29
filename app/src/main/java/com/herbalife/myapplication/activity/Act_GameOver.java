package com.herbalife.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.herbalife.myapplication.R;
import com.sitsmice.herbalife_jar.base.BaseActivity;

/**
 * Created by sahara on 2016/12/14.
 */

public class Act_GameOver extends BaseActivity{
    private ImageView iv_back;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gameover);
        initUI();
    }

    @Override
    public void initUI() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.button:
                finish();
                break;
        }
    }
}
