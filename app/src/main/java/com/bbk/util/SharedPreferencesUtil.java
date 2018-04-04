package com.bbk.util;

import java.util.HashMap;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class SharedPreferencesUtil {
	
	public static HashMap<String, String> getSharedData(Context context, String name) {
		if(TextUtils.isEmpty(name)) {
			return null;
		}
		SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE|Context.MODE_MULTI_PROCESS);
		return (HashMap<String, String>) preferences.getAll();
	}
	
	public static void putSharedData(Context context, String name, HashMap<String, String> dataMap) {
		if(TextUtils.isEmpty(name)) {
			return;
		}
		SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE|Context.MODE_MULTI_PROCESS);
		Editor editor = preferences.edit();
		
		for (Entry<String, String> entry: dataMap.entrySet()) {
		    editor.putString(entry.getKey(), entry.getValue());
		}
		
		editor.commit();
	}
	
	public static String getSharedData(Context context, String name, String key) {
		if(TextUtils.isEmpty(name)) {
			return "";
		}
		
		try {
			SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE|Context.MODE_MULTI_PROCESS);
			return preferences.getString(key, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static void putSharedData(Context context, String name, String key, String value) {
		if(TextUtils.isEmpty(name)) {
			return;
		}
		try {
			SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE|Context.MODE_MULTI_PROCESS);
			Editor editor = preferences.edit();
			editor.putString(key, value);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void cleanShareData(Context context, String name) {
		if(TextUtils.isEmpty(name)) {
			return;
		}
		SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE|Context.MODE_MULTI_PROCESS);
		Editor editor = preferences.edit();  
		editor.clear();  
		editor.commit(); 
	}
	
	
}
