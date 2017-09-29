/** 
 * TuSdkDemo
 * SimpleBase.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午12:52:59 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package com.herbalife.myapplication;

import android.app.Activity;

import org.lasque.tusdk.modules.components.TuSdkHelperComponent;

/**
 * 范例接口，封装好的组件示例
 * 
 * @author Clear
 */
public abstract class SampleBase
{
	/** 分组ID */
	public SampleGroup.GroupType groupId;

	/** 标题资源ID */
	public int titleResId;
	
	/** 组件帮助方法 */
	// see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/base/TuSdkHelperComponent.html
	public TuSdkHelperComponent componentHelper;

	/**
	 * 范例分组头部信息
	 * 
	 * @param groupId
	 *            分组ID
	 * @param titleResId
	 *            标题资源ID
	 */
	public SampleBase(SampleGroup.GroupType groupId, int titleResId)
	{
		this.groupId = groupId;
		this.titleResId = titleResId;
	}

	/** 显示范例 */
	public abstract void showSample(Activity activity);
}