package com.herbalife.myapplication.activity;


import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.herbalife.myapplication.R;
import com.herbalife.myapplication.fragment.Frag_Camera;
import com.herbalife.myapplication.fragment.Frag_Game;
import com.sitsmice.herbalife_jar.JarApplication;
import com.sitsmice.herbalife_jar.base.BaseFragActivity;
import com.sitsmice.herbalife_jar.utils.UtilFile;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhy.autolayout.AutoLinearLayout;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;

import java.util.List;


public class Act_Home extends BaseFragActivity {

    private AutoLinearLayout lin_tab1, lin_tab2, lin_tab3, lin_tab4,lin_tab;
    /**
     * 滤镜管理器委托
     */
    private FilterManager.FilterManagerDelegate mFilterManagerDelegate = new FilterManager.FilterManagerDelegate() {
        @Override
        public void onFilterManagerInited(FilterManager manager) {
            TuSdk.messageHub().showSuccess(Act_Home.this, R.string.lsq_inited);
        }
    };
    private View selectView;
    private boolean isPermissionOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        initUI();
        frags.add(new Frag_Game());
        frags.add(new Frag_Camera());
        loadFragment(0);

        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(Act_Home.this, rationale).show();
                    }
                })
                .send();

    }

    @PermissionYes(100)
    private void getPermissionYes(List<String> grantedPermissions){
        UtilFile.isHaveFile(JarApplication.mErrLogPath);//生成错误文件夹
        UtilFile.isHaveFile(JarApplication.mDownLoadPath);//生成图片文件夹
        UtilFile.isHaveFile(JarApplication.mCachePath);//生成缓存文件夹
//        showToast("获取权限成功！");
    }

    @PermissionNo(100)
    private void getPermissionNo(List<String> grantedPermissions){
        showToast("获取权限失败！");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * 转给AndPermission分析结果。
         *
         * @param object     要接受结果的Activity、Fragment。
         * @param requestCode  请求码。
         * @param permissions  权限数组，一个或者多个。
         * @param grantResults 请求结果。
         */
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void initUI() {
        lin_tab = (AutoLinearLayout) findViewById(R.id.lin_tab);
        lin_tab1 = (AutoLinearLayout) findViewById(R.id.lin_tab1);
        lin_tab2 = (AutoLinearLayout) findViewById(R.id.lin_tab2);
//        lin_tab3 = (AutoLinearLayout) findViewById(R.id.lin_tab3);
//        lin_tab4 = (AutoLinearLayout) findViewById(R.id.lin_tab4);
        lin_tab1.setOnClickListener(this);
        lin_tab2.setOnClickListener(this);
//        lin_tab3.setOnClickListener(this);
//        lin_tab4.setOnClickListener(this);

        lin_tab1.setSelected(true);
        selectView = lin_tab1;
    }

    private int position;
    @Override
    public void onClick(View v) {
//        if (v.isSelected()) {
//            return;
//        }
//        if (selectView != v) {
//            selectView.setSelected(false);
//            v.setSelected(true);
//            selectView = v;
//        }
        switch (v.getId()) {
            case R.id.lin_tab1:
                position = 0;
                lin_tab1.setSelected(true);
                lin_tab2.setSelected(false);
//                lin_tab3.setSelected(false);
//                lin_tab4.setSelected(false);
                loadFragment(0);
                break;
            case R.id.lin_tab2:
                lin_tab1.setSelected(false);
                lin_tab2.setSelected(true);
//                lin_tab3.setSelected(false);
//                lin_tab4.setSelected(false);
//                Fragment fragment = frags.get(1);
//                if (!fragment.isAdded()){
//                    frags.set(1,new Frag_Camera());
//                }
                loadFragment(1);
//                setTable(View.GONE);
                break;
//            case R.id.lin_tab3:
//                position = 2;
//                lin_tab1.setSelected(false);
//                lin_tab2.setSelected(false);
//                lin_tab3.setSelected(true);
//                lin_tab4.setSelected(false);
//                loadFragment(2);
//                break;
//            case R.id.lin_tab4:
//                position = 3;
//                lin_tab1.setSelected(false);
//                lin_tab2.setSelected(false);
//                lin_tab3.setSelected(false);
//                lin_tab4.setSelected(true);
//                loadFragment(3);
//                break;
        }
    }

    public void setTable(int i) {
        if (i == View.GONE) {
            lin_tab.setAnimation(AnimationUtils.loadAnimation(this, R.anim.tab_out));
        } else {
            lin_tab.setAnimation(AnimationUtils.loadAnimation(this, R.anim.tab_in));
        }
        lin_tab.getAnimation().start();
        lin_tab.setVisibility(i);
    }

    public void removeFrag(){
        fragmentTransaction.remove(frags.get(1));//remove之后这个fragment事例会被销毁的，所以想重新现实需要new一个对象再调add方法
    }

    public void showFrag(){
        if (position==0){
            lin_tab1.setSelected(true);
            lin_tab2.setSelected(false);
            lin_tab3.setSelected(false);
            lin_tab4.setSelected(false);
        }
        if (position==2){
            lin_tab1.setSelected(false);
            lin_tab2.setSelected(false);
            lin_tab3.setSelected(true);
            lin_tab4.setSelected(false);
        }
        if (position==3){
            lin_tab1.setSelected(false);
            lin_tab2.setSelected(false);
            lin_tab3.setSelected(false);
            lin_tab4.setSelected(true);
        }
        loadFragment(position);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
