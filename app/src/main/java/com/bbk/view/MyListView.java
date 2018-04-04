package com.bbk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ListView;

public class MyListView extends ListView{
	boolean expanded = true;  
	  
    public boolean isExpanded() {  
        return expanded;  
    }  
  
    public MyListView(Context context) {  
        super(context);  
    }  
  
    public MyListView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public MyListView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        //测量
//        int height = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, height);
//    }
    @Override  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        if (true) {  
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
            super.onMeasure(widthMeasureSpec, expandSpec);  
            ViewGroup.LayoutParams params = getLayoutParams();  
            params.height = getMeasuredHeight();  
        } else {  
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
        }  
    }  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {  
            return true; // 禁止GridView滑动  
        }  
        return super.dispatchTouchEvent(ev);  
      
    }  
    public void setExpanded(boolean expanded) {  
        this.expanded = expanded;  
    } 
}
