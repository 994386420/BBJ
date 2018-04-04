package com.bbk.util;

import java.text.DecimalFormat;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bbk.activity.R;

/**
 * 
 * @author LiangRan
 * @date 2016-03-31
 * 评分添加星星图片
 */
public class AddStarUtil {
	
	/**
	 * 
	 * @param context
	 * @param starLayout
	 * @param score
	 */
	public static void addStar(Context context, LinearLayout starLayout, String score) {
		if(TextUtils.isEmpty(score)) {
			score = "0.0";
		}
		
		float scoreF = Float.parseFloat(score);
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		String[] aa = decimalFormat.format(scoreF / 2).split("\\.");
		
		int starNum = Integer.parseInt(aa[0]);
		int halfStarNum = Integer.parseInt(aa[1]) >= 5 ? 1 : 0;
		
		int w = DensityUtil.dip2px(context, 12);
		int h = w;
		int leftMargin = DensityUtil.dip2px(context, 4);
		for(int i = 1; i <= 5; i ++) {
			ImageView img = new ImageView(context);
		
			if(i <= starNum) {
				LoadImgUtil.loadImg(context, img, R.mipmap.icon_star_yellow);
			} else if(i == (starNum + 1) && halfStarNum == 1) {
				LoadImgUtil.loadImg(context, img, R.mipmap.icon_star_half_yellow);
			} else {
				LoadImgUtil.loadImg(context, img, R.mipmap.icon_star_gray);
			}

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
			params.leftMargin = leftMargin;
			starLayout.addView(img, i, params);
		}
	}
	
}
