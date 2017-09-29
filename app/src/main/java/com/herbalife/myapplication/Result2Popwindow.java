package com.herbalife.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * Created by sahara on 2016/6/30.
 */
public class Result2Popwindow extends PopupWindow implements View.OnClickListener{

    private Activity context;

    public Result2Popwindow(Activity context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.pop_result2, null);
        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(this);
        v.findViewById(R.id.iv_close).setOnClickListener(this);

        this.setContentView(v);// 设置View
//        int height = MyApplication.scrnHeight*1/2;
        int width = MyApplication.scrnWidth*4/5;
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);// 设置高度
        this.setWidth(width);// 设置宽度
//        this.setBackgroundDrawable(new BitmapDrawable());//设置之后 就可以点击外部
        // 设置弹出窗口可点击
        this.setFocusable(false);//设置true之后就不会相应外部点击事件,默认是false
        this.setOutsideTouchable(false);
        this.update();// 刷新状态
        this.setAnimationStyle(R.style.pop_anim2);// 设置弹出与消失动画
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_sure://揭秘终极主题
//                Intent intent = new Intent(context, Act_GameOver.class);
//                context.startActivity(intent);
//                context.finish();
//                break;
//            case R.id.iv_close://关闭
//
//                break;
//        }
        dismissPop();
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            MyUtils.backgroundAlpha(context,(float) 0.3);
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);//居中显示
        } else {
            MyUtils.backgroundAlpha(context,(float) 1.0);
            this.dismiss();
        }
    }

    public void dismissPop() {
        if (Result2Popwindow.this != null && Result2Popwindow.this.isShowing()) {
            if (context!=null&&!context.isFinishing()){
                MyUtils.backgroundAlpha(context,(float) 1.0);
                this.dismiss();
            }
        }
    }

}
