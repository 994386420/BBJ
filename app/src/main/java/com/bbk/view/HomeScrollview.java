package com.bbk.view;

import com.andview.refreshview.XScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class HomeScrollview extends XScrollView {  
    
	private OnScollChangedListener onScollChangedListener = null;
	private float xDistance, yDistance, xLast, yLast;

	public HomeScrollview(Context context) {
	    super(context);
	}

	public HomeScrollview(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	public void setOnScollChangedListener(OnScollChangedListener onScollChangedListener) {
	    this.onScollChangedListener = onScollChangedListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
	    super.onScrollChanged(x, y, oldx, oldy);
	    if (onScollChangedListener != null) {
	        onScollChangedListener.onScrollChanged(this, x, y, oldx, oldy);
	    }
	}

	public interface OnScollChangedListener {

	    void onScrollChanged(HomeScrollview scrollView, int x, int y, int oldx, int oldy);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	    switch (ev.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	        xDistance = yDistance = 0f;
	        xLast = ev.getX();
	        yLast = ev.getY();
	        break;
	    case MotionEvent.ACTION_MOVE:
	        final float curX = ev.getX();
	        final float curY = ev.getY();

	        xDistance += Math.abs(curX - xLast);
	        yDistance += Math.abs(curY - yLast);
	        xLast = curX;
	        yLast = curY;

	        /**
	         * X轴滑动距离大于Y轴滑动距离，也就是用户横向滑动时，返回false，ScrollView不处理这次事件，
	         * 让子控件中的TouchEvent去处理，所以横向滑动的事件交由子控件处理， ScrollView只处理纵向滑动事件
	         */
	        if (xDistance > yDistance) {
	            return false;
	        }
	    }

	    return super.onInterceptTouchEvent(ev);
		}
	}
