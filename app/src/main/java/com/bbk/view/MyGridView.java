package com.bbk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.GridView;

public class MyGridView extends GridView{
	boolean expanded = true;  
	  
    public boolean isExpanded() {  
        return expanded;  
    }  
  
    public MyGridView(Context context) {  
        super(context);  
    }  
  
    public MyGridView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        
    }  
  
    public MyGridView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        
    }  
  
    @Override  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
    	int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec+50); 
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
