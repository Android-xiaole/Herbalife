package com.herbalife.myapplication;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitsmice.herbalife_jar.base.AbsBaseAdapter;

import java.util.List;

/**
 * Created by sahara on 2016/11/16.
 */

public class GameAdapter extends AbsBaseAdapter<ImageRes,GameAdapter.myHolder>{

    public GameAdapter(Context ctx, List<ImageRes> list){
        super(ctx, list);
    }

    @Override
    protected myHolder getHolder() {
        return new myHolder(R.layout.item_game);
    }

    @Override
    protected void getView(final myHolder t, ImageRes item,final int position,ViewGroup parent) {
        if (item.isLight){
            t.imageView.setImageResource(item.res_true);
        }else{
            t.imageView.setImageResource(item.res_false);
        }
        t.tv_name.setText(item.name);
    }

    class myHolder extends AbsBaseAdapter.Holder{
        private ImageView imageView;
        private TextView tv_name;

        public myHolder(int r) {
            super(r);
            imageView = (ImageView) mView.findViewById(R.id.imageView);
            tv_name = (TextView) mView.findViewById(R.id.tv_name);
        }
    }

    /**
     * 更新一条指定的listview
     */
    public void updateView(GridView listView, int position){
//        int firstVisiblePosition = listView.getFirstVisiblePosition();
//        int lastVisiblePosition = listView.getLastVisiblePosition();
//        if (position>=firstVisiblePosition&&position<=lastVisiblePosition){//只有当前item可见的时候才展示动画效果里每个item都可见所以就不用考虑这些了}
        View childAt = listView.getChildAt(position);
        if (childAt==null){
            return;
        }
        final myHolder t = (myHolder) childAt.getTag();
//        if (position==0){//显示渐变动画
//            Animation animation_z = AnimationUtils.loadAnimation(ctx, R.anim.anim_1z);
//            t.imageView_z.startAnimation(animation_z);
//            Animation animation_f = AnimationUtils.loadAnimation(ctx, R.anim.anim_1f);
//            t.imageView_f.startAnimation(animation_f);
//        }
//        if (position==1){//显示平移动画
//            Animation animation_z = AnimationUtils.loadAnimation(ctx, R.anim.anim_2z);
//            t.imageView_z.startAnimation(animation_z);
//            Animation animation_f = AnimationUtils.loadAnimation(ctx, R.anim.anim_2f);
//            t.imageView_f.startAnimation(animation_f);
//        }
//        if (position==2){//显示缩放动画
//            Animation animation_z = AnimationUtils.loadAnimation(ctx, R.anim.anim_3z);
//            t.imageView_z.startAnimation(animation_z);
//            Animation animation_f = AnimationUtils.loadAnimation(ctx, R.anim.anim_3f);
//            t.imageView_f.startAnimation(animation_f);
//        }
//        if (position==3){//显示平移动画
//            Animation animation_z = AnimationUtils.loadAnimation(ctx, R.anim.anim_4z);
//            t.imageView_z.startAnimation(animation_z);
//            Animation animation_f = AnimationUtils.loadAnimation(ctx, R.anim.anim_4f);
//            t.imageView_f.startAnimation(animation_f);
//        }
//        if (position==4){//显示翻牌动画
//            setCameraDistance(t);
//            setAnimators(t);
//            mRightOutSet.setTarget(t.imageView_z);
//            mLeftInSet.setTarget(t.imageView_f);
//            mRightOutSet.start();
//            mLeftInSet.start();
//        }
//        if (position == 5){
//            Animation animation_z = AnimationUtils.loadAnimation(ctx, R.anim.anim_6z);
//            t.imageView_z.startAnimation(animation_z);
//            Animation animation_f = AnimationUtils.loadAnimation(ctx, R.anim.anim_6f);
//            t.imageView_f.startAnimation(animation_f);
//        }
//        if (position == 6){
//            Animation animation_z = AnimationUtils.loadAnimation(ctx, R.anim.anim_7z);
//            t.imageView_z.startAnimation(animation_z);
//            Animation animation_f = AnimationUtils.loadAnimation(ctx, R.anim.anim_7f);
//            t.imageView_f.startAnimation(animation_f);
//            MyApplication.mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    t.imageView_z.setVisibility(View.GONE);
//                }
//            },2000);
//        }
//        if (position == 7){
//            Animation animation_z = AnimationUtils.loadAnimation(ctx, R.anim.anim_8z);
//            t.imageView_z.startAnimation(animation_z);
//            Animation animation_f = AnimationUtils.loadAnimation(ctx, R.anim.anim_8f);
//            t.imageView_f.startAnimation(animation_f);
//        }
//        if (position == 8){
//            Animation animation_z = AnimationUtils.loadAnimation(ctx, R.anim.anim_9z);
//            t.imageView_z.startAnimation(animation_z);
//            Animation animation_f = AnimationUtils.loadAnimation(ctx, R.anim.anim_9f);
//            t.imageView_f.startAnimation(animation_f);
//            MyApplication.mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    t.imageView_z.setVisibility(View.GONE);
//                }
//            },2000);
//        }
    }

    private AnimatorSet mRightOutSet; // 右出动画
    private AnimatorSet mLeftInSet; // 左入动画

//    // 设置动画
//    private void setAnimators(final myHolder t) {
//        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(ctx, R.animator.anim_out);
//        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(ctx, R.animator.anim_in);
//
//        // 设置点击事件
//        mRightOutSet.addListener(new AnimatorListenerAdapter() {
//            @Override public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//                t.frameLayout.setClickable(false);
//            }
//        });
//        mLeftInSet.addListener(new AnimatorListenerAdapter() {
//            @Override public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                t.frameLayout.setClickable(true);
//            }
//        });
//    }
//
//    // 改变视角距离, 贴近屏幕
//    private void setCameraDistance(myHolder t) {
//        int distance = 16000;
//        float scale = ctx.getResources().getDisplayMetrics().density * distance;
//        t.imageView_f.setCameraDistance(scale);
//        t.imageView_z.setCameraDistance(scale);
//    }

    public Bitmap gray(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap
                .createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }
}
