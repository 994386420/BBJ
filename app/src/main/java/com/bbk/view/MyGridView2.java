package com.bbk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyGridView2 extends GridView{
	boolean expanded = true;

    public boolean isExpanded() {
        return expanded;
    }

    public MyGridView2(Context context) {
        super(context);
    }

    public MyGridView2(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MyGridView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);  
        
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
