/** 
 * TuSdkDemo
 * SimpleCameraFragment.java
 *
 * @author 		Clear
 * @Date 		2014-11-20 下午1:22:19 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package com.herbalife.myapplication.examples.api;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.herbalife.myapplication.R;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter.CameraState;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface.TuSdkStillCameraListener;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFilterView;
import org.lasque.tusdk.impl.components.camera.TuCameraFilterView.TuCameraFilterViewDelegate;
import org.lasque.tusdk.impl.components.camera.TuFocusTouchView;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;

import java.util.ArrayList;


/**
 * 快速相机范例
 * 
 * @author Clear
 */

public class DefineCameraBaseFragment extends TuFragment
{
	/** 布局ID */
	public static final int layoutId = R.layout.demo_define_camera_base_fragment;

	public DefineCameraBaseFragment()
	{
		this.setRootViewLayoutId(layoutId);
	}

	/** 相机视图 */
	private RelativeLayout cameraView;
	// 配置栏
	// private LinearLayout configBar;
	/** 取消按钮 */
	private TextView cancelButton;
	/** 闪光灯栏 */
	private LinearLayout flashBar;
	/** 闪光灯 关闭按钮 */
	private TextView flashOffButton;
	/** 闪光灯 自动按钮 */
	private TextView flashAutoButton;
	/** 闪光灯 开启按钮 */
	private TextView flashOpenButton;
	/** 切换前后摄像头按钮 */
	private TextView switchCameraButton;
	// 底部栏
	// private RelativeLayout bottomBar;
	/** 拍摄按钮 */
	private Button captureButton;
	/** 滤镜选择栏 */
	private TuCameraFilterView filterBar;
	/** 滤镜开关按钮 */
	private TextView filterToggleButton;
	/** 闪光灯按钮列表 */
	private ArrayList<TextView> mFlashBtns = new ArrayList<TextView>(3);
	/** 相机对象 */
	private TuSdkStillCameraInterface mCamera;
	/** 默认闪关灯模式 */
	private CameraFlash mFlashModel = CameraFlash.Off;

