package com.herbalife.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.herbalife.myapplication.activity.Act_Home;
import com.herbalife.myapplication.fragment.Frag_Game;

import static com.herbalife.myapplication.R.id.btn_sure;

/**
 * Created by sahara on 2016/6/30.
 */
public class ResultPopwindow extends PopupWindow implements View.OnClickListener{

    private Activity context;
    private TextView tv_score,tv_title1,tv_title2;
    private ImageView iv_res,iv_flower;

    public ResultPopwindow(Activity context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.pop_result, null);

        tv_score = (TextView) v.findViewById(R.id.tv_score);
        tv_title1 = (TextView) v.findViewById(R.id.tv_title1);
        tv_title2 = (TextView) v.findViewById(R.id.tv_title2);
        Button btn_sure = (Button) v.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(this);
        v.findViewById(R.id.iv_close).setOnClickListener(this);
        iv_res = (ImageView) v.findViewById(R.id.iv_res);
        iv_flower = (ImageView) v.findViewById(R.id.iv_flower);

        this.setContentView(v);// 设置View
//        int height = MyApplication.scrnHeight*1/2;
        int width = MyApplication.scrnWidth*4/5;
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);// 设置高度
        this.setWidth(width);// 设置宽度

        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        this.setFocusable(false);//设置true之后就不会相应外部点击事件,默认是false
        this.setOutsideTouchable(false);
        this.update();// 刷新状态
        this.setAnimationStyle(R.style.pop_anim2);// 设置弹出与消失动画
    }

    public void startAnim(){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_pop);
        iv_res.startAnimation(animation);
        Animation animation_flower = AnimationUtils.loadAnimation(context, R.anim.anim_pop_flower);
        iv_flower.startAnimation(animation_flower);
    }
    public void setScore(String title){
        tv_score.setText(title);
    }
    public void setTitle(){
        tv_title1.setText("您已全部解锁成功");
        tv_title2.setText("感谢您参与发现之旅！");
        tv_score.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case btn_sure://去试试
                if (context instanceof Act_Home){
                    ((Frag_Game)((Act_Home)context).frags.get(0)).countNum();
                }
                break;
            case R.id.iv_close://关闭

                break;
        }
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
        if (ResultPopwindow.this != null && ResultPopwindow.this.isShowing()) {
            if (context!=null&&!context.isFinishing()){
                MyUtils.backgroundAlpha(context,(float) 1.0);
                this.dismiss();
            }
        }
    }

}
