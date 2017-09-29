package com.sitsmice.herbalife_jar.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.sitsmice.herbalife_jar.IDEventHttpManager;
import com.sitsmice.idevevt_jar.R;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;


public class BaseFragActivity extends FragmentActivity implements OnClickListener, IBaseUI, BackHandledInterface {

    public ArrayList<Fragment> frags = new ArrayList<>();
    protected AutoFrameLayout auto_FrameL;
    protected AutoLinearLayout auto_icon, auto_menu;
    protected TextView title_icon, title_icont, title_text, title_menu, title_menut;
    protected BaseUI baseUI;
    protected IDEventHttpManager mHttpManager;
    protected FragmentManager fragmentManger = getSupportFragmentManager();
    protected FragmentTransaction fragmentTransaction;
    protected Fragment currentFrg = null;
    protected InputMethodManager inputMethodManager;
    private BackHandledFragment mBackHandedFragment;

    protected void loadFragment(int position) {
        Fragment f = frags.get(position);
        fragmentTransaction = fragmentManger.beginTransaction();
        if (currentFrg != null) {
            fragmentTransaction.hide(currentFrg);
        }
        if (!f.isAdded()) {
            fragmentTransaction.add(R.id.frameLayout, f);
        } else {
            fragmentTransaction.show(f);
        }
        currentFrg = f;
        fragmentTransaction.commit();
    }

    protected void loadFragment(Fragment f) {
        fragmentTransaction = fragmentManger.beginTransaction();
        if (currentFrg != null) {
            fragmentTransaction.hide(currentFrg);
        }
        if (!f.isAdded()) {
            fragmentTransaction.add(R.id.frameLayout, f);
        } else {
            fragmentTransaction.show(f);
        }
        currentFrg = f;
        fragmentTransaction.commit();
    }



    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        baseUI = new BaseUI(this);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//取消横屏
//        mHttpManager = JarApplication.getInstance().getHttpManger();

        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//        } else {
//            showToast("请先到应用中心打开相关权限！");
//        }
//    }

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

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void initUI() {
        initTitle();
    }

    @Override
    public void showToast(Object msg) {
        baseUI.showToast(msg);
    }

    @Override
    public void showProgress(Object msg) {
        baseUI.showProgress(msg);
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
        if (v == auto_icon) {
            finish();
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
