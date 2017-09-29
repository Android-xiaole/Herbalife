package com.sitsmice.herbalife_jar.base;


import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitsmice.herbalife_jar.IDEventHttpManager;
import com.sitsmice.herbalife_jar.JarApplication;
import com.sitsmice.idevevt_jar.R;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.AutoLinearLayout;


public class BaseActivity extends AutoLayoutActivity implements OnClickListener, IBaseUI {

    protected AutoFrameLayout auto_FrameL;
    protected AutoLinearLayout auto_icon, auto_menu;
    protected TextView title_icon, title_icont, title_text, title_menu, title_menut;

    //404UI
    protected AutoLinearLayout lin_404;
    protected ImageView iv_404;
    protected TextView tv_404_title, tv_404_content;
    protected Button btn_404;

    protected BaseUI baseUI;
    protected IDEventHttpManager mHttpManager;

    /**
     * 初始化title
     */
    protected void initTitle() {
        auto_FrameL = (AutoFrameLayout) findViewById(R.id.auto_frameL);
        auto_icon = (AutoLinearLayout) findViewById(R.id.auto_icon);
        auto_menu = (AutoLinearLayout) findViewById(R.id.auto_menu);
        title_icon = (TextView) findViewById(R.id.title_icon);
        title_icont = (TextView) findViewById(R.id.title_icont);
        title_text = (TextView) findViewById(R.id.title_text);
        title_menu = (TextView) findViewById(R.id.title_menu);
        title_menut = (TextView) findViewById(R.id.title_menut);
        auto_icon.setOnClickListener(this);
        auto_menu.setOnClickListener(this);
    }

    /**
     * 初始化404、无数据等界面UI
     */
    protected void init404() {
        //404 init
        lin_404 = (AutoLinearLayout) findViewById(R.id.lin_404);
        iv_404 = (ImageView) findViewById(R.id.iv_404);
        tv_404_title = (TextView) findViewById(R.id.tv_404_title);
        tv_404_content = (TextView) findViewById(R.id.tv_404_content);
        btn_404 = (Button) findViewById(R.id.btn_404);
        btn_404.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//取消横屏
        baseUI = new BaseUI(this);
        mHttpManager = JarApplication.getInstance().getHttpManger();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            showToast("请先到应用中心打开相关权限！");
        }
    }

    /**
     * 初始化UI，如果不想初始化title子类继承的时候可以不调super方法
     */
    @Override
    public void initUI() {
        initTitle();
    }

    @Override
    public void showToast(Object obj) {
        baseUI.showToast(obj);
    }

    @Override
    public void showProgress(Object mes) {
        baseUI.showProgress(mes);
    }

    @Override
    public void dismissProgress() {
        baseUI.dismissProgress();
    }

    @Override
    public void toActivity(Class<?> c) {
        baseUI.toActivity(c);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.auto_icon) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
