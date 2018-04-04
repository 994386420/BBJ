package com.bbk.evaluator;

import android.animation.TypeEvaluator;
import android.graphics.Color;

public class BGAlphaEvaluator implements TypeEvaluator<Integer> {  

	private int mCurrentAlpha = -1;  
  
    @Override  
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        // 初始化背景通道值 
        if (mCurrentAlpha == -1) {  
            mCurrentAlpha = startValue;  
        }  
        // 计算初始和结束之间的差值  
        int alphaDiff = Math.abs(startValue - endValue);
        int currentA = getCurrentColor(startValue,endValue,alphaDiff,fraction);
        String ca = getHexString(currentA);
        // 将计算出的当前颜色的值组装返回  
        String currentColor = "#"+ca+"000000";
        return Color.parseColor(currentColor);  
    }  
    /** 
     * 根据fraction值来计算当前的背景通道值。 
     */  
    private int getCurrentColor(int startAlpha, int endAlpha, int aplphaDiff,float fraction) {  
        int currentColor;  
        if (startAlpha > endAlpha) {  
            currentColor = (int) (startAlpha - (fraction * aplphaDiff));  
            if (currentColor < endAlpha) {  
                currentColor = endAlpha;  
            }  
        } else {  
            currentColor = (int) (startAlpha + (fraction * aplphaDiff));  
            if (currentColor > endAlpha) {
                currentColor = endAlpha;  
            }  
        }  
        return currentColor;  
    } 
  
    /** 
     * 将10进制颜色值转换成16进制。 
     */  
    private String getHexString(int value) {  
        String hexString = Integer.toHexString(value);  
        if (hexString.length() == 1) {  
            hexString = "0" + hexString;  
        }  
        return hexString;  
    }  
  
}  
