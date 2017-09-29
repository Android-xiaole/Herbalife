package com.sitsmice.herbalife_jar.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.sitsmice.herbalife_jar.Config;
import com.sitsmice.herbalife_jar.MLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by sahara on 2016/4/7.
 */
public class UtilNetWork {

    // 判断当前是否使用的是 WIFI网络
    public static boolean isWifiActive(Context icontext) {
        Context context = icontext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info;
        if (connectivity != null) {
            info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        MLog.e("test", "当前网络在wifi状态");
                        return true;
                    }
                }
            }
        }
        MLog.e("test", "当前网络不在wifi状态");
        return false;
    }

    /**
     * 检查当前网络是否可用
     * true 有网络| false 无网络
     *
     * @param context
     * @return
     */

    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            MLog.e("test", "网络不可用");
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        MLog.e("test", "网络可用");
                        return true;
                    }
                }
            }
        }
        MLog.e("test", "网络不可用");
        return false;
    }

    /**
     * 同步一下cookie
     */
    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.removeAllCookie();
        String oldCookie = null;
        try {
            oldCookie = "sign=" + URLEncoder.encode(Config.sign_value, "UTF-8");//sign值需要进行编码，不然后台解析错误
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MLog.e("test", "oldCookie:" + oldCookie);
        cookieManager.setCookie(url, oldCookie);//cookies是在之前的网络请求中获得的中获得的cookie
        CookieSyncManager.getInstance().sync();
        String newCookie = cookieManager.getCookie(url);
        if (newCookie != null) {
            MLog.e("test", "newCookie:" + newCookie);
        }
    }

}
