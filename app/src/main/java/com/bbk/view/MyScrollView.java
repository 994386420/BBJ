package com.bbk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
	private ScrollViewListener scrollViewListener = null;
	private float mLastY;
	private int mTouchSlop;
	
	public MyScrollView(Context context) {
		super(context);
	}
	
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}
	
	public void setOnScrollViewListener(ScrollViewListener scrollViewListene) {
		this.scrollViewListener = scrollViewListene;
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}
	
	public interface ScrollViewListener {
		void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (android.os.Build.VERSION.SDK_INT > 20) {
			int action = ev.getAction();
			float y = ev.getY();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				mLastY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				float dy = y - mLastY;
				if(Math.abs(dy) > mTouchSlop) {
					return true;
				}
				break;
			default:
				break;
			}
		}
		
		return super.onInterceptTouchEvent(ev);
	}
	
}
