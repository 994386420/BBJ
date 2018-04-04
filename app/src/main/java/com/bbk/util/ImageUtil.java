package com.bbk.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ImageUtil {
	/**  
	 * 按照屏幕宽度适当压缩图片数据
	 * @param context  
	 * @param resId  
	 * @return  
	 */  
	public static Bitmap readBitMap(Context context, int resId){
		/** 
		 * 第一轮解释，只为获取图片宽度
		 */  
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小  
		final BitmapFactory.Options options = new BitmapFactory.Options();  
		options.inJustDecodeBounds = true;  
		InputStream is = context.getResources().openRawResource(resId);   
		BitmapFactory.decodeStream(is,null,options); 
		// 源图片的宽度  
		int width = options.outWidth; 
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 获取设备屏幕宽度
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(outMetrics);
		int dWidth = outMetrics.widthPixels;
		
		int inSampleSize = 1; 
		if (width > dWidth) {  
	        // 计算出实际宽度和目标宽度的比率  
	        final int widthRatio = Math.round((float) width / (float) dWidth);  
	        inSampleSize = widthRatio;  
	    }
		// 调用上面定义的方法计算inSampleSize值（inSampleSize值为图片压缩比例）  
        /** 
         * 第二轮解析，负责具体压缩 
         */  
		// 使用获取到的inSampleSize值再次解析图片
		options.inSampleSize =inSampleSize;
		options.inJustDecodeBounds = false; 
		is = context.getResources().openRawResource(resId);   
		return BitmapFactory.decodeStream(is,null,options);
	}  
	/** 
	* Drawable转化为Bitmap 
	*/  
	public static Bitmap drawableToBitmap(Drawable drawable) {  
	   int width = drawable.getIntrinsicWidth();  
	   int height = drawable.getIntrinsicHeight();  
	   Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);  
	   Canvas canvas = new Canvas(bitmap);  
	   drawable.setBounds(0, 0, width, height);  
	   drawable.draw(canvas);  
	   return bitmap;  
	  
	}  
	/**
	 * Bitmap to Drawable
	 * @param bitmap
	 * @param mcontext
	 * @return
	 */
	public static Drawable bitmapToDrawble(Bitmap bitmap,Context mcontext){
		Drawable drawable = new BitmapDrawable(mcontext.getResources(), bitmap);
		return drawable;
	}
}
