package com.herbalife.myapplication.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.herbalife.myapplication.MyApplication;
import com.herbalife.myapplication.R;
import com.herbalife.myapplication.App;
import com.herbalife.myapplication.video.view.DrawPadView;
import com.jarek.imageselect.bean.ImageFolderBean;
import com.lansosdk.box.DrawPad;
import com.lansosdk.box.DrawPadUpdateMode;
import com.lansosdk.box.Pen;
import com.lansosdk.box.onDrawPadCompletedListener;
import com.lansosdk.box.onDrawPadProgressListener;
import com.lansosdk.box.onDrawPadSizeChangedListener;
import com.lansosdk.videoeditor.CopyDefaultVideoAsyncTask;
import com.lansosdk.videoeditor.MediaInfo;
import com.lansosdk.videoeditor.SDKFileUtils;
import com.lansosdk.videoeditor.VideoEditor;
import com.lansosdk.videoeditor.onVideoEditorProgressListener;
import com.sitsmice.herbalife_jar.MLog;
import com.sitsmice.herbalife_jar.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahara on 2016/12/6.
 */

public class Act_PicTOVideo extends BaseActivity{

    private DrawPadView mDrawPadView;
    private TextView tv_notify;
    private FrameLayout lin_play;
    private ArrayList<SlideEffect> slideEffectArray;
    private List<ImageFolderBean> piclist = new ArrayList<>();

