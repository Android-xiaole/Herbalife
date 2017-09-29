package com.herbalife.myapplication.fragment;

import android.view.View;

import com.herbalife.myapplication.R;
import com.sitsmice.herbalife_jar.base.BaseFragment;

/**
 * Created by sahara on 2016/11/15.
 */

public class Frag_Login extends BaseFragment{

    public Frag_Login(){
        r = R.layout.frag_login;
    }

    @Override
    protected void onCreateView() {
        initTitle();
        title_text.setText("报名注册");
        auto_icon.setVisibility(View.INVISIBLE);
        auto_menu.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }

    @Override
    public void initUI() {

    }
}
