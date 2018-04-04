package com.bbk.server;

import java.util.Stack;

import android.app.Activity;

public class ActivityManager {
	private static Stack activityStack;
	private static ActivityManager instance;
	
	private ActivityManager() {}

	public static ActivityManager getInstance() {
		if(instance == null) {
			instance = new ActivityManager();
		}
		return instance;
	}
	
	/**
	 * 出栈栈顶Activity
	 */
	public void popActivity() {
		Activity activity = (Activity) activityStack.lastElement();
		
		if(activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}
	
	/**
	 * 出栈指定Activity
	 * @param activity
	 */
	public void popActivity(Activity activity) {
		if(activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}
	
	/**
	 * 获取当前栈顶Activity
	 * @return
	 */
	public Activity currentActivity() {
		Activity activity = (Activity) activityStack.lastElement();
		return activity;
	}
	
	/**
	 * 入栈Activity
	 * @param activity
	 */
	public void pushActivity(Activity activity) {
		if(activityStack == null) {
			activityStack = new Stack();
		}
		activityStack.add(activity);
	}
	
	/**
	 * 保留指定Activity，其余全部出栈
	 * @param cls
	 */
	public void popAllActivityExceptOne(Class cls) {
		while(true) {
			Activity activity = currentActivity();
			if(activity == null) {
				break;
			}
			if(activity.getClass().equals(cls)) {
				break;
			}
		}
	}
}
