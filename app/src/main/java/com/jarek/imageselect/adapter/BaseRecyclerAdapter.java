package com.jarek.imageselect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jarek.imageselect.core.AnimateFirstDisplayListener;
import com.jarek.imageselect.listener.OnRecyclerViewClickListener;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 *  　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　 ████━████     ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　 　 ┗━━━┓
 * 　　　　　　　　　┃ 神兽保佑　　 ┣┓
 * 　　　　　　　　　┃ 代码无BUG   ┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 * RecyclerAdapter 适配器基类
 * Created by jarek(王健) on 2016/9/12.
 */
public class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected OnRecyclerViewClickListener mOnClickListener;

    protected Context mContext;
    protected LayoutInflater mInflater;

    protected AnimateFirstDisplayListener displayListener;

    //创建一个引用队列
    ReferenceQueue<Context> rq = new ReferenceQueue<>();
    ReferenceQueue<List> rq2 = new ReferenceQueue<>();
    protected BaseRecyclerAdapter(Context context, List<T> list) {
        //实现一个软引用，将强引用类型str和是实例化的rq放到软引用实现里面
        WeakReference<Context> srf = new WeakReference<>(context,rq);
        this.mContext = srf.get();
        //实现一个软引用，将强引用类型str和是实例化的rq放到软引用实现里面
        WeakReference<List> srf2 = new WeakReference<List>(list,rq2);
        this.list = srf2.get();
//        this.mContext = context;
//        this.list = list;
        this.mInflater = LayoutInflater.from(mContext);
    }

    protected List<T> list;
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public void setOnClickListener(OnRecyclerViewClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

}
