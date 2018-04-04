package com.bbk.view;

/**
 * Created by Administrator on 2018/3/29/029.
 */

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

    /*
     * ScrollView并没有实现滚动监听，所以我们必须自行实现对ScrollView的监听，
     * 我们很自然的想到在onTouchEvent()方法中实现对滚动Y轴进行监听
     * ScrollView的滚动Y值进行监听
     */
    public class MyNewScrollView extends ScrollView {

        private int flag=0;    //并发控制标志位

        private OnScrollListener mOnScrollListener;

        public MyNewScrollView(Context context) {
            super(context);
        }

        public MyNewScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyNewScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

//        @Override
//        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//
//        }


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
        private OnZdyScrollViewListener onZdyScrollViewListener;
        //listview加载完毕，将并发控制符置为0
        public void loadingComponent(){
            flag=0;
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
            View view=this.getChildAt(0);
            //如果scrollview滑动到底部并且并发控制符为0，回调接口向服务器端请求数据
            if (this.getHeight() + this.getScrollY() == view.getHeight() && flag == 0) {
                flag = 1;//一进来就将并发控制符置为1，虽然onScrollChanged执行多次，但是由于并发控制符的值为1，不满足条件就不会执行到这
                onZdyScrollViewListener.ZdyScrollViewListener();
            }
        }

        public void setOnZdyScrollViewListener(OnZdyScrollViewListener onZdyScrollViewListener){
            this.onZdyScrollViewListener=onZdyScrollViewListener;
        }

        public interface OnZdyScrollViewListener{
            public void ZdyScrollViewListener();
        }

    }
