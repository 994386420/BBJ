package com.bbk.dialog;

import java.util.ArrayList;
import java.util.List;

import com.bbk.activity.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class ActionSheetDialog {
	private Context context;
	private Dialog dialog;
	private TextView txt_title;
	private TextView txt_cancel;
	private LinearLayout lLayout_content;
	private ScrollView sLayout_content;
	private boolean showTitle = false;
	private List<SheetItem> sheetItemList;
	private Display display;

	public ActionSheetDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public ActionSheetDialog builder() {
		// ��ȡDialog����
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_actionsheet, null);

		// ����Dialog��С���Ϊ��Ļ���
		view.setMinimumWidth(display.getWidth());

		// ��ȡ�Զ���Dialog�����еĿؼ�
		sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
		lLayout_content = (LinearLayout) view
				.findViewById(R.id.lLayout_content);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
		txt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// ����Dialog���ֺͲ���
		dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);

		return this;
	}

	public ActionSheetDialog setTitle(String title) {
		showTitle = true;
		txt_title.setVisibility(View.VISIBLE);
		txt_title.setText(title);
		return this;
	}

	public ActionSheetDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
		dialog.setCanceledOnTouchOutside(cancel);
		return this;
	}

	/**
	 * 
	 * @param strItem
	 *            ��Ŀ����
	 * @param color
	 *            ��Ŀ������ɫ������null��Ĭ����ɫ
	 * @param listener
	 * @return
	 */
	public ActionSheetDialog addSheetItem(int size,String strItem, SheetItemColor color,
			OnSheetItemClickListener listener) {
		if (sheetItemList == null) {
			sheetItemList = new ArrayList<SheetItem>();
		}
		sheetItemList.add(new SheetItem(size,strItem, color, listener));
		return this;
	}

	/** ������Ŀ���� */
	private void setSheetItems() {
		if (sheetItemList == null || sheetItemList.size() <= 0) {
			return;
		}

		int size = sheetItemList.size();

		// TODO �߶ȿ��ƣ�����ѽ���취
		// �����Ŀ�����ʱ����Ƹ߶�
		if (size >= 7) {
			LinearLayout.LayoutParams params = (LayoutParams) sLayout_content
					.getLayoutParams();
			params.height = display.getHeight() / 2;
			sLayout_content.setLayoutParams(params);
		}

		// ѭ�������Ŀ
		for (int i = 1; i <= size; i++) {
			final int index = i;
			SheetItem sheetItem = sheetItemList.get(i - 1);
			String strItem = sheetItem.name;
			SheetItemColor color = sheetItem.color;
			final OnSheetItemClickListener listener = (OnSheetItemClickListener) sheetItem.itemClickListener;

			TextView textView = new TextView(context);
			textView.setText(strItem);
			textView.setTextSize(sheetItem.size);
			textView.setGravity(Gravity.CENTER);

			// ����ͼƬ
			if (size == 1) {
				if (showTitle) {
					textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
				} else {
					textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
				}
			} else {
				if (showTitle) {
					if (i >= 1 && i < size) {
						textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				} else {
					if (i == 1) {
						textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
					} else if (i < size) {
						textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				}
			}

			// ������ɫ
			if (color == null) {
				textView.setTextColor(Color.parseColor(SheetItemColor.Blue
						.getName()));
			} else {
				textView.setTextColor(Color.parseColor(color.getName()));
			}

			// �߶�
			float scale = context.getResources().getDisplayMetrics().density;
			int height = (int) (45 * scale + 0.5f);
			textView.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, height));

			// ����¼�
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onClick(index);
					dialog.dismiss();
				}
			});

			lLayout_content.addView(textView);
		}
	}

	public void show() {
		setSheetItems();
		dialog.show();
	}

	public interface OnSheetItemClickListener {
		void onClick(int which);
	}

	public class SheetItem {
		int size;
		String name;
		OnSheetItemClickListener itemClickListener;
		SheetItemColor color;

		public SheetItem(int size,String name, SheetItemColor color,
				OnSheetItemClickListener itemClickListener) {
			this.size = size;
			this.name = name;
			this.color = color;

			this.itemClickListener = itemClickListener;
		}
	}

	public enum SheetItemColor {
		Blue("#037BFF"), Red("#FD4A2E"),Gray("#B6B6B6");

		private String name;

		private SheetItemColor(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
