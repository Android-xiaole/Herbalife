package com.herbalife.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitsmice.herbalife_jar.base.AbsBaseAdapter;

import java.util.List;

/**
 * Created by sahara on 2016/12/14.
 */

public class MyBoxAdapter extends AbsBaseAdapter<ImageRes,MyBoxAdapter.myHolder>{

    public MyBoxAdapter(Context ctx, List<ImageRes> list) {
        super(ctx, list);
    }

    @Override
    protected myHolder getHolder() {
        return new myHolder(R.layout.item_mybox);
    }

    @Override
    protected void getView(myHolder t, ImageRes item, int position, ViewGroup parent) {
        t.imageView.setImageResource(item.res_true);
        t.tv_name.setText(item.name);
        if (item.isLight){
            t.iv_new.setVisibility(View.VISIBLE);
        }else{
            t.iv_new.setVisibility(View.GONE);
        }
    }

    class myHolder extends AbsBaseAdapter.Holder{
        private TextView tv_name;
        private ImageView iv_new,imageView;

        public myHolder(int r) {
            super(r);
            tv_name = (TextView) mView.findViewById(R.id.tv_name);
            iv_new = (ImageView) mView.findViewById(R.id.iv_new);
            imageView = (ImageView) mView.findViewById(R.id.imageView);
        }
    }
}
