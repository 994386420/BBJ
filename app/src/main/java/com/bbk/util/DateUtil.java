package com.bbk.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static String getDate() {
		Date dt = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
		String systemDate = dateFormat.format(dt); 
		return systemDate; 
	}
	
	public static String getDateDD() {
		Date dt = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
		String systemDate = dateFormat.format(dt); 
		return systemDate; 
	}
	
	public static int compareDate(String dateStr1, String dateStr2) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			long date1 = dateFormat.parse(dateStr1).getTime();
			long date2 = dateFormat.parse(dateStr2).getTime();
			if(date1 > date2) {
				return 1;
			} else if (date1 == date2) {
				return 0;
			} else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static int compareDateSS(String dateStr1, String dateStr2) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			long date1 = dateFormat.parse(dateStr1).getTime();
			long date2 = dateFormat.parse(dateStr2).getTime();
			if(date1 > date2) {
				return 1;
			} else if (date1 == date2) {
				return 0;
			} else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	public static String TimeDifference(String time){
		try {
			int sum = addsum(time);
			// 求出天数
			int day = sum / 60 / 60 / 24;
			// int day_time = sum % 24;
			Log.e("小时", day + "");
			Log.e("小时", sum % 24 + "");

			// 求出小时
			// int hour = day_time / 60;
			// int hour_time = day_time % 60;
			//
			// Log.e("小时", hour + "");
			//
			// 先获取个秒数值
			int sec = sum % 60;
			// 如果大于60秒，获取分钟。（秒数）
			int sec_time = sum / 60;
			// 再获取分钟
			int min = sec_time % 60;
			// 如果大于60分钟，获取小时（分钟数）。
			int min_time = sec_time / 60;
			// 获取小时
			int hour = min_time % 24;

			int day_decade = day / 10;
			int day_unit = day - day_decade * 10;

			int hour_decade = hour / 10;
			int hour_unit = hour - hour_decade * 10;

			int min_decade = min / 10;
			int min_unit = min - min_decade * 10;

			int sec_decade = sec / 10;
			int sec_unit = sec - sec_decade * 10;
			String day5 = day_decade+""+day_unit;
			String hour5 = hour_decade+""+hour_unit;
			String min5 = min_decade+""+min_unit;
			return day5+"天"+hour5+"时"+min5+"分";
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}
	public static int addsum(String time) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		Date date2;
		date2 = sDateFormat.parse(date);
		Calendar calendar2=Calendar.getInstance();
		calendar2.setTime(date2);
		Date date1 = sDateFormat.parse(time);
		calendar.setTime(date1);
		//获取系统的日期
		//年
		int year = calendar.get(Calendar.YEAR);
		int year2 = calendar2.get(Calendar.YEAR);
		//月
		int month = calendar.get(Calendar.MONTH);
		int month2 = calendar2.get(Calendar.MONTH);
		//日
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
		//获取系统时间
		//小时
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int hour2 = calendar2.get(Calendar.HOUR_OF_DAY);
		//分钟
		int minute = calendar.get(Calendar.MINUTE);
		int minute2 = calendar2.get(Calendar.MINUTE);
		//秒
		int second = calendar.get(Calendar.SECOND);
		int second2 = calendar2.get(Calendar.SECOND);
		int d1 = day*24*60*60+hour*60*60+minute*60+second + year*365*24*60*60;
		int d2 = day2*24*60*60+hour2*60*60+minute2*60+second2 + year*365*24*60*60;
		int sum = d1 - d2;
		return sum;
	}

}
