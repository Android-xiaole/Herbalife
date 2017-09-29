package com.sitsmice.herbalife_jar.base;

public interface IBaseUI {

	public void initUI();
	
	public void showToast(Object msg);

	public void showProgress(Object msg);

	public void dismissProgress();

	public void toActivity(Class<?> c);
}
