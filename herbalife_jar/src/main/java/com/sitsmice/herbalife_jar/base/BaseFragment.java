package com.sitsmice.herbalife_jar.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitsmice.herbalife_jar.IDEventHttpManager;
import com.sitsmice.idevevt_jar.R;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;

public abstract class BaseFragment extends BackHandledFragment implements View.OnClickListener,IBaseUI{

	protected TextView title_icon,title_icont,title_text,title_menu,title_menut;
	protected AutoFrameLayout auto_frameL;
	protected AutoLinearLayout auto_menu,auto_icon;
	protected ImageView iv_redPoint;
	protected View mView;
	protected int r;

	protected BaseUI baseUI;

    protected IDEventHttpManager mHttpManager;
	
	public BaseFragment(){
		
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mHttpManager = JarApplication.getInstance().getHttpManger();
		baseUI = new BaseUI(getActivity());
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (mView==null) {
			mView = inflater.inflate(r, null);
			onCreateView();
		}else {
			ViewGroup viewGroup = (ViewGroup) mView.getParent();
			if (viewGroup!=null) {
				viewGroup.removeView(mView);
			}
		}
		return mView;
	}
	
	protected abstract void onCreateView();

	protected void initTitle(){
		auto_frameL = (AutoFrameLayout) mView.findViewById(R.id.auto_frameL);
		auto_icon = (AutoLinearLayout) mView.findViewById(R.id.auto_icon);
		auto_menu = (AutoLinearLayout) mView.findViewById(R.id.auto_menu);
		title_icon = (TextView) mView.findViewById(R.id.title_icon);
		title_icont = (TextView) mView.findViewById(R.id.title_icont);
		title_text = (TextView) mView.findViewById(R.id.title_text);
		title_menu = (TextView) mView.findViewById(R.id.title_menu);
		title_menut = (TextView) mView.findViewById(R.id.title_menut);
		iv_redPoint = (ImageView) mView.findViewById(R.id.iv_redPoint);
		auto_icon.setOnClickListener(this);
		auto_menu.setOnClickListener(this);
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
	public void showToast(Object msg) {
		baseUI.showToast(msg);
	}

	@Override
	public void toActivity(Class<?> c) {
		baseUI.toActivity(c);
	}

	@Override
	public void onClick(View v) {
		if (v == auto_icon){
			getActivity().finish();
		}
	}
}
