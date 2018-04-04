package com.bbk.fragment;

import android.support.v4.app.Fragment;


public abstract class BaseViewPagerFragment extends Fragment {
	private boolean isVisible;
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			OnInvisible();
		}
	}
	
	public void onVisible() {
		lazyLoad();
	}
	
	public void OnInvisible() {
	}
	
	public abstract void lazyLoad();
}
