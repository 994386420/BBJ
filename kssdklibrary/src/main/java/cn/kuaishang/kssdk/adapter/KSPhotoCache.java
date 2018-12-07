package cn.kuaishang.kssdk.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

public class KSPhotoCache {

	private static KSPhotoCache instance=new KSPhotoCache();//当前对象
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	
	private KSPhotoCache(){}
	
	public static KSPhotoCache getInstance() {
		return instance;
	}
	
	/**
	 * 添加缓存图片
	 * @param path
	 * @param bm
	 */
	public void put(String path, Bitmap bm) {
		if (!TextUtils.isEmpty(path) && bm != null) {
			imageCache.put(path, new SoftReference<Bitmap>(bm));
		}
	}
	
	/**
	 * 获取缓存图片
	 * @param path
	 */
	public Bitmap get(String path) {
		if (TextUtils.isEmpty(path))return null;
		if (imageCache.containsKey(path)) {
			SoftReference<Bitmap> reference = imageCache.get(path);
			return reference.get();
		}
		return null;
	}
}
