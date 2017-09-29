package com.herbalife.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sahara on 2016/11/22.
 */

public class MyView extends View{
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(0,0,100,100);
        canvas.drawArc(rectF,180,360,false,new Paint());
    }

    // 手指最后在View中的坐标。
    private int mLastX;
    private int mLastY;

    // 手指按下时View的相对坐标。
    private int mDownViewX;
    private int mDownViewY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //第一步记录触摸点在X轴Y轴的的坐标。
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //记录view相对于初始位置的滚动坐标
                mDownViewX = getScrollX();
                mDownViewY = getScrollY();

                //更新手指此时的坐标
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算手指此时的坐标和上次的坐标滑动的距离。
                int dy = y - mLastY;
                int dx = x - mLastX;

                // 更新手指此时的坐标。
                mLastX = x;
                mLastY = y;

                // 滑动相对距离。
                scrollBy(-dx, -dy);
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:
                scrollTo(mDownViewX, mDownViewY);
                break;
        }
        return super.onTouchEvent(event);
    }
}
