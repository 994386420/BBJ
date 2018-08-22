package com.bbk.shopcar;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import java.lang.reflect.Field;

/**
 *
 * @describe  自定义behavior  以解决滑动抖动
 *
 */

public class CustomBehavior extends  AppBarLayout.Behavior {
    private OverScroller mScroller;
    public CustomBehavior() {
    }

    public CustomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        getParentScroller(context);
    }

    /**
     * 反射获得滑动属性。
     *
     * @param context
     */
    private void getParentScroller(Context context) {
        if (mScroller != null) return;
        mScroller = new OverScroller(context);
        try {
            Class<?> reflex_class = getClass().getSuperclass().getSuperclass();//父类AppBarLayout.Behavior  父类的父类   HeaderBehavior
            Field fieldScroller = reflex_class.getDeclaredField("mScroller");
            fieldScroller.setAccessible(true);
            fieldScroller.set(this, mScroller);
        } catch (Exception e) {}
    }
    //fling上滑appbar然后迅速fling下滑recycler时, HeaderBehavior的mScroller并未停止, 会导致上下来回晃动
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if(mScroller!=null){ //当recyclerView 做好滑动准备的时候 直接干掉Appbar的滑动
            if (mScroller.computeScrollOffset()) {
                mScroller.abortAnimation();
            }
        }

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent e) {

        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
        }



        return super.onTouchEvent(parent,child,e);
    }
}