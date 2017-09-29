package com.herbalife.myapplication;

import android.util.DisplayMetrics;

import com.herbalife.myapplication.camera.AppConstants;
import com.jarek.imageselect.core.SDCardStoragePath;
import com.jarek.imageselect.utils.SDCardUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sitsmice.herbalife_jar.JarApplication;

import java.io.IOException;

/**
 * Created by sky on 2015/7/6.
 */
public class App extends JarApplication {

    protected static App mInstance;
    private DisplayMetrics     displayMetrics = null;

    public App(){
        mInstance = this;
    }

    public static App getApp() {
        if (mInstance != null && mInstance instanceof App) {
            return (App) mInstance;
        } else {
            mInstance = new App();
            mInstance.onCreate();
            return (App) mInstance;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        mInstance = this;
//        try{
//            LoadLanSongSdk.loadLibraries();  //拿出来单独加载库文件.
//            LanSoEditor.initSo(getApplicationContext(),null);
//        }catch (Exception e){
//            showToast("视频库加载失败，视频合成将无法使用！");
//            MLog.e("test","视频库加载失败："+e);
//        }

        //因为从android6.0系统有各种权限的限制, 这里开始先检查是否有读写的权限,PermissionsManager采用github上开源库,不属于我们sdk的一部分.
        //下载地址是:https://github.com/anthonycr/Grant,您也可以使用别的方式来检查app所需权限.
//        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(getApplicationContext(), new PermissionsResultAction() {
//            @Override
//            public void onGranted() {
//            }
//
//            @Override
//            public void onDenied(String permission) {
//            }
//        });
    }


    private void initImageLoader() {
        try {
            SDCardUtils.createFolder(SDCardStoragePath.DEFAULT_IMAGE_CACHE_PATH);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
                .diskCache(new UnlimitedDiskCache(StorageUtils.getOwnCacheDirectory(this, AppConstants.APP_IMAGE)))
                .diskCacheSize(100 * 1024 * 1024).tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024)
                .threadPoolSize(3)
                .build();
        ImageLoader.getInstance().init(config);
    }


    public float getScreenDensity() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.density;
    }

    public int getScreenHeight() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.widthPixels;
    }

    public void setDisplayMetrics(DisplayMetrics DisplayMetrics) {
        this.displayMetrics = DisplayMetrics;
    }

    public int dp2px(float f)
    {
        return (int)(0.5F + f * getScreenDensity());
    }

    public int px2dp(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }

    //获取应用的data/data/....File目录
    public String getFilesDirPath() {
        return getFilesDir().getAbsolutePath();
    }

    //获取应用的data/data/....Cache目录
    public String getCacheDirPath() {
        return getCacheDir().getAbsolutePath();
    }



}
