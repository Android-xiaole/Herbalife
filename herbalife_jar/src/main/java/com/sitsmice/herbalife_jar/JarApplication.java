package com.sitsmice.herbalife_jar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.view.WindowManager;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sitsmice.herbalife_jar.utils.UtilFile;
import com.sitsmice.herbalife_jar.utils.UtilImageLoad;
import com.tencent.bugly.crashreport.CrashReport;

import org.xutils.x;

import java.util.List;


public class JarApplication extends Application {

	public static JarApplication mApplication;//Application单例
	public static IDEventHttpManager mHttpManger;//网络请求对象单例
	public static DisplayImageOptions options, options_user ,options_user_logo, options_tasktype;//imageload框架设置参数
	public static ObjectMapper mapper;//jackson解析数据
	public static SharedPreferences sPreferences;//共享参数对象

	public static Context mContext;
	public static Toast mToast;
	public static Handler mHandler;

	public static String mSdPath;// sd
	public static String mAppName = "herbalife";
	public static String mCompany = "SitSmice";
	/** 缓存 */
//	public static String mCachePath;//
	/** 私有文件 */
	public static String mBasePath;//基础文件夹路径
	public static String mFilesPath;// 私有文件
	public static String mErrLogPath;
	public static String mDownLoadPath;
	public static String mCachePath;

	public static float density;
	public static int scrnWidth; // 屏幕宽度
	public static int scrnHeight; // 屏幕高度
	public static Resources mResources;
	public static String myAppVersion;

	@SuppressLint("ShowToast")
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		System.gc();
		PackageManager manager = this.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			myAppVersion = info.versionName + "." + info.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		CrashReport.initCrashReport(getApplicationContext(), "900037329", Config.debug);
//		MLog.e("当前经度" + getLongitude());
//		MLog.e( "当前纬度" + getLatitude());
		x.Ext.init(this);//初始化xUtils3框架
		x.Ext.setDebug(Config.debug);//是否输出debug日志，开启debug会影响功能

		initImageLoader();//初始化imageload
		options = UtilImageLoad.defaultOptions();
		options_user = UtilImageLoad.defaultOptions_user();
		options_user_logo = UtilImageLoad.defaultOptions_user_logo();
		options_tasktype = UtilImageLoad.defaultOptions_tasktype();

		mapper = new ObjectMapper();
		sPreferences = getSharedPreferences("Flag", MODE_PRIVATE);

		mApplication = this;
		mContext = getApplicationContext();
		mResources = getResources();
		mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);

		density = getResources().getDisplayMetrics().density;
		mHandler = new Handler(getMainLooper());

		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		scrnHeight = wm.getDefaultDisplay().getHeight();
		scrnWidth = wm.getDefaultDisplay().getWidth();

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			mSdPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/";// /mnt/sdcard
//			mSdPath = "sdcard/";
		}