	@Override
	protected void loadView(ViewGroup view)
	{
		// sdk统计代码，请不要加入您的应用
		StatisticsManger.appendComponent(ComponentActType.sdkSimpleCamera);

		// 相机视图
		cameraView = this.getViewById(R.id.cameraView);
		// 配置栏
		// configBar = this.getViewById(R.id.configBar);
		// 取消按钮
		cancelButton = this.getViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(mClickListener);
		// 闪光灯栏
		flashBar = this.getViewById(R.id.flashBar);
		// 闪光灯 关闭按钮
		flashOffButton = this.getViewById(R.id.flashOffButton);
		flashOffButton.setOnClickListener(mClickListener);
		flashOffButton.setTag(CameraFlash.Off);
		mFlashBtns.add(flashOffButton);
		// 闪光灯 自动按钮
		flashAutoButton = this.getViewById(R.id.flashAutoButton);
		flashAutoButton.setOnClickListener(mClickListener);
		flashAutoButton.setTag(CameraFlash.Auto);
		mFlashBtns.add(flashAutoButton);
		// 闪光灯 开启按钮
		flashOpenButton = this.getViewById(R.id.flashOpenButton);
		flashOpenButton.setOnClickListener(mClickListener);
		flashOpenButton.setTag(CameraFlash.On);
		mFlashBtns.add(flashOpenButton);
		// 切换前后摄像头按钮
		switchCameraButton = this.getViewById(R.id.switchCameraButton);
		switchCameraButton.setOnClickListener(mClickListener);
		// 设置是否显示前后摄像头切换按钮
		this.showViewIn(switchCameraButton, CameraHelper.cameraCounts() > 1);
		// 底部栏
		// bottomBar = this.getViewById(R.id.bottomBar);
		// 拍摄按钮
		captureButton = this.getViewById(R.id.captureButton);
		captureButton.setOnClickListener(mClickListener);

		// 滤镜开关按钮
		filterToggleButton = this.getViewById(R.id.filterButton);
		filterToggleButton.setOnClickListener(mClickListener);
		// 滤镜选择栏
		filterBar = this.getViewById(R.id.lsq_group_filter_view);
		// 设置控制器
		filterBar.setActivity(this.getActivity());
		// 绑定选择委托
		filterBar.setDelegate(mFilterBarDelegate);

		// 设置默认是否显示
		filterBar.setDefaultShowState(true);
		// 显示滤镜标题视图
		filterBar.setDisplaySubtitles(true);

		// 滤镜选择栏 设置SDK内置滤镜
		filterBar.loadFilters();

		this.setFlashModel(mFlashModel);
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		// 创建相机对象
		mCamera = TuSdk.camera(this.getActivity(), CameraFacing.Back, this.cameraView);
		// 相机对象事件监听
		mCamera.setCameraListener(mCameraListener);

		// 可选：设置输出图片分辨率
		// 注意：因为移动设备内存问题，可能会限制部分机型使用最高分辨率
		// 请使用 TuSdkGPU.getGpuType().getSize() 查看当前设备所能够进行处理的图片尺寸
		// 默认使用 1920 * 1440分辨率
		// mCamera.setOutputSize(new TuSdkSize(5312, 2988));

		// 可选，设置相机手动聚焦
		mCamera.adapter().setFocusTouchView(TuFocusTouchView.getLayoutId());
		// 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限)
		mCamera.setEnableFaceDetection(true);
		// 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
		// mCamera.setDisableMirrorFrontFacing(true);
		// 启动相机
		mCamera.startCameraCapture();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (!this.isFragmentPause() && mCamera != null)
		{
			mCamera.startCameraCapture();
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (mCamera != null) mCamera.stopCameraCapture();
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		if (mCamera != null)
		{
			mCamera.destroy();
			mCamera = null;
		}
	}

	/** 滤镜选择栏委托 */
	private TuCameraFilterViewDelegate mFilterBarDelegate = new TuCameraFilterViewDelegate()
	{
		/**
		 * @param view
		 *            滤镜分组视图
		 * @param itemData
		 *            滤镜分组元素
		 * @param canCapture
		 *            是否允许拍摄
		 * @return 是否允许继续执行
		 */
		@Override
		public boolean onGroupFilterSelected(TuCameraFilterView view, GroupFilterItem itemData, boolean canCapture)
		{
			// 直接拍照
			if (canCapture)
			{
				handleCaptureAction();
				return true;
			}

			switch (itemData.type)
				{
				case TypeFilter:
					// 设置滤镜
					return handleSwitchFilter(itemData.filterOption);
				default:
					break;
				}
			return true;
		}

		@Override
		public void onGroupFilterShowStateChanged(TuCameraFilterView view, boolean isShow)
		{

		}
	};

	/** 按钮点击事件 */
	private OnClickListener mClickListener = new OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			switch (v.getId())
				{
				// 取消
				case R.id.cancelButton:
					handleCancelAction();
					break;
				// 闪光灯
				case R.id.flashOffButton:
				case R.id.flashAutoButton:
				case R.id.flashOpenButton:
					handleFlashAction(v);
					break;
				// 切换摄像头
				case R.id.switchCameraButton:
					handleSwitchCameraAction();
					break;
				// 拍摄
				case R.id.captureButton:
					handleCaptureAction();
					break;
				// 滤镜开关切换按钮
				case R.id.filterButton:
					handleToggleFilterAction();
					break;
				default:
					break;
				}
		}
	};

	/** 取消动作 */
	private void handleCancelAction()
	{
		this.dismissActivityWithAnim();
	}

	/** 滤镜开关切换按钮 */
	protected void handleToggleFilterAction()
	{
		filterBar.showGroupView();
	}

	/** 闪光灯动作 */
	private void handleFlashAction(View view)
	{
		this.setFlashModel((CameraFlash) view.getTag());
	}

	/** 设置闪光灯模式 */
	private void setFlashModel(CameraFlash flashMode)
	{
		mFlashModel = flashMode;
		for (TextView btn : mFlashBtns)
		{
			if (flashMode.equals(btn.getTag()))
			{
				btn.setTextColor(this.getResColor(R.color.demo_flash_selected));
			}
			else
			{
				btn.setTextColor(this.getResColor(R.color.demo_flash_normal));
			}
		}
		if (mCamera != null)
		{
			mCamera.setFlashMode(flashMode);
		}
	}

	/** 切换摄像头 */
	private void handleSwitchCameraAction()
	{
		if (mCamera != null)
		{
			mCamera.rotateCamera();
		}
	}

	/** 拍照 */
	private void handleCaptureAction()
	{
		if (mCamera != null)
		{
			mCamera.captureImage();
		}
	}

	/**
	 * 处理滤镜切换
	 * 
	 * @param opt
	 * @return 是否允许切换
	 */
	private boolean handleSwitchFilter(FilterOption opt)
	{
		if (mCamera == null) return false;

		String code = FilterLocalPackage.NormalFilterCode;
		if (opt != null)
		{
			code = opt.code;
		}

		mCamera.switchFilter(code);
		return true;
	}

	/** 相机监听委托 */
	private TuSdkStillCameraListener mCameraListener = new TuSdkStillCameraListener()
	{
		/**
		 * 相机状态改变 (如需操作UI线程， 请检查当前线程是否为主线程)
		 * 
		 * @param camera
		 *            相机对象
		 * @param state
		 *            相机运行状态
		 */
		@Override
		public void onStillCameraStateChanged(TuSdkStillCameraInterface camera, CameraState state)
		{
			if (state != CameraState.StateStarted) return;

			if (camera.canSupportFlash())
			{
				camera.setFlashMode(mFlashModel);
				showViewIn(flashBar, true);
			}
			else
			{
				showViewIn(flashBar, false);
			}
			// 输出相机设置信息
			// CameraHelper.logParameters(mCamera.getCameraParameters());
		}

		/**
		 * 获取拍摄图片 (如需操作UI线程， 请检查当前线程是否为主线程)
		 * 
		 * @param camera
		 *            相机对象
		 * @param result
		 *            Sdk执行结果
		 */
		@Override
		public void onStillCameraTakedPicture(TuSdkStillCameraInterface camera, final TuSdkResult result)
		{
			new Handler(Looper.getMainLooper()).post(new Runnable()
			{
				@Override
				public void run()
				{
					test(result);
				}
			});
		}
	};

	/** 测试方法 */
	private void test(TuSdkResult result)
	{
		result.logInfo();

		Bitmap image = result.image;

		ImageView imageView = new ImageView(this.getActivity());
		imageView.setBackgroundColor(Color.GRAY);
		imageView.setScaleType(ScaleType.FIT_CENTER);
		imageView.setImageBitmap(image);
		imageView.setOnClickListener(mImageViewClickListener);
		cameraView.addView(imageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	private OnClickListener mImageViewClickListener = new OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			v.setOnClickListener(null);
			cameraView.removeView(v);
			mCamera.resumeCameraCapture();
		}
	};
}