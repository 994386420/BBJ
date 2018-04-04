 package com.bbk.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.bbk.util.ClipDialogUtil;
import com.bbk.util.SchemeIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

 public class BaseActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectivities();
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
//    	//友盟错误统计
//    	 MobclickAgent.setCatchUncaughtExceptions(true);
//        Log.e("BaseActivity", "onInit");
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
		MobclickAgent.onResume(this);
		String clipchange = SharedPreferencesUtil.getSharedData(this, "clipchange", "clipchange");
		if (clipchange.equals("1")) {
			ClipDialogUtil.creatDialog(this);
			SharedPreferencesUtil.putSharedData(getApplicationContext(), "clipchange", "clipchange", "0");
		}
//		//外部应用打开APP传递参数
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

	private boolean connectivities() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (null == networkInfo) {
			Toast toast = Toast.makeText(this, "没有网络连接,请设置！", Toast.LENGTH_LONG);
			toast.show();
			return false;
		} else if (!wifiInfo.isConnected()) {
//			Toast toast = Toast.makeText(this, "您的WIFI未连接，正在使用流量",
//					Toast.LENGTH_SHORT);
//			toast.show();
		}
		final ClipboardManager cm1 = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);  
		cm1.addPrimaryClipChangedListener(new OnPrimaryClipChangedListener() {  
		    @Override  
		    public void onPrimaryClipChanged() {  
		        ClipData data = cm1.getPrimaryClip();  
		                Item item = data.getItemAt(0);  
		        Intent mIntent = new Intent();  
		        mIntent.setAction("com.cybertron.dict.ClipBoardReceiver");
				if (item.getText()!=null) {
					mIntent.putExtra("clipboardvalue", item.getText().toString());
					sendBroadcast(mIntent);
				}
		    }  
		});
		return true;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends View> T $(int resId) {
		return (T) super.findViewById(resId);
	}
	protected MyApplication getApp(){
		return (MyApplication) getApplication();
	}
	protected View inflateView(int id) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(id, null);
		return view;
	}
	
}
