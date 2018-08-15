package com.bbk.util;

import android.content.Context;

public class DialogCheckYouhuiUtil {

	private static CheckYouhuiAlertDialog dialog = null;

	public static void show(Context context) {
		if (context != null) {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (dialog == null) {
				dialog =CheckYouhuiAlertDialog.create(context, true, null,"正在查询优惠信息...");
			}
			dialog.show();
		}

	}
	public static void show(Context context,String tips) {
		if (context != null) {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (dialog == null) {
				dialog = CheckYouhuiAlertDialog.create(context, true, null,tips);
			}
			dialog.show();
		}

	}
	public static void  dismiss(final int time)  {
	   new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(time);
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
						dialog = null;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
