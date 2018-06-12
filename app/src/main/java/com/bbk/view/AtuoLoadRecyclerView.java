package com.bbk.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2018/5/14/014.
 */

public class AtuoLoadRecyclerView extends RecyclerView {

    private onLoadMoreListener loadMoreListener;    //加载更多回调
    private boolean isLoadingMore;                  //是否加载更多

    public AtuoLoadRecyclerView(Context context) {
        this(context, null);
    }

    public AtuoLoadRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AtuoLoadRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        isLoadingMore = false;  //默认无需加载更多
        setOnScrollListener(new AutoLoadScrollListener(null, true, true));
    }

    /**
     * 配置显示图片，需要设置这几个参数，快速滑动时，暂停图片加载
     *
     * @param imageLoader   ImageLoader实例对象
     * @param pauseOnScroll
     * @param pauseOnFling
     */
    public void setOnPauseListenerParams(Glide imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {

        setOnScrollListener(new AutoLoadScrollListener(imageLoader, pauseOnScroll, pauseOnFling));

    }

    public void setLoadMoreListener(onLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
//
//    @Override
//    public void loadFinish(Object obj) {
//        isLoadingMore = false;
//    }


    //加载更多的回调接口
    public interface onLoadMoreListener {
        void loadMore();
    }


    /**
     * 滑动自动加载监听器
     */
    private class AutoLoadScrollListener extends OnScrollListener {

        private Glide imageLoader;
        private final boolean pauseOnScroll;
        private final boolean pauseOnFling;

        public AutoLoadScrollListener(Glide imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
            super();
            this.pauseOnScroll = pauseOnScroll;
            this.pauseOnFling = pauseOnFling;
            this.imageLoader = imageLoader;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //由于GridLayoutManager是LinearLayoutManager子类，所以也适用
            if (getLayoutManager() instanceof LinearLayoutManager) {
                int lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = AtuoLoadRecyclerView.this.getAdapter().getItemCount();

                //有回调接口，且不是加载状态，且计算后剩下2个item，且处于向下滑动，则自动加载
                if (loadMoreListener != null && !isLoadingMore && lastVisibleItem >= totalItemCount -
                        2 && dy > 0) {
                    loadMoreListener.loadMore();
                    isLoadingMore = true;
                }
            }
        }

        //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；由于用户的操作，屏幕产生惯性滑动时为2
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            //根据newState状态做处理
            if (imageLoader != null) {
                switch (newState) {
                    case 0:
                        Log.v("onScrollStateChanged", "开始滚动：SCROLL_STATE_FLING");
                        Glide.with(getContext()).resumeRequests();
                        break;

                    case 1:
                        if (pauseOnScroll) {
                            Glide.with(getContext()).pauseRequests();
                        } else {
                            Glide.with(getContext()).resumeRequests();
                        }
                        break;

                    case 2:
                        if (pauseOnFling) {
                            Glide.with(getContext()).pauseRequests();
                        } else {
                            Glide.with(getContext()).resumeRequests();
                        }
                        break;
                }
            }
        }
    }
}
