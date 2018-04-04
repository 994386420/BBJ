package com.bbk.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

public class BaseTools {
	
	/**
	 * 获取屏幕宽度 width
	 * @param activity
	 * @return
	 */
	public static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	public static int getWindowsHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	public static int getViewHeight (View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		int height = view.getMeasuredHeight();
//		int width = view.getMeasuredWidth();
		
		return height;
	}
	
	
	/**
	 * dp转换为px
	 * @param activity
	 * @param size
	 * @return
	 */
	public static int getPixelsFromDp(Context context,int size){

		DisplayMetrics metrics =new DisplayMetrics();

		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

		return(size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;

		}
}
