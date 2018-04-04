package com.bbk.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bbk.activity.R;

public class DialogUtil {

//	private Context mContext;
//	private int mId;
//
//	public DialogUtil(Context context, int id) {
//		this.mContext = context;
//		this.mId = id;
//
//		//		this.buildDialog();
//	}
	private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

	public static AlertDialog buildDialog(Context context, int id){

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
		
//		Glide
//		.with(context)
//		.load(R.drawable.loading)
//		.diskCacheStrategy(DiskCacheStrategy.NONE)
//		.signature(new StringSignature(DateUtil.getDate()))
//		.dontAnimate()
//		.into(imageView);

		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		
		Window window = dialog.getWindow();
//		window.setLayout(DensityUtil.dip2px(context, 90), DensityUtil.dip2px(context, 120));
//		window.setGravity(Gravity.CENTER);
//		WindowManager.LayoutParams lp = window.getAttributes();
//		lp.alpha = 0.9f;
//		window.setAttributes(lp);
	    window.setWindowAnimations(R.style.dialog_style); 

//	    WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
//        lp.alpha = 1f;
//        ((Activity)mContext).getWindow().setAttributes(lp);
	    
		return dialog;
	}
	
	public static void show(AlertDialog dialog) {
		if(dialog != null && !dialog.isShowing()) {
			dialog.show();
		}
	}
	
	public static void dismiss(final AlertDialog dialog, int time) {
		if(dialog != null && dialog.isShowing()) {
			Handler handler=new Handler();				
			handler.postDelayed(new Runnable(){
				@Override
				public void run(){
					dialog.dismiss();
					dialog.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
						}
					});
				}  
			}, time);
		}
	}
	
}
