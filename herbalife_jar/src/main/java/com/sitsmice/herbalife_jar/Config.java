package com.sitsmice.herbalife_jar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Config {
//JM0oOm7f5kz/74WvoBWLjWYOHRI1y/jLPKv93qvb2mEW+sYVVqdPCLtPn1K2r2GIoDqKEJ4xflYWSox2jAw+NnY03hruBWa0dyJVOf3/u7dHLXunwO4gvAAmqBCP179q
//	public static final String http_base = "http://wangyun.api.mobile.idevent.cn";
	public static final boolean debug = true;//true:打开调试 fasle:关闭调试
	public static String cookie_value = "";
	public static String sign_value = "";
	public static int mid = 0;
	public static String tuid = "";
	public static String nickname = "";
	public static String tuid_login = "";
	public static List<Bundle> bundles = new ArrayList<>();//这个集合对象用于存储JPush推送过来的信息
	public static String BROADCAST_FLAG = "com.sitsmice.ui.fragment.Frag_Setting";
	public static String BROADCAST_TASK = "com.sitsmice.ui.activity.task.Act_TaskFaBu";
	public static String BROADCAST_REMIND = "com.sitsmice.ui.fragment.Frag_Reminder";
	public static String BROADCAST_CALLNAME = "com.sitsmice.ui.activity.sign.Act_CallName";

	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";//JPush

//	public static final Device_ID = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

	public static BitmapFactory.Options options;
	static{
		options = new BitmapFactory.Options();
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			options.inPurgeable = true;//inPurgeable 设为True的话表示使用BitmapFactory创建的Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
		}
		
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;//色彩模式
		options.inSampleSize = 1;
	}


}
