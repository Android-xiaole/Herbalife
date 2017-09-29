package com.sitsmice.herbalife_jar.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.sitsmice.herbalife_jar.JarApplication;
import com.sitsmice.idevevt_jar.R;

import java.util.ArrayList;
import java.util.List;

public class BaseUI implements IBaseUI {

	protected Context mContext;
	protected List<Runnable> runnables = new ArrayList<>();

	public BaseUI(Context ctx) {
		mContext = ctx;
	}

	@Override
	public void initUI() {

	}

	/**
	 * 显示 Toast
	 * 
	 * @param obj
	 *            可以为空
	 */
	@Override
	public void showToast(Object obj) {
		JarApplication.showToast(obj);
	}

	// =======================ProgressDialog
	private ProgressDialog pd;
	private Integer number = 0;
	/**
	 * 显示progress
	 */
	@Override
	public void showProgress(Object mes) {
		synchronized(number){
			if (mes==null) {
				mes ="  请稍后......";
			}
			final Object str = mes+"";
			++number;
//			JarApplication.post(new Runnable() {
//				@Override
//				public void run() {
			if (mContext==null){
				return;
			}
			((Activity)mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (pd==null) {
						pd = new ProgressDialog(mContext);
					}
					pd.setMessage(str+"");
					pd.setCancelable(false);
					pd.getWindow().setWindowAnimations(R.style.pop_anim);
					try{
						pd.show();
					}catch (Exception e){

					}
//				}
				}
			});
//			});
		}
	}

	@Override
	public void dismissProgress() {
				synchronized(number){
					--number;

					if (number>0||pd == null) {
						return ;
					}
					pd.dismiss();
					pd=null;
				}
	}

	@Override
	public void toActivity(Class<?> c) {
		mContext.startActivity(new Intent(mContext,c));
	}
	
//	@Override
//	public void removeCallback() {
//		for (int i = callbacks.size()-1; i > -1; --i) {
//			MCallback remove = callbacks.remove(i);
//			remove.cancel();
//			remove = null;
//		}
//		for (int i = callbacks.size()-1; i > -1; --i) {
//			Runnable remove = runnables.remove(i);
//			JarApp.mHandler.removeCallbacks(remove);
//		}
//	}


}
