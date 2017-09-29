package com.sitsmice.herbalife_jar.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sitsmice.herbalife_jar.JarApplication;
import com.sitsmice.idevevt_jar.R;

import java.io.File;

/**
 * 相关文档：http://blog.csdn.net/vipzjyno1/article/details/23206387
 * 		  http://blog.csdn.net/xiaanming/article/details/26810303
 * 
 *  String imageUri = "http://site.com/image.png"; // from Web  
	String imageUri = "file:///mnt/sdcard/image.png"; // from SD card  
	String imageUri = "content://media/external/audio/albumart/13"; // from content provider  
	String imageUri = "assets://image.png"; // from assets  
	String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch) 
 * @author Le
 *
 */
public class UtilImageLoad {

	public static DisplayImageOptions personOptions(){
		DisplayImageOptions options=new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.idcard_default)
		.showImageForEmptyUri(R.drawable.idcard_default)//设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.idcard_default) //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(false)// 设置下载的图片是否缓存在内存中
		.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
		.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
		.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
		.build();
		return options;
	}
	
	public static DisplayImageOptions defaultOptions(){
		DisplayImageOptions options=new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.img_mission_default_bg) //设置图片在下载期间显示的图片
		.showImageForEmptyUri(R.drawable.img_mission_default_bg)//设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.img_mission_default_bg) //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(false)// 设置下载的图片是否缓存在内存中
		.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
		.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
		.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
		.build();
		return options;
	}

	public static DisplayImageOptions defaultOptions_user(){
		DisplayImageOptions options=new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.img_checkin_user_detail_icon) //设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.img_checkin_user_detail_icon)//设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.img_checkin_user_detail_icon) //设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(false)// 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
				.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.build();
		return options;
	}

	public static DisplayImageOptions defaultOptions_user_logo(){
		DisplayImageOptions options=new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.img_profile_icon) //设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.img_profile_icon)//设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.img_profile_icon) //设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(false)// 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
				.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.build();
		return options;
	}

	public static DisplayImageOptions defaultOptions_tasktype(){
		DisplayImageOptions options=new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.img_task_creat_icon) //设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.img_task_creat_icon)//设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.img_task_creat_icon) //设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(false)// 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
				.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.build();
		return options;
	}
	
	/**
	 * ImageLoader.getInstance().init(config);//全局初始化此配置
	 *  //创建默认的ImageLoader配置参数  
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this); 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static ImageLoaderConfiguration initConfig(){
		File cacheDir = StorageUtils.getOwnCacheDirectory(JarApplication.mContext, "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
			    .Builder(JarApplication.getInstance())
			    .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽  
			    .threadPoolSize(3)//线程池内加载的数量  
			    .threadPriority(Thread.NORM_PRIORITY - 2)  
			    .denyCacheImageMultipleSizesInMemory()  
			    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
			    .memoryCacheSize(2 * 1024 * 1024)    
			    .discCacheSize(50 * 1024 * 1024)    
			    .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密  
			    .tasksProcessingOrder(QueueProcessingType.LIFO)  
			    .discCacheFileCount(100) //缓存的文件数量  
			    .discCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径  
			    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
			    .imageDownloader(new BaseImageDownloader(JarApplication.getInstance(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
			    .writeDebugLogs() // Remove for release app  
			    .build();//开始构建  
		return config;
	}
	
}
