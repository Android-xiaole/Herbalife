/** 
 * TuSdkDemo
 * EditAvatarComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:36:55 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package com.herbalife.myapplication.examples.suite;

import android.app.Activity;

import com.herbalife.myapplication.R;
import com.herbalife.myapplication.SampleBase;
import com.herbalife.myapplication.SampleGroup;

import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuAvatarComponent;
import org.lasque.tusdk.modules.components.TuSdkComponent.TuSdkComponentDelegate;

import java.util.Arrays;


/**
 * 头像设置组件(编辑)范例
 * 
 * @author Clear
 */
public class EditAvatarComponentSample extends SampleBase
{
	/** 头像设置组件(编辑)范例 */
	public EditAvatarComponentSample()
	{
		super(SampleGroup.GroupType.SuiteSample, R.string.sample_EditAvatarComponent);
	}

	/** 显示范例 */
	@Override
	public void showSample(Activity activity)
	{
		if (activity == null) return;

		// 组件选项配置
		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuAvatarComponent.html
		TuAvatarComponent component = TuSdkGeeV1.avatarCommponent(activity, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment)
			{
				TLog.d("onAvatarComponentReaded: %s | %s", result, error);
			}
		});

		// 组件选项配置
		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuAvatarComponentOption.html
		// component.componentOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/album/TuAlbumListOption.html
		// component.componentOption().albumListOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/album/TuPhotoListOption.html
		// component.componentOption().photoListOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/camera/TuCameraOption.html
		// component.componentOption().cameraOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditTurnAndCutOption.html
		// component.componentOption().editTurnAndCutOption()

		// 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜, 可选)
		String[] filters = {
				"SkinNature", "SkinPink", "SkinJelly", "SkinNoir", "SkinRuddy", "SkinPowder", "SkinSugar" };
		component.componentOption().cameraOption().setFilterGroup(Arrays.asList(filters));

		component
		// 在组件执行完成后自动关闭组件
				.setAutoDismissWhenCompleted(true)
				// 显示组件
				.showComponent();
	}
}