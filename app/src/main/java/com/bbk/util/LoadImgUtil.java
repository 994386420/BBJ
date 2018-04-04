package com.bbk.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;

/**
 * 
 * @author LiangRan
 * @date 2016-03-31
 * 封装图片加载，抽象依赖第三方框架
 */
public class LoadImgUtil {
	
	/**
	 * 
	 * @param context
	 * @param img
	 * @param url
	 */
	public static void loadImg(Context context, ImageView img, String url) {
		Glide.with(context)
		.load(url)
		.diskCacheStrategy(DiskCacheStrategy.NONE)
		.signature(new StringSignature(DateUtil.getDate()))
        .placeholder(R.mipmap.default_img)
        .dontAnimate()
        .into(img);
	}
	
	/**
	 * 
	 * @param context
	 * @param img
	 * @param id
	 */
	public static void loadImg(Context context, ImageView img, int id) {
		Glide.with(context)
		.load(id)
		.diskCacheStrategy(DiskCacheStrategy.NONE)
		.signature(new StringSignature(DateUtil.getDate()))
        .placeholder(R.mipmap.default_img)
        .dontAnimate()
        .into(img);
	}
	
	/**
	 * 
	 * @param context
	 * @param img
	 * @param url
	 * @param w
	 * @param h
	 */
	public static void loadImg(Context context, ImageView img, String url, int w, int h) {
		Glide.with(context)
		.load(url)
		.override(w, h)
		.diskCacheStrategy(DiskCacheStrategy.NONE)
		.signature(new StringSignature(DateUtil.getDate()))
        .placeholder(R.mipmap.default_img)
        .dontAnimate()
        .into(img);
	}
	
	/**
	 * 
	 * @param context
	 * @param img
	 * @param id
	 * @param w
	 * @param h
	 */
	public static void loadImg(Context context, ImageView img, int id, int w, int h) {
		Glide.with(context)
		.load(id)
		.override(w, h)
		.diskCacheStrategy(DiskCacheStrategy.NONE)
		.signature(new StringSignature(DateUtil.getDate()))
        .placeholder(R.mipmap.default_img)
        .dontAnimate()
        .into(img);
	}
	
	/**
	* 从服务器取图片
	*http://bbs.3gstdy.com
	* @param url
	* @return
	*/
	public static Bitmap getHttpBitmap(String url) {
	     URL myFileUrl = null;
	     Bitmap bitmap = null;
	     InputStream is = null;
	     try {
	          myFileUrl = new URL(url);
	          HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
	          conn.setConnectTimeout(4000);
	          conn.setDoInput(true);
	          conn.connect();
	          is = conn.getInputStream();
	          bitmap = BitmapFactory.decodeStream(is);
	     } catch (Exception e) {
	          e.printStackTrace();
	     }finally{
	    	 if(is != null){
	    		 try {
					is.close();
				} catch (IOException e) {
				}
	    	 }
	     }
	     return bitmap;
	}
}
