package com.herbalife.myapplication;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sitsmice.herbalife_jar.base.AbsBaseAdapter;

import java.util.List;

/**
 * Created by sahara on 2016/11/21.
 */

public class PhotoListAdapter extends AbsBaseAdapter<PhotoList,PhotoListAdapter.myHolder>{


    public PhotoListAdapter(Context ctx, List<PhotoList> list) {
        super(ctx, list);
    }

    @Override
    protected myHolder getHolder() {
        return new myHolder(R.layout.item_photolist);
    }

    @Override
    protected void getView(myHolder t, PhotoList item, int position, ViewGroup parent) {
        ImageLoader.getInstance().displayImage(item.imageUrl,t.imageView,MyApplication.options);
        t.textView.setText(item.name+"("+item.num+")");
    }

    class myHolder extends AbsBaseAdapter.Holder{
        private ImageView imageView ;
        private TextView textView;

        public myHolder(int r) {
            super(r);
            imageView = (ImageView) mView.findViewById(R.id.imageView);
            textView = (TextView) mView.findViewById(R.id.textView);
        }
    }
}
