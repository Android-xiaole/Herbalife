package com.herbalife.myapplication.video;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.herbalife.myapplication.MyApplication;
import com.herbalife.myapplication.R;
import com.sitsmice.herbalife_jar.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahara on 2016/12/8.
 */

public class Act_VideoList extends BaseActivity{
    private ListView listView;
    private VieoListAdapter adapter;
    private List<VideoInfo> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_videolist);
        adapter = new VieoListAdapter(this,list);
        initUI();
        //检索出本地缓存的视频列表
        File file = new File(MyApplication.mCachePath);
        File[] files = file.listFiles();
        for (File file1:files) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.filePath = MyApplication.mCachePath+file1.getName();
            videoInfo.bitmap = getVideoThumbnail(videoInfo.filePath);
            list.add(videoInfo);
        }
        adapter.notifyDataSetChanged();
    }

    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    public void initUI() {
        super.initUI();
        title_text.setText("视频列表");
        auto_menu.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String bpath = "file://" + list.get(position).filePath;
                intent.setDataAndType(Uri.parse(bpath), "video/*");
                startActivity(intent);
            }
        });
    }
}
