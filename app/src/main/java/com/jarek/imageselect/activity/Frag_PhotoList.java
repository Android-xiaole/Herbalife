package com.jarek.imageselect.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.herbalife.myapplication.R;
import com.herbalife.myapplication.video.Act_VideoList;
import com.jarek.imageselect.adapter.ImageFolderAdapter;
import com.jarek.imageselect.bean.ImageFolderBean;
import com.jarek.imageselect.core.ImageSelectObservable;
import com.jarek.imageselect.listener.OnRecyclerViewClickListener;
import com.jarek.imageselect.utils.ImageUtils;
import com.sitsmice.herbalife_jar.base.BaseFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 本地图片浏览 list列表
 */
public class Frag_PhotoList extends BaseFragment implements Callback, OnRecyclerViewClickListener {

	/** 图片所在文件夹适配器 */
	private ImageFolderAdapter mFloderAdapter;
	/** 图片列表 */
	ArrayList<ImageFolderBean> mImageFolderList;
	/** 消息处理 */
	private Handler mHandler;
	
	private final int MSG_PHOTO = 10;

	private static final int DEFAULT_MAX_PIC_NUM = 9;
	/** 可选择图片总数 */
	public static int sMaxPicNum = DEFAULT_MAX_PIC_NUM;
	
	private final int REQUEST_ADD_OK_CODE = 22;
	
	private RecyclerView mRecyclerView;
	
	/** 是否选择单张图片 */
	private boolean mIsSelectSingleImge = false;


	public Frag_PhotoList(){
		r = R.layout.photo_folder_main;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler(this);
		mImageFolderList = new ArrayList<>();
		sMaxPicNum = getActivity().getIntent().getIntExtra("max_num", DEFAULT_MAX_PIC_NUM);

		mIsSelectSingleImge = getActivity().getIntent().getBooleanExtra("single", false);

		ImageUtils.loadLocalFolderContainsImage(getActivity(), mHandler, MSG_PHOTO);
		ImageSelectObservable.getInstance().addSelectImagesAndClearBefore((ArrayList<ImageFolderBean>) getActivity().getIntent().getSerializableExtra("list"));
		
		mFloderAdapter = new ImageFolderAdapter(getActivity(), mImageFolderList);
	}

	@Override
	protected void onCreateView() {
		initView();
	}

	private void initView () {
		initTitle();
		auto_icon.setVisibility(View.GONE);
		title_menu.setBackgroundResource(R.drawable.img_video);
		mRecyclerView = (RecyclerView) mView.findViewById(R.id.lv_photo_folder);

		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		mRecyclerView.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(layoutManager);

		mRecyclerView.setAdapter(mFloderAdapter);
		mFloderAdapter.setOnClickListener(this);
		title_text.setText("相册列表");
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_PHOTO:
			mImageFolderList.clear();
			mImageFolderList.addAll((Collection<? extends ImageFolderBean>) msg.obj);
			mFloderAdapter.notifyDataSetChanged();
			break;
		}
		return false;
	}
	

	@Override
	public void onItemClick(View view, int position) {
		File file = new File(mImageFolderList.get(position).path);
		ImageSelectActivity.startPhotoSelectGridActivity(getActivity(), file.getParentFile().getAbsolutePath(), mIsSelectSingleImge, sMaxPicNum, REQUEST_ADD_OK_CODE);
	}

	@Override
	public void onItemLongClick(View view, int position) {

	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		ImageSelectObservable.getInstance().clearSelectImgs();
		ImageSelectObservable.getInstance().clearFolderImages();
	}

	@Override
	public void initUI() {

	}

	@Override
	public void onClick(View v) {
		if (v == auto_menu){
			toActivity(Act_VideoList.class);
		}
	}

	@Override
	protected boolean onBackPressed() {
		return false;
	}
}
