package com.bbk.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bbk.activity.R;
import com.bbk.util.BaseTools;
import com.bbk.util.DensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * android banner图
 */
public class RollHeaderView3 extends FrameLayout implements OnPageChangeListener {

	private int height;
    private Context mContext;
    private ViewPager mViewPager;
    private List<Object> mUrlList;

    private MyAdapter mAdapter = null;
    private Handler mHandler = null;
    private AutoRollRunnable mAutoRollRunnable = null;

    private int prePosition = 0;

    private HeaderViewClickListener headerViewClickListener;

    public RollHeaderView3(Context context) {
        this(context, null,0,-1,true);
    }
    public RollHeaderView3(Context context,float height,boolean isScale) {
        this(context, null,0,height,isScale);
    }

    public RollHeaderView3(Context context, AttributeSet attrs, int defStyle,float height,boolean isScale) {
        super(context, attrs, defStyle);
        if(isScale){
        	if(height == -1){
        		//如果没有设置banner高度就设置高度为屏幕高度的1/5
        		height = 0.22f;
        	}
        	this.height = (int) (DensityUtil.getMobileHeight(context) * height);
        }else{
        	this.height = DensityUtil.dip2px(context, height);
        }
        this.mContext = context;
        initView();
        initData();
        initListener();
    }

    //初始化view
    private void initView() {
        View.inflate(mContext, R.layout.roll_header_view3, this);
        mViewPager = (ViewPager) findViewById(R.id.vp);

        ViewGroup.LayoutParams vParams = mViewPager.getLayoutParams();
        
        vParams.height = height;
        mViewPager.setLayoutParams(vParams);
        // 设置页与页之间的间距
        mViewPager.setPageMargin(10);
        
    }

    //初始化数据
    private void initData() {
        mAutoRollRunnable = new AutoRollRunnable();
        mHandler = new Handler();
        mAdapter = new MyAdapter();
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(this);
    }


    /**
     * 设置数据
     *
     * @param urlList
     */
    public void setImgUrlData(List<Object> urlList) {
        this.mUrlList = urlList;
        mAdapter = new MyAdapter();
        mViewPager.setAdapter(mAdapter);

        //设置viewpager初始位置, +10000就够了
        mViewPager.setCurrentItem(urlList.size() + 10000);
        mViewPager.setOffscreenPageLimit(urlList.size()-1);
        startRoll();
    }


    /**
     * 设置点击事件
     *
     * @param headerViewClickListener
     */
    public void setOnHeaderViewClickListener(HeaderViewClickListener headerViewClickListener) {
        this.headerViewClickListener = headerViewClickListener;
    }


    //开始轮播
    public void startRoll() {
        mAutoRollRunnable.start();
    }

    // 停止轮播
    public void stopRoll() {
        mAutoRollRunnable.stop();
    }

    private class AutoRollRunnable implements Runnable {

        //是否在轮播的标志
        boolean isRunning = false;

        public void start() {
            if (!isRunning) {
                isRunning = true;
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this, 3000);
            }
        }

        public void stop() {
            if (isRunning) {
                mHandler.removeCallbacks(this);
                isRunning = false;
            }
        }

        @Override
        public void run() {
            if (isRunning) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                mHandler.postDelayed(this, 3000);
            }
        }
    }

    public interface HeaderViewClickListener {
        void HeaderViewClick(int position);
    }

    private class MyAdapter extends PagerAdapter {

        //为了复用
        private List<ImageView> imgCache = new ArrayList<ImageView>();

        @Override
        public int getCount() {
            //无限滑动
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        // 滑动距离及坐标 归还父控件焦点  
        private float xDistance, yDistance, xLast, yLast,mLeft; 
		@Override
        public Object instantiateItem(ViewGroup container, final int position) {

            ImageView iv;

            //获取ImageView对象
            if (imgCache.size() > 0) {
                iv = imgCache.remove(0);
            } else {
                iv = new ImageView(mContext);
            }
            iv.setScaleType(ScaleType.CENTER_CROP);
            iv.setOnTouchListener(new OnTouchListener() {
                private int downX = 0;
                private long downTime = 0;

                @SuppressLint("ClickableViewAccessibility")
				@Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mAutoRollRunnable.stop();
                            //获取按下的x坐标
                            downX = (int) event.getX();
                            downTime = System.currentTimeMillis();
                            xDistance = yDistance = 0f;  
                            xLast = event.getX();  
                            yLast = event.getY();  
                            mLeft = event.getX();
                            break;
                        case MotionEvent.ACTION_UP:
                        	final float curX = event.getX();  
                            final float curY = event.getY();  
              
                            xDistance += Math.abs(curX - xLast);  
                            yDistance += Math.abs(curY - yLast);  
                            xLast = curX;  
                            yLast = curY;  
                            if (mLeft<100||xDistance < yDistance) {  
                                getParent().requestDisallowInterceptTouchEvent(false);  
                            } else {  
                                getParent().requestDisallowInterceptTouchEvent(true);  
                            }  
                            mAutoRollRunnable.start();
                            int moveX = (int) event.getX();
                            long moveTime = System.currentTimeMillis();
                            if (downX == moveX && (moveTime - downTime < 500)) {//点击的条件
                                //轮播图回调点击事件
                                headerViewClickListener.HeaderViewClick(position % mUrlList.size());
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            mAutoRollRunnable.start();
                            break;
                    }
                    return true;
                }
            });
           
				//加载图片
	            Glide.with(mContext)
	    		.load(mUrlList.get(position % mUrlList.size()))
	            .into(iv);
            

            ((ViewPager) container).addView(iv);

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object != null && object instanceof ImageView) {
                ImageView iv = (ImageView) object;
                ((ViewPager) container).removeView(iv);
                imgCache.add(iv);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
    	
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }


    //停止轮播
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRoll();
    }
}
