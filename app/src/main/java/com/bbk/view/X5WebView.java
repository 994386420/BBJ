package com.bbk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.widget.ProgressBar;

import com.bbk.activity.R;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class X5WebView extends WebView {
	TextView title;
	private ProgressBar progressbar;
	private boolean is_gone = false;
	private WebViewClient client = new WebViewClient() {
		/**
		 * 防止加载网页时调起系统浏览器
		 */
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	};

	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		this.setWebViewClient(client);
		// this.setWebChromeClient(chromeClient);
		// WebStorage webStorage = WebStorage.getInstance();
		progressbar = new android.widget.ProgressBar(arg0, null,
				android.R.attr.progressBarStyleHorizontal);
		// 设置进度条的大小
		progressbar.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT,
				5, 0, 0));
		// 可以改变颜色
		ClipDrawable d = new ClipDrawable(new ColorDrawable(getResources().getColor(R.color.tuiguang_color5)),
				Gravity.LEFT, ClipDrawable.HORIZONTAL);
		progressbar.setProgressDrawable(d);
		progressbar.setBackgroundColor(getResources().getColor(R.color.transparent));

		// progressbar.setProgressDrawable(context.getResources().getDrawable(
		// R.drawable.barbgimg));

		addView(progressbar);
		initWebViewSettings();
		setWebChromeClient(new WebChromeClient());
		this.getView().setClickable(true);
	}

	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(true);
		// webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		// webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setDisplayZoomControls(false);//不显示缩放按钮
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

		// this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
		// settings 的设计
	}

//	@Override
//	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
//		boolean ret = super.drawChild(canvas, child, drawingTime);
//		canvas.save();
//		Paint paint = new Paint();
//		paint.setColor(0x7fff0000);
//		paint.setTextSize(24.f);
//		paint.setAntiAlias(true);
//		if (getX5WebViewExtension() != null) {
//			canvas.drawText(this.getContext().getPackageName() + "-pid:"
//					+ android.os.Process.myPid(), 10, 50, paint);
//			canvas.drawText(
//					"X5  Core:" + QbSdk.getTbsVersion(this.getContext()), 10,
//					100, paint);
//		} else {
//			canvas.drawText(this.getContext().getPackageName() + "-pid:"
//					+ android.os.Process.myPid(), 10, 50, paint);
//			canvas.drawText("Sys Core", 10, 100, paint);
//		}
//		canvas.drawText(Build.MANUFACTURER, 10, 150, paint);
//		canvas.drawText(Build.MODEL, 10, 200, paint);
//		canvas.restore();
//		return ret;
//	}

	public X5WebView(Context arg0) {
		super(arg0);
		setBackgroundColor(85621);
	}

	public class WebChromeClient extends com.tencent.smtt.sdk.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				progressbar.setVisibility(GONE);
			} else {
				if (progressbar.getVisibility() == GONE)
					progressbar.setVisibility(VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}

	@Override

	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if(visibility == View.GONE) {
			try {
				android.webkit.WebView.class.getMethod("onPause").invoke(this);// stop flash
			} catch(Exception e) {

			}
			this.pauseTimers();
			this.is_gone = true;
		} else if (visibility == View.VISIBLE) {
			try {
				android.webkit.WebView.class.getMethod("onResume").invoke(this);// resume flash
			} catch(Exception e) {
			}
			this.resumeTimers();
			this.is_gone = false;
		}
	}

	@Override

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(this.is_gone) {
			try {
				this.destroy();
			} catch
					(Exception e) {
			}
		}
	}
}
