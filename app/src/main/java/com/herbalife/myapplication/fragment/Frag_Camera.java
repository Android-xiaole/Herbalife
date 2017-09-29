package com.herbalife.myapplication.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.herbalife.myapplication.R;
import com.herbalife.myapplication.activity.Act_Camera;
import com.herbalife.myapplication.camera.ui.AlbumActivity;
import com.sitsmice.herbalife_jar.base.BaseFragment;

/**
 * Created by sahara on 2016/11/15.
 */

public class Frag_Camera extends BaseFragment{

    private ImageView button1,button2;

    public Frag_Camera(){
        r = R.layout.frag_camera;
    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onCreateView() {
        button1 = (ImageView) mView.findViewById(R.id.button1);
        button2 = (ImageView) mView.findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void initUI() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                toActivity(Act_Camera.class);
                break;
            case R.id.button2:
                toActivity(AlbumActivity.class);
                break;
        }
    }
}
