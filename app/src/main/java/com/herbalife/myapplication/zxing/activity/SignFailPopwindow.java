package com.herbalife.myapplication.zxing.activity;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.herbalife.myapplication.R;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;


/**
 * Created by sahara on 2016/7/14.
 */
public class SignFailPopwindow extends PopupWindow implements PopupWindow.OnDismissListener{
    private Activity context;
    //创建一个引用队列
    ReferenceQueue<Activity> rq = new ReferenceQueue<>();
    private TextView tv_title;

    public SignFailPopwindow(Activity context){
//        this.context = context;
        //实现一个软引用，将强引用类型str和是实例化的rq放到软引用实现里面
        WeakReference<Activity> srf = new WeakReference<>(context,rq);
        this.context = srf.get();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.pop_signfail,null);

        tv_title = (TextView) v.findViewById(R.id.tv_title);
        v.findViewById(R.id.lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPop();
            }
        });
        this.setContentView(v);// 设置View
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);// 设置高度
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);// 设置宽度
        // 设置弹出窗口可点击
//        this.setBackgroundDrawable(new BitmapDrawable());//设置之后 就可以点击外部
        this.setBackgroundDrawable(null);
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        this.update();// 刷新状态
        this.setAnimationStyle(R.style.pop_anim2);// 设置弹出与消失动画
    }

    /**
     * 设置显示标题
     */
    public void setTitle(String title){
        if (title==null){
            tv_title.setText("无效的二维码");
            return;
        }
        tv_title.setText(title);
    }

    public void showPopwindow(View parent){
        if (!this.isShowing()){
            backgroundAlpha(context,(float) 0.3);
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        }
    }

    public void dismissPop(){
        if (SignFailPopwindow.this!=null&&SignFailPopwindow.this.isShowing()){
            if (context!=null&&!context.isFinishing()){
                backgroundAlpha(context,(float) 1.0);
                SignFailPopwindow.this.dismiss();
            }
        }
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(context,(float) 1.0);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity context,float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        context.getWindow().setAttributes(lp);
    }
}
