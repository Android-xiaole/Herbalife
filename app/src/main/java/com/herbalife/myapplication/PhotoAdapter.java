package com.herbalife.myapplication;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sitsmice.herbalife_jar.base.AbsBaseAdapter;

import java.util.List;

/**
 * Created by sahara on 2016/11/21.
 */

public class PhotoAdapter extends AbsBaseAdapter<String,PhotoAdapter.myHolder>{

    public PhotoAdapter(Context ctx, List<String> list) {
        super(ctx, list);
    }

    @Override
    protected myHolder getHolder() {
        return new myHolder(R.layout.item_photo);
    }

    @Override
    protected void getView(myHolder t, String item, int position, ViewGroup parent) {
        int measuredHeight = parent.getMeasuredHeight();
        ViewGroup.LayoutParams lp = t.imageView.getLayoutParams();
        lp.height = (measuredHeight-40)/3;
        t.imageView.setLayoutParams(lp);
        ImageLoader.getInstance().displayImage(item,t.imageView,MyApplication.options);
    }

    class myHolder extends AbsBaseAdapter.Holder{

        private ImageView imageView;

        public myHolder(int r) {
            super(r);
            imageView = (ImageView) mView.findViewById(R.id.imageView);
        }
    }
}
