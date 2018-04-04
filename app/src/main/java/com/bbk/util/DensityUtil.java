package com.bbk.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil {
    /** 
     * 将px值转换为dip或dp值，保证尺寸大小不变 
     *  
     * @param pxValue 
     * @param scale 
     *            （DisplayMetrics类中属性density） 
     * @return 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
  
    /** 
     * 将dip或dp值转换为px值，保证尺寸大小不变 
     *  
     * @param dipValue 
     * @param scale 
     *            （DisplayMetrics类中属性density） 
     * @return 
     */  
    public static int dip2px(Context context, float dipValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dipValue * scale + 0.5f);  
    }  
  
    /** 
     * 将px值转换为sp值，保证文字大小不变 
     *  
     * @param pxValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }  
  
    /** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }  
    
    
    public static int getStatusBarHeight(Context context){
    	Class<?> c = null;
    	Object obj = null;
    	Field field = null;
    	int x = 0, statusBarHeight = 0;
    	try {
    		c = Class.forName("com.android.internal.R$dimen");
    		obj = c.newInstance();
    		field = c.getField("status_bar_height");
    		x = Integer.parseInt(field.get(obj).toString());
    		statusBarHeight = context.getResources().getDimensionPixelSize(x);
    	} catch (Exception e1) {
    		e1.printStackTrace();
    	}       
    	return statusBarHeight;
    }
    /**
	 * get screen height of this cellphone
	 * 
	 * @param context
	 * @return
	 */
	public static int getMobileHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels; // 得到高度
		return height;
	}

	/**
	 * get screen width of this cellphone
	 * 
	 * @param context
	 * @return
	 */
	public static int getMobileWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels; // 得到宽度
		return width;

	}
	/*public int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}*/
}
