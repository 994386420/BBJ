package com.bbk.dialog;


import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class HomeAlertDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView txt_title;
	private TextView txt_msg,txt_msg2,txt_msg3,txt_msg4;
	private TextView btn_neg;
	private TextView btn_pos;
	private ImageView img_line;
	private Display display;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showMsg2 = false;
	private boolean showMsg3 = false;
	private boolean showMsg4 = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;
	private LinearLayout mclose;
	private ImageView mimg;

	public HomeAlertDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public HomeAlertDialog builder() {
		// ��ȡDialog����
		View view = LayoutInflater.from(context).inflate(
				R.layout.home_alert_dialog, null);
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		mimg = (ImageView) view.findViewById(R.id.mimg);
		mclose = (LinearLayout) view.findViewById(R.id.mclose);
		mclose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		// ����Dialog���ֺͲ���
		dialog = new Dialog(context, R.style.AlertDialogStyle2);
		dialog.setContentView(view);

		// ����dialog������С
		android.view.ViewGroup.LayoutParams params = lLayout_bg.getLayoutParams();
		params.width = (int) (display.getWidth() * 0.7);
		params.height = (int) (display.getWidth()* 0.7*1020/846);
		lLayout_bg.setLayoutParams(params);
		android.view.ViewGroup.LayoutParams params2 = mimg.getLayoutParams();
		params2.height = (int) (display.getWidth()* 0.7*1020/846);
		params2.width = (int) (display.getWidth() * 0.7);
		mimg.setLayoutParams(params2);
		return this;
	}
	public HomeAlertDialog setimag(String url){
		Glide.with(context).load(url).skipMemoryCache(true).priority(Priority.HIGH).into(mimg);
		return this;
	}

	public HomeAlertDialog setonclick(
			final OnClickListener listener) {
		lLayout_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public void show() {
		dialog.show();
	}
}
