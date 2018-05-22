package com.bbk.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.activity.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {
	private static RelativeLayout inflater;
	public static String getResultNum(String str) {
		char[] a = str.toCharArray();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < a.length; i++) {
			char tmp = a[i];
			String s1 = "[0-9 -]";
			Pattern p = Pattern.compile(s1);
			Matcher m = p.matcher(tmp + "");
			if (m.find()) {
				sb.append(tmp);
			} 
		}

		return sb.toString();
	}

	public static String getCode() {
		StringBuffer codeBuffer = new StringBuffer();
		for(int i = 0; i < 6; i ++) {
			int code = (int) (Math.random() * 10);
			codeBuffer.append(code);
		}
		return codeBuffer.toString();
	}

	public static boolean isNum(String str) {

		char[] a = str.toCharArray();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < a.length; i++) {
			char tmp = a[i];
			String s1 = "[0-9]";
			Pattern p = Pattern.compile(s1);
			Matcher m = p.matcher(tmp + "");
			if (!m.find()) {
				return false;
			} 
		}
		return true;
	}
	public static boolean isPhoneNum(String str){
		return str.matches("^((1[3,5,8][0-9])|(14[5,7])|(17[0,1,6,7,8]))\\d{8}$");
	}

	public static String getRsult(String str) {
		char[] a = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for(int i = 2; i < a.length; i ++) {
			sb.append(a[i]);
		}
		return sb.toString();
	}

	public static boolean isJsonArray(String str){
		final char[] strChar = str.substring(0, 1).toCharArray(); 
		final char firstChar = strChar[0];
		if(firstChar == '['){ 
			return true;
		}else{ 
			return false;
		} 
	}
	public static void main(String[] args) {
		System.out.println(isPhoneNum("174813925349"));
	}

	//提示消息自定义弹窗
	public static void showToast(Context context,String tishi) {
// TODO Auto-generated method stub
		View layout = inflater.inflate(context, R.layout.toast_layout, null);
		TextView title = layout.findViewById(R.id.toast_title);
		title.setText(tishi);
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(layout);
		toast.show();
	}

	public  static List removeDuplicate(List list)  {
		for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
			for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
				if  (list.get(j).equals(list.get(i)))  {
					list.remove(j);
				}
			}
		}
		return list;
	}

	public static boolean isNullOrEmpty(String content){
		boolean result = false;
		if(null == content || "".equals(content.trim())){
			return true;
		}
		return false;
	}

	/**
	 * 验证手机号码的格式是否正确
	 */
	public static boolean isMobilePhoneVerify(String mobileString){
		if(mobileString ==null || "".equals(mobileString.trim())){
			return true;
		}else{
			Pattern p = Pattern.compile("^1[0-9][0-9]{9}$");
			Matcher m = p.matcher(mobileString.trim());
			if(!m.matches()){
				return true;
			}else{
				return false;
			}
		}

	}
}