    private  VideoEditor mEditor=null;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pictovideo);
        piclist = (List<ImageFolderBean>) getIntent().getSerializableExtra("piclist");
        initUI();
        mEditor = new VideoEditor();
        mEditor.setOnProgessListener(new onVideoEditorProgressListener() {
            @Override
            public void onProgress(VideoEditor v, int percent) {

            }
        });
        mp = MediaPlayer.create(Act_PicTOVideo.this, R.raw.herbalife);
    }

    /**
     * 第二步:创建一个AsyncTask,并在backgroud中执行VideoEditor的方法.(当然您也可以创建一个Thread,在Thread中执行)
     */
    public class SubAsyncTask extends AsyncTask<Object, Object, Boolean> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showProgress("正在合成音乐...");
        }
        @Override
        protected synchronized Boolean doInBackground(Object... params) {
            // TODO Auto-generated method stub
            /**
             * 真正执行的代码,因演示的方法过多, 用每个方法的ID的形式来区分, 您实际使用中, 可直接填入具体方法的代码.
             */
            addMusic(Act_PicTOVideo.this,mEditor,"sdcard/atest.mp4");
            return null;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dismissProgress();
            lin_play.setVisibility(View.VISIBLE);
            tv_notify.setText("视频生成完毕！");
            auto_menu.setClickable(true);
            if (mp!=null){
                mp.reset();
                mp.release();
                mp = null;
            }
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        title_text.setText("视频预览");
        title_menu.setBackgroundResource(R.drawable.img_share);
        tv_notify = (TextView) findViewById(R.id.tv_notify);
        mDrawPadView = (DrawPadView) findViewById(R.id.drawPadView);
        lin_play = (FrameLayout) findViewById(R.id.lin_play);
        lin_play.setOnClickListener(this);
        auto_menu.setClickable(false);
    }

    private String videoPath;
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == lin_play){//播放合成好的视频
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String bpath = "file://" + videoPath;
            intent.setDataAndType(Uri.parse(bpath), "video/*");
            startActivity(intent);
        }
        if (v == auto_menu){//分享当前生成的视频
            shareVideo();
        }
    }

    /**
     * 调用系统分享视频
     */
    private void shareVideo(){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM,
                Uri.fromFile(new File(videoPath)));
        share.setType("*/*");//此处可发送多种文件
        startActivity(Intent.createChooser(share, "分享到："));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.post(new Runnable() {
            @Override
            public void run() {
                start();
            }
        },100);
    }

    private void start(){
        try {
//            mp.prepare();
            mp.start();
        } catch (Exception e) {
            MLog.e("test","音乐播放失败:"+e);
            e.printStackTrace();
        }
        //设置为自动刷新模式, 帧率为25
        mDrawPadView.setUpdateMode(DrawPadUpdateMode.AUTO_FLUSH,25);
        //使能实时录制,并设置录制后视频的宽度和高度, 码率, 帧率,保存路径.
        mDrawPadView.setRealEncodeEnable(App.getApp().getScreenWidth(),App.getApp().getScreenWidth(),1000000,25,"sdcard/atest.mp4");

        //设置DrawPad的宽高, 这里设置为480x480,如果您已经在xml中固定大小,则不需要再次设置,
        //可以直接调用startDrawPad来开始录制.
        mDrawPadView.setDrawPadSize(App.getApp().getScreenWidth(),App.getApp().getScreenWidth(),new onDrawPadSizeChangedListener() {

            @Override
            public void onSizeChanged(int viewWidth, int viewHeight) {
                // TODO Auto-generated method stub
                mDrawPadView.startDrawPad(new DrawPadProgressListener(),new DrawPadCompleted());
                //先 获取第一张Bitmap的Pen, 因为是第一张,放在DrawPad中维护的数组的最下面, 认为是背景图片.
                mDrawPadView.addBitmapPen(BitmapFactory.decodeResource(getResources(),R.drawable.backg));
                slideEffectArray=new ArrayList<>();

//                File file  = new File("sdcard/a/");
//                File[] files = file.listFiles();
                for (int i = 0;i<piclist.size();i++){
                    getFifthPen(piclist.get(i).path,i*3000,i*3000+3000);
                }
            }
        });
    }

    private void getFifthPen(int resId,long startMS,long endMS){
        Pen item=mDrawPadView.addBitmapPen(BitmapFactory.decodeResource(getResources(), resId));
        SlideEffect  slide=new SlideEffect(item, 25, startMS, endMS, true);
        slideEffectArray.add(slide);
    }
    private void getFifthPen(File file, long startMS, long endMS){
        Pen item=mDrawPadView.addBitmapPen(Bitmap.createScaledBitmap(BitmapFactory.decodeFile("sdcard/a/"+file.getName()),480,480,false));
        SlideEffect  slide=new SlideEffect(item, 25, startMS, endMS, true);
        slideEffectArray.add(slide);
    }

    private void getFifthPen(String path, long startMS, long endMS){
        Pen item=mDrawPadView.addBitmapPen(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path),480,480,false));
        SlideEffect  slide=new SlideEffect(item, 25, startMS, endMS, true);
        slideEffectArray.add(slide);
    }

    //DrawPad完成时的回调.
    private class DrawPadCompleted implements onDrawPadCompletedListener
    {

        @Override
        public void onCompleted(DrawPad v) {
            // TODO Auto-generated method stub

            if(isDestorying==false){
                toastStop();
            }
            //视频合成完成之后就开始合成音乐
            new SubAsyncTask().execute();
        }
    }

    //源文件和目标文件不能是同一个
    private int addMusic(Context ctx, VideoEditor editor, String srcVideo){
        int ret=-1;
        MediaInfo info=new MediaInfo(srcVideo,false);
        if(info.prepare())
        {
            String video2=srcVideo;
            String video3=null;
            //如果源视频中有音频,则先删除音频
            if(info.isHaveAudio()){
                video3= SDKFileUtils.createFileInBox(info.fileSuffix);
                editor.executeDeleteAudio(video2, video3);

                video2=video3;
            }
            String audio= CopyDefaultVideoAsyncTask.copyFile(ctx, "herbalife.aac");
            videoPath = MyApplication.mCachePath+System.currentTimeMillis()+"herbalife.mp4";
            ret=editor.executeVideoMergeAudio(video2,audio, videoPath,0,piclist.size()*3+1);
            SDKFileUtils.deleteFile(video3);
        }
        return ret;
    }
        //DrawPad进度回调.
        private class DrawPadProgressListener implements onDrawPadProgressListener
        {

            @Override
            public void onProgress(DrawPad v, long currentTimeUs) {  //单位是微妙
                // TODO Auto-generated method stub
                if(currentTimeUs>=(piclist.size()*3+1)*1000*1000)  //多出一秒,让图片走完.
                {
                    mDrawPadView.stopDrawPad();
                }
                if(slideEffectArray!=null && slideEffectArray.size()>0){
                    for(SlideEffect item: slideEffectArray){
                        item.run(currentTimeUs/1000);
                    }
                }
            }
        }

    private void toastStop()
    {
        Toast.makeText(getApplicationContext(), "录制已停止!!", Toast.LENGTH_SHORT).show();
    }

    boolean isDestorying=false;  //是否正在销毁, 因为销毁会停止DrawPad
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();


        isDestorying=true;
        if(slideEffectArray!=null){
            slideEffectArray.clear();
            slideEffectArray=null;
        }

        if(mDrawPadView!=null){
            mDrawPadView.stopDrawPad();
            mDrawPadView=null;
        }
        if (mp!=null){
            mp.release();
            mp = null;
        }
    }
}
