package com.bbk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class AssignScrollView extends ScrollView {
	/**
	 * 是否滚动，不滚动就交给子控件滚动
	 */
	private volatile boolean scroll = true;
	
	public AssignScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setEnableScroll(boolean scroll) {
		this.scroll = scroll;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return scroll;
	}
}