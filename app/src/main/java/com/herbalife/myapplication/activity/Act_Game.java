package com.herbalife.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.herbalife.myapplication.GameAdapter;
import com.herbalife.myapplication.ImageRes;
import com.herbalife.myapplication.MyApplication;
import com.herbalife.myapplication.R;
import com.sitsmice.herbalife_jar.base.BaseActivity;
import com.sitsmice.herbalife_jar.utils.UtilObjectIO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahara on 2016/12/13.
 */

public class Act_Game extends BaseActivity{
    private GridView gridView;
    private Button button;

    private GameAdapter adapter;
    private List<ImageRes> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_game);
        List<ImageRes> list = UtilObjectIO.readObject(ArrayList.class, MyApplication.mDownLoadPath);
        if (list!=null){
            this.list = list;
        }else{
//            this.list.add(new ImageRes(R.drawable.img_game_map1_false,R.drawable.img_game_map1_true,"餐厅","Restaurant","我们都是梦想家，同时我们也都是美食家，让我们来品味特别的异域餐厅美食吧。",false));
//            this.list.add(new ImageRes(R.drawable.img_game_map2_false,R.drawable.img_game_map2_true,"长尾船","Long Tail","在泰国当地，长尾船被称为最方便的交通工具。船尾尖尖，缀以大串鲜花乘风破浪在前，驾驶员的船头反在后面。",false));
//            this.list.add(new ImageRes(R.drawable.img_game_map3_false,R.drawable.img_game_map3_true,"King Power免税","King Power Duty-Free Store","在这里除了世界各国的大牌之外。当地特色产品：药膏精油、水果食品、在这里除了世界各国的大牌以外，当地特色产品：药膏精油、水果食品、曼谷包也是我们馈赠亲朋好友的优选礼品。",false));
//            this.list.add(new ImageRes(R.drawable.img_game_map4_false,R.drawable.img_game_map4_true,"普吉山顶","Popularize The Peak","登上观景台，居高临下可以俯瞰整个普吉和蔚蓝的大海。",false));
//            this.list.add(new ImageRes(R.drawable.img_game_map5_false,R.drawable.img_game_map5_true,"神仙半岛","Fairy Peninsula","突出于普吉岛最南端，泰语中德意思为上帝的岬角，景观台上供奉了四面佛，此处是傍晚看日落的最佳地方。",false));
//            this.list.add(new ImageRes(R.drawable.img_game_map6_false,R.drawable.img_game_map6_true,"晚宴","Dinner","这次我们为大家准备的晚宴可是大名鼎鼎的Blue Elephant，菜品做的可是泰国皇室料理。除了美食，还为大家准备了一场视听盛宴，请大家带着愉悦的心情尽情的享受。",false));
//            this.list.add(new ImageRes(R.drawable.img_game_map7_false,R.drawable.img_game_map7_true,"Prime奥特莱斯","Prime Outlets","泰国最大的Outlet，超过300个本地和国际品牌进驻。每日都有指定的货品两折出售，不过数量不多，看看各位能否成功淘宝。",false));
//            this.list.add(new ImageRes(R.drawable.img_game_map8_false,R.drawable.img_game_map8_true,"攀牙湾","Phang-Nga","泰国的小桂林，遍布着诸多大小岛屿，怪石嶙峋，景色千变万化。电影007系列还将此地作为取景地。",false));
//            this.list.add(new ImageRes(R.drawable.img_game_map9_false,R.drawable.img_game_map9_true,"沙发里乐园","Safari Canteen","乐园展示了泰国当地农民的生活，在这里我们能和大象、猴子亲密接触。",false));
            UtilObjectIO.writeObject(this.list,MyApplication.mDownLoadPath);
        }
        initUI();
    }

    @Override
    public void initUI() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new GameAdapter(Act_Game.this,list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Act_Game.this,Act_GameMap.class);
                intent.putExtra("ImageRes",list.get(position));
                intent.putExtra("position",position);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //查找本地文件，看是否全部解锁
        List<ImageRes> list = UtilObjectIO.readObject(ArrayList.class,MyApplication.mDownLoadPath);
        if (list!=null){
            this.list.clear();
            this.list.addAll(list);
            adapter.notifyDataSetChanged();
            int n = 0;
            for (ImageRes imageRes:list) {
                if (imageRes.isLight){
                    n++;
                }
            }
            if (n>=9){
                button.setVisibility(View.VISIBLE);
            }else{
                button.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){//刷新当前界面的解锁情况
            list.clear();
            List<ImageRes> list = UtilObjectIO.readObject(ArrayList.class,MyApplication.mDownLoadPath);
            this.list.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back){
            finish();
        }
        if (v == button){//揭秘终极主题
            toActivity(Act_GameOver.class);
        }
    }
}
