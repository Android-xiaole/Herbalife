/** 
 * TuSdkDemo
 * DefineCameraSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:40:59 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package com.herbalife.myapplication.examples.api;

import android.app.Activity;

import com.herbalife.myapplication.R;
import com.herbalife.myapplication.SampleBase;
import com.herbalife.myapplication.SampleGroup;

import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;

/**
 * 自定义相机范例
 * 
 * @author Clear
 */
public class DefineCameraBaseSample extends SampleBase
{
	/**
	 * 自定义相机范例
	 */
	public DefineCameraBaseSample()
	{
		super(SampleGroup.GroupType.APISample, R.string.sample_api_CameraBase);
	}

	/**
	 * 显示范例
	 * 
	 * @param activity
	 */
	@Override
	public void showSample(Activity activity)
	{
		if (activity == null) return;

		// 如果不支持摄像头显示警告信息
		if (CameraHelper.showAlertIfNotSupportCamera(activity)) return;

		// see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/base/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);

		this.componentHelper.presentModalNavigationActivity(
				new DefineCameraBaseFragment(), true);
	}
}
