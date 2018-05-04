package com.bbk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;


public abstract class BaseViewPagerFragment extends Fragment {
	private boolean isVisible;

	//Fragment的View加载完毕的标记
	private boolean isViewCreated;

	//Fragment对用户可见的标记
	private boolean isUIVisible;

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		isViewCreated = true;
		lazyLoad();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		//isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
		if (isVisibleToUser) {
			isUIVisible = true;
			lazyLoad();
		} else {
			isUIVisible = false;
		}
	}
	private void lazyLoad() {
		//这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
		if (isViewCreated && isUIVisible) {
			loadLazyData();
			//数据加载完毕,恢复标记,防止重复加载
			isViewCreated = false;
			isUIVisible = false;
		}
	}

	public void onVisible() {
		lazyLoad();
	}
	
	public void OnInvisible() {
	}
	protected abstract void loadLazyData();
//	public abstract void lazyLoad();
}
