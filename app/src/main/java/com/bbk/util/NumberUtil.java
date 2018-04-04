package com.bbk.util;

/**
 *  <code>NumberUtil.java</code>
 *  <p>功能:字符转数字工具类
 *  
 *  <p>Copyright 比比网络信息技术 2014 All right reserved.
 *  @author 李量 zaplilang@163.com 时间 2014-3-19 下午03:45:40	
 *  @version 1.0 
 */
public class NumberUtil {

	public static final double parseDouble(String parseStr, double defaultDouble) {
		double retDouble = defaultDouble;
		try {
			retDouble = Double.valueOf(parseStr);
		} catch (Exception ex) {
			retDouble = defaultDouble;
		}

		return retDouble;
	}

	public static final float parseFloat(String parseStr, float defaultFloat) {
		float retFloat = defaultFloat;
		try {
			retFloat = Float.valueOf(parseStr);
		} catch (Exception ex) {
			retFloat = defaultFloat;
		}
		return retFloat;
	}

	public static final int parseInt(String parseStr, int defaultInt) {
		int retInt = defaultInt;
		try {
			retInt = Integer.valueOf(parseStr);
		} catch (Exception ex) {
			retInt = defaultInt;
		}
		return retInt;
	}

	public static final long parseLong(String parseStr, long defaultLong) {
		long retLong = defaultLong;
		try {
			retLong = Long.valueOf(parseStr);
		} catch (Exception ex) {
			retLong = defaultLong;
		}
		return retLong;
	}
	
	

}
