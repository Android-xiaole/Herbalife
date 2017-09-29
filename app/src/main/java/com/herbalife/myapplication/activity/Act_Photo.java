package com.herbalife.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.herbalife.myapplication.PhotoAdapter;
import com.herbalife.myapplication.R;
import com.herbalife.myapplication.touchimageview.PictureBigLookActivity;
import com.herbalife.myapplication.video.Act_PicTOVideo;
import com.sitsmice.herbalife_jar.base.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahara on 2016/11/21.
 */

public class Act_Photo extends BaseActivity{

    private GridView gridView;
    private PhotoAdapter adapter;
    private List<String> list = new ArrayList<>();

    private int position;


    private boolean isPermissionOk=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_photo);
        position = getIntent().getIntExtra("position",0);
        if (position==0){
            list.add("http://imgsrc.baidu.com/forum/pic/item/8be9e6cd7b899e5118311eef42a7d933c9950da4.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/b3e72d2eb9389b50dd9bb9918535e5dde6116e47.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/cb40dfb44aed2e737871afcd8701a18b86d6fa76.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/bf096b63f6246b60ab2126f0ebf81a4c510fa203.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/303bc27b6b79b975962b43f0.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/a6efce1b9d16fdfa5e346d6cb48f8c5495ee7bdc.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/9c16fdfaaf51f3ded9eca89094eef01f3b2979e3.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/48540923dd54564eb466aca8b3de9c82d1584f1b.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/54fbb2fb43166d225ea1070c462309f79152d257.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/c5290c7b02087bf42ef74034f2d3572c10dfcf14.jpg");
            list.add("http://www.sharewithu.com/data/attachment/forum/201211/08/1708220gg6wjd7lcj63jgy.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/fc1f4134970a304eb0a2b448d1c8a786c8175cfb.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/af547cb45b4fe7bea044dfc4.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/9d82d158ccbf6c813d9af47ebc3eb13532fa40f3.jpg");
            list.add("http://imgsrc.baidu.com/forum/pic/item/06b10f2442a7d9332e352a81ad4bd11372f001a1.jpg");
        }else{
            list.add("http://a.hiphotos.baidu.com/zhidao/pic/item/a50f4bfbfbedab6493f41970f436afc378311eff.jpg");
            list.add("http://fdfs.xmcdn.com/group18/M01/CB/1B/wKgJKlfFn2fyPbSEAALzRK4lfK0393.jpg");
            list.add("http://img4.duitang.com/uploads/item/201611/06/20161106221207_tUjKX.thumb.700_0.jpeg");
            list.add("http://img5.duitang.com/uploads/item/201610/01/20161001203443_4mCnU.thumb.700_0.jpeg");
            list.add("http://img4.duitang.com/uploads/item/201610/04/20161004181005_8eR4c.thumb.700_0.jpeg");
            list.add("http://img5.duitang.com/uploads/item/201508/15/20150815162355_LNvQC.jpeg");
            list.add("http://img5.duitang.com/uploads/item/201611/04/20161104131934_jWCZe.jpeg");
            list.add("http://i.k1982.com/design_img/id17/200894155356171778033.jpg");
            list.add("http://img4.duitang.com/uploads/item/201508/18/20150818160054_JzCRX.jpeg");
        }
        adapter = new PhotoAdapter(this,list);
        initUI();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == auto_menu){
            Intent intent = new Intent(this, Act_PicTOVideo.class);
            intent.putExtra("piclist", (Serializable) list);
            startActivity(intent);
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        if (position==0){
            title_text.setText("美景图册");
        }else{
            title_text.setText("火迷图册");
        }
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Act_Photo.this, PictureBigLookActivity.class);
                intent.putExtra("imageUrl",list.get(position));
                intent.putExtra("PicList", (Serializable) list);
                startActivity(intent);
            }
        });
    }
}
