package com.herbalife.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.herbalife.myapplication.ImageRes;
import com.herbalife.myapplication.MyApplication;
import com.herbalife.myapplication.MyBoxAdapter;
import com.herbalife.myapplication.R;
import com.sitsmice.herbalife_jar.base.BaseActivity;
import com.sitsmice.herbalife_jar.utils.UtilObjectIO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahara on 2016/12/14.
 */

public class Act_MyBox extends BaseActivity {
    private GridView gridView;

    private List<ImageRes> list = new ArrayList<>();
    private MyBoxAdapter adapter;
    private String[] s = {"mybox"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mybox);
        adapter = new MyBoxAdapter(this, list);
        initUI();
        List<ImageRes> list = UtilObjectIO.readObject(ArrayList.class, MyApplication.mDownLoadPath, s);
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; i--) {
                this.list.add(list.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initUI() {
        super.initUI();
        auto_menu.setVisibility(View.GONE);
        title_icon.setBackgroundResource(R.drawable.img_game_back);
        title_text.setText("我的宝箱");
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        List<ImageRes> list = UtilObjectIO.readObject(ArrayList.class, MyApplication.mDownLoadPath, s);
        for (ImageRes imageRes : list) {
            imageRes.isLight = false;
        }
        UtilObjectIO.writeObject(list, MyApplication.mDownLoadPath, s);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        List<ImageRes> list = UtilObjectIO.readObject(ArrayList.class, MyApplication.mDownLoadPath,s);
//        for (ImageRes imageRes:list) {
//            imageRes.isLight = false;
//        }
//        UtilObjectIO.writeObject(list,MyApplication.mDownLoadPath,s);
    }
}
