package com.bbk.view;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.andview.refreshview.callback.IFooterCallBack;
import com.bbk.activity.R;
import com.bbk.util.BaseTools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class MyFootView extends LinearLayout implements IFooterCallBack {
	private View mLayout;
	private TextView mHintView;
	private boolean isShow = true;

	public MyFootView(Context context) {
		super(context);
		initView(context);
	}

	public MyFootView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		// TODO Auto-generated constructor stub
	}

	private void initView(Context context) {
		mLayout = LayoutInflater.from(context).inflate(R.layout.view_footer2, null);
		mLayout.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		mHintView = (TextView) mLayout.findViewById(R.id.footer_hint_textview);
		addView(mLayout);
		hide();
		// mHintView.setVisibility(View.GONE);
	}

	/**
	 * hide footer
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mLayout.getLayoutParams();
		lp.height = 0;
		mLayout.setLayoutParams(lp);
	}

	@Override
	public void callWhenNotAutoLoadMore(XRefreshView xRefreshView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStateReady() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStateRefreshing() {
		mHintView = (TextView) mLayout.findViewById(R.id.footer_hint_textview);
		mHintView.setText("加载中...");
		LinearLayout.LayoutParams rp = (LinearLayout.LayoutParams) mLayout.getLayoutParams();
		rp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		mLayout.setLayoutParams(rp);
	}

	@Override
	public void onReleaseToLoadMore() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStateFinish(boolean hidefooter) {
		mHintView = (TextView) mLayout.findViewById(R.id.footer_hint_textview);
		mHintView.setText("加载完成");
		LinearLayout.LayoutParams rp = (LinearLayout.LayoutParams) mLayout.getLayoutParams();
		rp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		mLayout.setLayoutParams(rp);
	}

	@Override
	public void onStateComplete() {
		mHintView = (TextView) mLayout.findViewById(R.id.footer_hint_textview);
		mHintView.setText("加载完成");
		LinearLayout.LayoutParams rp = (LinearLayout.LayoutParams) mLayout.getLayoutParams();
		rp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		mLayout.setLayoutParams(rp);
	}

	@Override
	public void show(boolean show) {
		if (show == isShow) {
			return;
		}
		isShow = show;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mLayout.getLayoutParams();
		lp.height = show ? LayoutParams.WRAP_CONTENT : 0;
		mLayout.setLayoutParams(lp);
	}

	@Override
	public int getFooterHeight() {
		return BaseTools.getPixelsFromDp(getContext(), 40);
	}

	@Override
	public boolean isShowing() {
		// TODO Auto-generated method stub
		return isShow;
	}

}
