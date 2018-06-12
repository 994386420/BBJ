package com.bbk.view;

import com.andview.refreshview.XScrollView;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class SuperScrollView extends XScrollView{
    private OnScrollListener mOnScrollListener;
	public SuperScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SuperScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}


	private OnScrollListener onScrollListener;  
    /** 
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较 
     */  
    private int lastScrollY;  
      

           
//    /**
//     * 设置滚动接口
//     * @param onScrollListener
//     */
//    public void setOnScrollListener(OnScrollListener onScrollListener) {
//        this.onScrollListener = onScrollListener;
//    }
//
  
    /** 
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中 
     */  
    private Handler handler = new Handler() {  
  
        public void handleMessage(android.os.Message msg) {  
            int scrollY = SuperScrollView.this.getScrollY();  
            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息  
            if(lastScrollY != scrollY){  
                lastScrollY = scrollY;  
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
               
            }  
            if(onScrollListener != null){  
                onScrollListener.onScroll(scrollY);  
            }  
              
        };  
  
    };

    /**
     * 设置滚动接口
     *
     * @param listener
     */
    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }

    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener {
        /**
         * MyScrollView滑动的Y方向距离变化时的回调方法
         *
         * @param scrollY
         */
        void onScroll(int scrollY);

    }
    /** 
     * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候， 
     * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候， 
     * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理 
     * MyScrollView滑动的距离 
     */  
    @Override  
    public boolean onTouchEvent(MotionEvent ev) {  
        if(onScrollListener != null){  
            onScrollListener.onScroll(lastScrollY = this.getScrollY());  
        }  
        switch(ev.getAction()){  
        case MotionEvent.ACTION_UP:  
             handler.sendMessageDelayed(handler.obtainMessage(), 5);    
            break;  
        }  
        return super.onTouchEvent(ev);  
    }
    /**
     * 监听ScroView的滑动情况
     *
     * @param l    变化后的X轴位置
     * @param t    变化后的Y轴的位置
     * @param oldl 原先的X轴的位置
     * @param oldt 原先的Y轴的位置
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mOnScrollListener) {
            mOnScrollListener.onScroll(t);
        }

//    /**
//     *
//     * 滚动的回调接口
//     *
//     * @author xiaanming
//     *
//     */
//    public interface OnScrollListener{
//        /**
//         * 回调方法， 返回MyScrollView滑动的Y方向距离
//         * @param scrollY
//         *              、
//         */
//        public void onScroll(int scrollY);
//    }
    }
}
