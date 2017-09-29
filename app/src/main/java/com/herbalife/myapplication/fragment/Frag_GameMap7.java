package com.herbalife.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.herbalife.myapplication.Fglass;
import com.herbalife.myapplication.ImageRes;
import com.herbalife.myapplication.MyApplication;
import com.herbalife.myapplication.R;
import com.herbalife.myapplication.ResultPopwindow;
import com.herbalife.myapplication.activity.Act_GameMap;
import com.herbalife.myapplication.activity.Act_MyBox;
import com.herbalife.myapplication.zxing.activity.Act_ZXing;
import com.sitsmice.herbalife_jar.base.BaseFragment;
import com.sitsmice.herbalife_jar.utils.UtilObjectIO;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import static com.sitsmice.herbalife_jar.utils.UtilObjectIO.readObject;


/**
 * Created by sahara on 2016/12/13.
 */

public class Frag_GameMap7 extends BaseFragment {

    private ImageView iv_true, iv_false, iv_goleft, iv_goright, iv_scan,iv_lock;
    private AutoLinearLayout lin_lock;
    private View view;
    private TextView tv_englishName, tv_cnName, tv_pager,tv_description,tv_lock;
    private ResultPopwindow popwindow;

    private ImageRes imageRes;
    private List<ImageRes> list;
    private String[] s = {"mybox"};

    private int position;

    public Frag_GameMap7() {
        r = R.layout.frag_gamemap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String simpleName = this.getClass().getSimpleName();
        position = Integer.valueOf(simpleName.substring(simpleName.length()-1,simpleName.length()))-1;
    }

    @Override
    protected void onCreateView() {
        list = readObject(ArrayList.class, MyApplication.mDownLoadPath);
        if (list == null){
            showToast("app出现异常，请先返回首页再进入！");
        }
        imageRes = list.get(position);
        if (imageRes == null){
            showToast("app出现异常，请先返回首页再进入！");
        }
        initUI();
    }

    private Intent intent;
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.auto_menu://跳转到宝箱 查看已获得贴纸的界面
                intent = new Intent(getActivity(), Act_MyBox.class);
                startActivityForResult(intent,1);
                break;
            case R.id.iv_goleft:
                ((Act_GameMap)getActivity()).goleft();
                break;
            case R.id.iv_goright:
                ((Act_GameMap)getActivity()).goright();
                break;
            case R.id.iv_scan:
                intent = new Intent(getActivity(), Act_ZXing.class);
                startActivityForResult(intent,0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){//扫描返回，弹出惊喜、随机贴纸
            if (imageRes.isLight){
                return;
            }
            //应该先播放解锁当前场景的动画，然后再显示弹出框
            MyApplication.post(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_gamemap_img);
                    iv_false.startAnimation(animation);
                    view.setVisibility(View.GONE);
                }
            },1000);
            Animation animation_top = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_gamemap_top);
            iv_lock.startAnimation(animation_top);
            Animation animation_bottom = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_gamemap_bottom);
            tv_lock.startAnimation(animation_bottom);
            MyApplication.post(new Runnable() {
                @Override
                public void run() {
                    if (popwindow==null){
                        popwindow = new ResultPopwindow(getActivity());
                    }
                    popwindow.showPopupWindow(iv_scan);
                    //随机抽取贴纸
                    List<ImageRes> arrayList = UtilObjectIO.readObject(ArrayList.class, MyApplication.mDownLoadPath, s);
                    if (arrayList==null){
                        arrayList = new ArrayList<>();
                    }
                    arrayList.add(new ImageRes("巧克力",R.drawable.img_tiezhi1,true));
                    arrayList.add(new ImageRes("巧克力奶昔",R.drawable.img_tiezhi2,true));
                    arrayList.add(new ImageRes("小黄花",R.drawable.img_tiezhi3,true));
                    arrayList.add(new ImageRes("草莓",R.drawable.img_tiezhi4,true));
                    UtilObjectIO.writeObject(arrayList,MyApplication.mDownLoadPath,s);
                    refreshRedPoint();
                    iv_scan.setVisibility(View.GONE);
                    tv_description.setVisibility(View.VISIBLE);
                }
            },4000);
            //将当前的场景标记成已经解锁
            imageRes.isLight = true;
            UtilObjectIO.writeObject(list,MyApplication.mDownLoadPath);
        }
        if (requestCode == 1){
            refreshRedPoint();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            refreshRedPoint();
        }
    }

    /**
     * 刷新小红点
     */
    private void refreshRedPoint(){
        if (iv_redPoint == null){
            return;
        }
        List<ImageRes> list = UtilObjectIO.readObject(ArrayList.class,MyApplication.mDownLoadPath,s);
        int n = 0;
        if (list!=null){
            for (ImageRes imageRes:list) {
                if (imageRes.isLight){
                    n++;
                }
            }
            if (n>0){
                iv_redPoint.setVisibility(View.VISIBLE);
            }else{
                iv_redPoint.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }

    private boolean flag;
    @Override
    public void initUI() {
        initTitle();
        title_text.setVisibility(View.GONE);
        auto_frameL.setBackgroundColor(getResources().getColor(R.color.none));
        title_icon.setBackgroundResource(R.drawable.img_game_back);
        title_menu.setBackgroundResource(R.drawable.img_game_map_chest);
        iv_true = (ImageView) mView.findViewById(R.id.iv_true);
        iv_false = (ImageView) mView.findViewById(R.id.iv_false);
        iv_goleft = (ImageView) mView.findViewById(R.id.iv_goleft);
        iv_goright = (ImageView) mView.findViewById(R.id.iv_goright);
        iv_scan = (ImageView) mView.findViewById(R.id.iv_scan);
        lin_lock = (AutoLinearLayout) mView.findViewById(R.id.lin_lock);
        tv_englishName = (TextView) mView.findViewById(R.id.tv_englishName);
        tv_cnName = (TextView) mView.findViewById(R.id.tv_cnName);
        tv_pager = (TextView) mView.findViewById(R.id.tv_pager);
        tv_description = (TextView) mView.findViewById(R.id.tv_description);
        iv_lock = (ImageView) mView.findViewById(R.id.iv_lock);
        tv_lock = (TextView) mView.findViewById(R.id.tv_lock);
        view = mView.findViewById(R.id.view);

        iv_goleft.setOnClickListener(this);
        iv_goright.setOnClickListener(this);
        iv_scan.setOnClickListener(this);

        refreshRedPoint();
        //set data
        tv_pager.setText((position+1)+"/9");
        tv_englishName.setText("/ "+imageRes.englishName);
        tv_cnName.setText(imageRes.name);
        iv_true.setImageResource(imageRes.res_true);
        if (imageRes.isLight){//have unlocked
            lin_lock.setVisibility(View.INVISIBLE);
            iv_false.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            tv_description.setVisibility(View.VISIBLE);
            tv_description.setText(imageRes.description);
            iv_scan.setVisibility(View.GONE);
        }else{
            lin_lock.setVisibility(View.VISIBLE);
            iv_false.setVisibility(View.VISIBLE);
            tv_description.setVisibility(View.GONE);
            iv_scan.setVisibility(View.VISIBLE);
            iv_true.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (iv_true.getMeasuredWidth()!=0&&!flag){
                        Fglass.blur(iv_true,iv_false,12,8);
                        flag = true;
                    }
                }
            });
        }
    }
}
