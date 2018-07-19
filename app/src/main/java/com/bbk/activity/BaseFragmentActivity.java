package com.bbk.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.bbk.util.ClipDialogUtil;
import com.bbk.util.SchemeIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.umeng.analytics.MobclickAgent;

public class BaseFragmentActivity extends FragmentActivity {
	private ClipDialogUtil clipDialogUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		connectivities();
		clipDialogUtil = new ClipDialogUtil(this);
		Looper.myQueue().addIdleHandler(new IdleHandler() {
            @Override
            public boolean queueIdle() {
//                Log.i("IdleHandler","queueIdle");
                onInit();
                return false; //false 表示只监听一次IDLE事件,之后就不会再执行这个函数了.
            }
        });
	}
	
	//子类重写此函数即可,而不需要在onCreate()中去初始化.
    protected void onInit() {
		// 设置为U-APP场景
		MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
		//友盟错误统计
		MobclickAgent.setCatchUncaughtExceptions(true);
    }

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
		MobclickAgent.onResume(this);
		String clipchange = SharedPreferencesUtil.getSharedData(this, "clipchange", "clipchange");
		if (clipchange.equals("1")) {
			if (BaseFragmentActivity.this != null) {
				ClipDialogUtil.creatDialog(this);
				SharedPreferencesUtil.putSharedData(getApplicationContext(), "clipchange", "clipchange", "0");
			}
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//外部应用打开APP传递参数
//		Intent intent = getIntent();
//		String scheme = intent.getScheme();
//		Uri uri = intent.getData();
//		if (uri!= null){
//			SchemeIntentUtil.intent(uri,this);
//		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
		MobclickAgent.onPause(this);
	}


	public void connectivities() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (null == networkInfo) {
			StringUtil.showToast(this, "没有网络连接");
		} else if (!wifiInfo.isConnected()) {
//			Toast toast = Toast.makeText(this, "您的WIFI未连接，正在使用流量",
//					Toast.LENGTH_LONG);
//			toast.show();
		}
	}
	@SuppressWarnings("unchecked")
	public <T extends View> T $(int resId) {
		return (T) super.findViewById(resId);
	}

	protected View inflateView(int id) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(id, null);
		return view;
	}
	protected MyApplication getApp(){
		return (MyApplication) getApplication();
	}
}
