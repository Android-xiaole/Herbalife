package com.sitsmice.herbalife_jar;


import android.util.Log;

public class MLog {
	
public static boolean debug = Config.debug;
	
	/**
	 * 错误
	 * @param msg
	 */
	public static void e(String msg){
		if (debug) {
			Log.e("test", msg);
		}
	}

	/**
	 * 错误
	 * @param tag
	 * @param msg
     */
	public static void e(String tag,String msg){
		if (debug) {
			Log.e(tag, msg);
		}
	}
	
	/**
	 * 信息
	 * @param msg
	 */
	public static void i(String msg){
		if (debug) {
			Log.i("test", msg);
		}
	} 
	
	/**
	 * 警告
	 * @param msg
	 */
	public static void w(String msg){
		if (debug) {
			Log.w("test", msg);
		}
	}


}
