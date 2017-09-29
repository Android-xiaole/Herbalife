package com.herbalife.myapplication.touchimageview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.herbalife.myapplication.MyApplication;
import com.herbalife.myapplication.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;



public class PictureBigLookActivity extends Activity {
	
	private TextView textView1,textView2;
	private String content;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_big_look);
		
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
//		textView3 = (TextView) findViewById(R.id.textView3);
	    ExtendedViewPager mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
	    final ArrayList<String> images = getIntent().getStringArrayListExtra("PicList");
	    content = getIntent().getStringExtra("Content");
//	    textView3.setText(content);
	    if (images!=null && images.size()>=1) {
	    	mViewPager.setAdapter(new TouchImageAdapter(images));
//	    	String selcedPic=getIntent().getStringExtra("selcedPic");
//	    	if (selcedPic!=null) {
//				mViewPager.setCurrentItem(images.indexOf(selcedPic));
//			}
	    }

	    textView2.setText("/"+images.size());
	    mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				textView1.setText(position % images.size()+1+"");
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	    
	    int n = Integer.MAX_VALUE / 2 % images.size();  
//        int itemPosition = Integer.MAX_VALUE / 2 - n;
//        mViewPager.setCurrentItem(itemPosition);
		mViewPager.setCurrentItem(images.indexOf(getIntent().getStringExtra("imageUrl")));
	    
//	    View findViewWithTag = mViewPager.findViewWithTag("");//获得viewpager中指定的view
	}
	 private class TouchImageAdapter extends PagerAdapter {
	        private ArrayList<String> images;

	        public TouchImageAdapter(ArrayList<String> images) {
				super();
				this.images = images;
			}

			@Override
	        public int getCount() {
//	        	return images==null?0:images.size();
				return Integer.MAX_VALUE;
	        }

	        @Override
	        public View instantiateItem(ViewGroup container, int position) {
//	        	TouchImageView img = new TouchImageView(container.getContext());
	        	View v = View.inflate(container.getContext(), R.layout.item_photoview, null);
	        	container.addView(v);
	        	TouchImageView img = (TouchImageView) v.findViewById(R.id.imageView);
	        	final ProgressBar pb = (ProgressBar) v.findViewById(R.id.pb);
	        	pb.setVisibility(View.VISIBLE);
//	        	container.addView(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

	        	ImageLoader.getInstance().displayImage(images.get(position % images.size()), img, MyApplication.options,new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						pb.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						pb.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						pb.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						pb.setVisibility(View.GONE);
					}
				},new ImageLoadingProgressListener() {
					
					@Override
					public void onProgressUpdate(String arg0, View arg1, int arg2, int arg3) {
						//用来处理进度条显示
					}
				});

	            img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						PictureBigLookActivity.this.finish();
					}
				});
	            return v;
	        }

	        @Override
	        public void destroyItem(ViewGroup container, int position, Object object) {
	            container.removeView((View) object);
	        }

	        @Override
	        public boolean isViewFromObject(View view, Object object) {
	            return view == object;
	        }

	    }
}
