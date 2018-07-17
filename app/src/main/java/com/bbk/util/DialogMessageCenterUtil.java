package com.bbk.util;

import android.content.Context;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class DialogMessageCenterUtil {

	private static NewAlertDialog dialog = null;

	public static void show(Context context) {
		if (context != null) {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (dialog == null) {
				dialog = NewAlertDialog.create(context, true, null);
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
				dialog = NewAlertDialog.create(context, true, null);
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
