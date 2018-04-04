package com.bbk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.activity.R;

public class FooterView extends RelativeLayout {

	public final static int STATE_NORMAL = 0;
	public final static int STATE_LOADING = 1;
	public final static int STATE_OVER = 2;
	public final static int STATE_NOTFOUND = 3;

	private View mLayout;
	private TextView mHintView;
	private int mState = STATE_NORMAL;

	public FooterView(Context context) {
		super(context);
		initView(context);
	}

	public FooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		mLayout = LayoutInflater.from(context).inflate(R.layout.view_footer, null);
		mLayout.setLayoutParams(new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		mHintView = (TextView) mLayout.findViewById(R.id.footer_hint_textview);
		addView(mLayout);
		hide();
	}

	/**
	 * Set footer view state
	 * 
	 * @see #STATE_LOADING
	 * @see #STATE_NORMAL
	 * @see #STATE_OVER
	 * 
	 * @param state
	 */
	public void setState(int state) {
		if (state == mState)
			return;

		switch (state) {
		case STATE_NORMAL:
			mHintView.setText("");
			hide();
			break;
		case STATE_LOADING:
			mHintView.setText("加载中...");
			show();
			break;
		case STATE_OVER:
			mHintView.setText("没有更多了！");
			show();
			break;
		case STATE_NOTFOUND:
			mHintView.setText("没有找到相关数据！");
			show();
			break;
		}

		mState = state;
	}
	
	/**
     * hide footer
     */
    public void hide() {
        RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) mLayout.getLayoutParams();
        rp.height = 0;
        mLayout.setLayoutParams(rp);
    }

    /**
     * show footer
     */
    public void show() {
    	RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) mLayout.getLayoutParams();
    	rp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
        mLayout.setLayoutParams(rp);
    }
}
