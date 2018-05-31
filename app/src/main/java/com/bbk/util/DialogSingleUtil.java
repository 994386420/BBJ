package com.bbk.util;

import com.bbk.activity.R;
import com.bbk.view.IosLoadingView;
import com.zyao89.view.zloading.ZLoadingView;
import com.zyao89.view.zloading.Z_TYPE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DialogSingleUtil {

	private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
	private static AlertDialog alertDialog = null;
	private static int showCount = 0;
	public static void show(Context context, int id,String tips){
		showCount++;
		if(alertDialog == null || !alertDialog.isShowing()){
			AlertDialog.Builder builder = new Builder(context, R.style.dialog);
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(id, null);
			builder.setView(view,0,0,0,0);

			ImageView imageView = (ImageView) view.findViewById(R.id.dialog_img);

			final RotateAnimation animation =new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setDuration(1000);
			animation.setRepeatCount(Animation.INFINITE);
			animation.setRepeatMode(Animation.RESTART);
			animation.setInterpolator(LINEAR_INTERPOLATOR);
			imageView.setAnimation(animation);
			animation.startNow();
//			imageView.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE,0.5);
			alertDialog = builder.create();
			alertDialog.setCanceledOnTouchOutside(false);
			
			if(!TextUtils.isEmpty(tips))
				((TextView)view.findViewById(R.id.tips_tv)).setText(tips);
			
			Window window = alertDialog.getWindow();
			WindowManager.LayoutParams params = window.getAttributes();
			params.gravity = Gravity.CENTER;
			//设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
			params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			window.setAttributes(params);
		    window.setWindowAnimations(R.style.dialog_style);
		    alertDialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					showCount = 0;
				}
			});
			alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//全局弹窗
		    alertDialog.show();
		}
	}
	public static void show(Context context,String tips){
		show(context, R.layout.loading_dialog,tips);
	}
	public static void show(Context context){
		show(context, R.layout.loading_dialog,null);
	}
	
	public  static void dismiss(final int time) {
		showCount--;
		if(showCount>0){
			return;
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(time);
					if(alertDialog != null && alertDialog.isShowing()) {
						alertDialog.dismiss();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}
	
}
