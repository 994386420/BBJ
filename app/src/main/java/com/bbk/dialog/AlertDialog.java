package com.bbk.dialog;


import com.bbk.activity.R;

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

public class AlertDialog {
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

	public AlertDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public AlertDialog builder() {
		// ��ȡDialog����
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_alertdialog, null);

		// ��ȡ�Զ���Dialog�����еĿؼ�
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_title.setVisibility(View.GONE);
		txt_msg = (TextView) view.findViewById(R.id.txt_msg);
		txt_msg.setVisibility(View.GONE);
		txt_msg2 = (TextView) view.findViewById(R.id.txt_msg2);
		txt_msg2.setVisibility(View.GONE);
		txt_msg3 = (TextView) view.findViewById(R.id.txt_msg3);
		txt_msg3.setVisibility(View.GONE);
		txt_msg4 = (TextView) view.findViewById(R.id.txt_msg4);
		txt_msg4.setVisibility(View.GONE);
		btn_neg = (TextView) view.findViewById(R.id.btn_neg);
		btn_neg.setVisibility(View.GONE);
		btn_pos = (TextView) view.findViewById(R.id.btn_pos);
		btn_pos.setVisibility(View.GONE);
		img_line = (ImageView) view.findViewById(R.id.img_line);
		img_line.setVisibility(View.GONE);

		// ����Dialog���ֺͲ���
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		// ����dialog������С
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//全局弹窗
		return this;
	}

	public AlertDialog setTitle(String title) {
		showTitle = true;
		if ("".equals(title)) {
			txt_title.setText("");
		} else {
			txt_title.setText(title);
		}
		return this;
	}
	public AlertDialog setTitleColor(String color) {
		if (!"".equals(color)) {
			txt_title.setTextColor(Color.parseColor(color));
		}
		return this;
	}

	public AlertDialog setMsg(String msg) {
		showMsg = true;
		if ("".equals(msg)) {
			txt_msg.setText("����");
		} else {
			txt_msg.setText(msg);
		}
		return this;
	}
	public AlertDialog setMsg2(String msg) {
		showMsg2 = true;
		if ("".equals(msg)) {
			txt_msg2.setText("����");
		} else {
			txt_msg2.setText(msg);
		}
		return this;
	}
	public AlertDialog setMsg3(String msg) {
		showMsg3 = true;
		if ("".equals(msg)) {
			txt_msg3.setText("����");
		} else {
			txt_msg3.setText(msg);
		}
		return this;
	}
	public AlertDialog setMsg4(String msg) {
		showMsg4 = true;
		if ("".equals(msg)) {
			txt_msg4.setText("����");
		} else {
			txt_msg4.setText(msg);
		}
		return this;
	}

	public AlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}
	public AlertDialog setLeft() {
		txt_msg.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		txt_msg2.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		txt_msg3.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		txt_msg4.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		return this;
	}

	public AlertDialog setPositiveButton(String text,
			final OnClickListener listener) {
		showPosBtn = true;
		if ("".equals(text)) {
			btn_pos.setText("ȷ��");
		} else {
			btn_pos.setText(text);
		}
		btn_pos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}
	public AlertDialog setPositiveButtonColor(String color) {
		if (!"".equals(color)) {
			btn_pos.setTextColor(Color.parseColor(color));

		}
		return this;
	}
	public AlertDialog setPositiveBackgroundColor(String color) {
		if (!"".equals(color)) {
			btn_pos.setBackgroundResource(R.drawable.bg_alertdialog_btn);
		}
		return this;
	}

	public AlertDialog setNegativeButton(String text,
			final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			btn_neg.setText("ȡ��");
		} else {
			btn_neg.setText(text);
		}
		btn_neg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}
	public AlertDialog setNegativeButtonColor(String color) {
		if (!"".equals(color)) {
			btn_neg.setTextColor(Color.parseColor(color));

		}
		return this;
	}
	private void setLayout() {
		if (!showTitle && !showMsg) {
			txt_title.setText("��ʾ");
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showTitle) {
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showMsg) {
			txt_msg.setVisibility(View.VISIBLE);
		}
		if (showMsg2) {
			txt_msg2.setVisibility(View.VISIBLE);
		}
		if (showMsg3) {
			txt_msg3.setVisibility(View.VISIBLE);
		}
		if (showMsg4) {
			txt_msg4.setVisibility(View.VISIBLE);
		}
		if (!showPosBtn && !showNegBtn) {
			btn_pos.setText("ȷ��");
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
			btn_pos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}

		if (showPosBtn && showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
//			btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
			btn_neg.setVisibility(View.VISIBLE);
//			btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
			img_line.setVisibility(View.VISIBLE);
		}

		if (showPosBtn && !showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
//			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}

		if (!showPosBtn && showNegBtn) {
			btn_neg.setVisibility(View.VISIBLE);
//			btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}
	}

	public void show() {
		setLayout();
		dialog.show();
	}
}
