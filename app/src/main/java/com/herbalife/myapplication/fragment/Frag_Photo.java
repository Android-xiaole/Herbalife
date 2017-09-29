package com.herbalife.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.herbalife.myapplication.PhotoList;
import com.herbalife.myapplication.PhotoListAdapter;
import com.herbalife.myapplication.R;
import com.herbalife.myapplication.activity.Act_Photo;
import com.sitsmice.herbalife_jar.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahara on 2016/11/21.
 */

public class Frag_Photo extends BaseFragment{

    private ListView listView;
    private PhotoListAdapter adapter;
    private List<PhotoList> list = new ArrayList<>();

    public Frag_Photo(){
        r = R.layout.frag_photo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add(new PhotoList("本地相册","15","http://imgsrc.baidu.com/forum/pic/item/8be9e6cd7b899e5118311eef42a7d933c9950da4.jpg"));
//        list.add(new PhotoList("火迷图册","9","http://a.hiphotos.baidu.com/zhidao/pic/item/a50f4bfbfbedab6493f41970f436afc378311eff.jpg"));
        adapter = new PhotoListAdapter(getActivity(),list);
    }

    @Override
    protected void onCreateView() {
        initTitle();
        title_text.setText("电子相册");
        auto_icon.setVisibility(View.INVISIBLE);
        auto_menu.setVisibility(View.INVISIBLE);
        listView = (ListView) mView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),Act_Photo.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
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