//		mCachePath = getCacheDir().getAbsolutePath(); // /data/data/com.xiaoniu.jar/cache-----------缓存
		mFilesPath = getFilesDir().getAbsolutePath(); // /data/data/com.xiaoniu.jar/files
		mBasePath = JarApplication.mSdPath + mCompany;
		mErrLogPath = JarApplication.mSdPath + mCompany + "/" + mAppName
				+ "/Log/";
		mDownLoadPath = JarApplication.mSdPath + mCompany + "/" + mAppName
				+ "/DownLoad/";
		mCachePath = JarApplication.mSdPath + mCompany + "/" + mAppName
				+ "/Cache/";

		UtilFile.isHaveFile(mErrLogPath);//生成错误文件夹
		UtilFile.isHaveFile(mDownLoadPath);//生成图片文件夹
		UtilFile.isHaveFile(mCachePath);//生成缓存文件夹

	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	public synchronized static JarApplication getInstance() {
		return mApplication;
	}

	/**
	 * 获得HttpMangerWithCookie单例
	 *
	 * @return
	 */
	public IDEventHttpManager getHttpManger() {
		if (mHttpManger == null) {
			mHttpManger = new IDEventHttpManager();
		}
		return mHttpManger;
	}

	/**
	 * 将JSONObject转换成Object
	 *
	 * @param <T>
	 * @param json
	 * @return
	 */
	public static <T> T JsonToObject(String json, Class<T> t) {
		T t1 = null;
		try {
			t1 = mapper.readValue(json, t);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t1;
	}

	/**
	 * 将Object转换成JSON格式的Sting
	 *
	 * @param <T>
	 * @param t
	 */
	public static <T> String ObjectToJson(T t) {
		String json = null;
		try {
			json = mapper.writeValueAsString(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 将JSONArray转换成List
	 *
	 * @param json
	 * @param t
	 * @return
	 */
	public static <T> List<T> JsonToList(String json, Class<T> t) {
		List<T> list = null;
		try {
			list = mapper.readValue(json, mapper.getTypeFactory()
					.constructCollectionType(List.class, t));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 将JSONArray转换成List
	 * @return
	 */
	public static <T> String ListToJson(List<T> list) {
		String s = null;
		try {
			s = mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * handler.post() 这里为了try-catch
	 */
	public static void post(Runnable r) {
		post(r, 0);
	}

	/**
	 * handler.post() 这里为了try-catch
	 */
	public static void post(Runnable r, int time) {
		try {
			mHandler.postDelayed(r, time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示 Toast 放这里是为了在任何地方都可用
	 *
	 * @param obj
	 *            可以为空
	 */
	public static void showToast(final Object obj) {
		JarApplication.post(new Runnable() {
			@Override
			public void run() {
				JarApplication.mToast.setText(obj + "");
				JarApplication.mToast.show();
			}
		});
	}

	/**
	 * 获取当前经度
	 */
	public String getLongitude() {
		Location location;
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//获取所有可用的位置提供器
		List<String> providers = locationManager.getProviders(true);
		String locationProvider;
		if (providers.contains(LocationManager.GPS_PROVIDER)) {
			//如果是GPS
			locationProvider = LocationManager.GPS_PROVIDER;
		} else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
			//如果是Network
			locationProvider = LocationManager.NETWORK_PROVIDER;
		} else {
//			Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
			return null;
		}
//		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//			// TODO: Consider calling
//			//    ActivityCompat#requestPermissions
//			// here to request the missing permissions, and then overriding
//			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//			//                                          int[] grantResults)
//			// to handle the case where the user grants the permission. See the documentation
//			// for ActivityCompat#requestPermissions for more details.
//		}
//		location = locationManager.getLastKnownLocation(locationProvider);
//		if (location!=null){
//			return location.getLongitude()+"";
//		}
		return null;
	}

	/**
	 * 获取当前纬度
	 */
	public String getLatitude(){
		Location location ;
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//获取所有可用的位置提供器
		List<String> providers = locationManager.getProviders(true);
		String locationProvider;
		if (providers.contains(LocationManager.GPS_PROVIDER)) {
			//如果是GPS
			locationProvider = LocationManager.GPS_PROVIDER;
		} else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
			//如果是Network
			locationProvider = LocationManager.NETWORK_PROVIDER;
		} else {
//			Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
			return null;
		}
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return null;
		}
		location = locationManager.getLastKnownLocation(locationProvider);
		if (location!=null){
			return location.getLatitude()+"";
		}
		return null;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
//	public static int dp2px(float dp) {
//		return (int) (dp * density + 0.5f);
//	}
//
//	/**
//	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
//	 */
//	public static int px2dp(float px) {
//		return (int) (px / density + 0.5f);
//	}

	/**
	 * 初始化ImageLoader
	 */

//	private void initImageLoader(Context context) {
//		// 缓存文件的目录
//		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
//				"SitSmice/iDEvent/Cache");
//		// 初始化图片加载库
//		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
//				.cacheOnDisk(true)
//						// 图片存本地
//				.displayer(new FadeInBitmapDisplayer(50))
//				.bitmapConfig(Bitmap.Config.RGB_565)
//				.imageScaleType(ImageScaleType.EXACTLY) // default
//				.cacheInMemory(false)// 启用内存缓存
//				.build(); // 启用硬盘缓存
//
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				context)
//				.memoryCacheExtraOptions(480, 800)
//						// max width, max height，即保存的每个缓存文件的最大长宽
//				.threadPoolSize(3)
//						// 线程池内加载的数量
//				.threadPriority(Thread.NORM_PRIORITY - 2)
//				.denyCacheImageMultipleSizesInMemory()
//				.memoryCache(new WeakMemoryCache())
//						// .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
//						// 1024)) // You
//				.memoryCacheSize(2 * 1024 * 1024)
//						// 内存缓存的最大值
//				.diskCacheSize(50 * 1024 * 1024)
//				.tasksProcessingOrder(QueueProcessingType.LIFO)
//						// 由原先的discCache -> diskCache
//				.diskCache(new UnlimitedDiskCache(cacheDir))
//				.diskCacheFileCount(100)
//				.imageDownloader(
//						new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
//				.defaultDisplayImageOptions(displayImageOptions) // default
//						// .writeDebugLogs() // Remove for release app
//				.build();
//		// 全局初始化此配置
//		ImageLoader.getInstance().init(config);
//
//	}

	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(false)
				.imageScaleType(ImageScaleType.EXACTLY)
				.cacheOnDisk(true)
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.defaultDisplayImageOptions(defaultOptions)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCache(new UnlimitedDiskCache(StorageUtils.getOwnCacheDirectory(this, Environment.getExternalStorageDirectory() + "/StickerCamera"+"/image")))
				.diskCacheSize(100 * 1024 * 1024).tasksProcessingOrder(QueueProcessingType.LIFO)
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024)
				.threadPoolSize(3)
				.build();
		ImageLoader.getInstance().init(config);
	}

}
