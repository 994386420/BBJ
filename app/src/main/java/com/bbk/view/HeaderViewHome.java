package com.bbk.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.andview.refreshview.callback.IHeaderCallBack;
import com.bbk.activity.R;
import com.zyao89.view.zloading.ZLoadingView;
import com.zyao89.view.zloading.Z_TYPE;

/**
 * Created by Administrator on 2018/5/03/003.
 */

public class HeaderViewHome extends LinearLayout implements IHeaderCallBack {
    private ZLoadingView bar1,bar;
    private TextView downTextView;
    private TextView refresh;
    private ImageView mfreshImage;

    public HeaderViewHome(Context context) {
        this(context, null);
    }

    public HeaderViewHome(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderViewHome(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderViewHome(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private Scroller mScroller;

    private void init(Context context) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.refresh_top_item, this);
        //刷新bar
        bar = (ZLoadingView) refreshView.findViewById(R.id.progress);
        bar1 = (ZLoadingView) refreshView.findViewById(R.id.progress1);
        //下拉显示text
        downTextView = (TextView) refreshView.findViewById(R.id.refresh_hint);
        refresh = (TextView) refreshView.findViewById(R.id.refresh);
        bar.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE,0.5);
        downTextView.setTextColor(context.getResources().getColor(R.color.black));
        refresh.setTextColor(context.getResources().getColor(R.color.black));
        refreshView.findViewById(R.id.fresh_image).setVisibility(VISIBLE);
        mScroller = new Scroller(getContext(), new LinearInterpolator());
    }

    @Override
    public void onStateNormal() {
        bar.setVisibility(View.GONE);
        downTextView.setText("下拉更新...");
    }

    @Override
    public void onStateReady() {
        bar.setVisibility(View.GONE);
        downTextView.setText("松手更新...");
    }

    @Override
    public void onStateRefreshing() {
        bar.setVisibility(View.VISIBLE);
        refresh.setVisibility(VISIBLE);
        refresh.setText("更新中...");
        downTextView.setVisibility(View.GONE);
        invalidate();
    }

    @Override
    public void onStateFinish(boolean success) {
        bar.setVisibility(View.GONE);
        refresh.setVisibility(GONE);
        downTextView.setVisibility(View.VISIBLE);
        downTextView.setText("下拉更新...");
        mScroller.forceFinished(true);
    }


    @Override
    public void onHeaderMove(double headerMovePercent, int offsetY, int deltaY) {
    }

    @Override
    public void setRefreshTime(long lastRefreshTime) {

    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    @Override
    public int getHeaderHeight() {
        return getMeasuredHeight();
    }
}
