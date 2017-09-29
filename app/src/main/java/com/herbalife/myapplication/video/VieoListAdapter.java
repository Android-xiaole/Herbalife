package com.herbalife.myapplication.video;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.herbalife.myapplication.R;
import com.sitsmice.herbalife_jar.base.AbsBaseAdapter;

import java.io.File;
import java.util.List;

/**
 * Created by sahara on 2016/12/8.
 */

public class VieoListAdapter extends AbsBaseAdapter<VideoInfo,VieoListAdapter.myHolder>{

    public VieoListAdapter(Context ctx, List<VideoInfo> list) {
        super(ctx, list);
    }

    @Override
    protected myHolder getHolder() {
        return new myHolder(R.layout.item_videolist);
    }

    @Override
    protected void getView(myHolder t, final VideoInfo item, int position, ViewGroup parent) {
        t.iv_logo.setImageBitmap(item.bitmap);
        t.iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideo(item.filePath);
            }
        });
    }

    /**
     * 调用系统分享视频
     */
    private void shareVideo(String videoPath){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM,
                Uri.fromFile(new File(videoPath)));
        share.setType("*/*");//此处可发送多种文件
        ctx.startActivity(Intent.createChooser(share, "分享到："));
    }

    class myHolder extends AbsBaseAdapter.Holder{
        private ImageView iv_logo,iv_share;

        public myHolder(int r) {
            super(r);
            iv_logo = (ImageView) mView.findViewById(R.id.iv_logo);
            iv_share = (ImageView) mView.findViewById(R.id.iv_share);
        }
    }
}
