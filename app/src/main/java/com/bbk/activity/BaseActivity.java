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
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.bbk.Bean.CheckBean;
import com.bbk.Bean.DemoTradeCallback;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.fragment.NewHomeFragment;
import com.bbk.resource.NewConstants;
import com.bbk.util.ClipDialogUtil;
import com.bbk.util.DialogCheckYouhuiUtil;
import com.bbk.util.SchemeIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.UpdataDialog;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.logg.Logg;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

 public class BaseActivity extends Activity {
 	private ClipDialogUtil clipDialogUtil;
	 private ClipboardManager clipboardManager;
	 private long previousTime = 0;
	 private CheckBean checkBean;
	 private static AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
	 private static Map<String, String> exParams;//yhhpass参数
	 private static Handler mHandler = new Handler();
	 private static UpdataDialog updataDialog;
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
//    	//友盟错误统计
//    	 MobclickAgent.setCatchUncaughtExceptions(true);
//        Log.e("BaseActivity", "onInit");
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
		MobclickAgent.onResume(this);
//		String clipchange = SharedPreferencesUtil.getSharedData(this, "clipchange", "clipchange");
//		if (clipchange.equals("1")) {
//			if (BaseActivity.this != null ){
//				ClipDialogUtil.creatDialog(this);
//			    SharedPreferencesUtil.putSharedData(getApplicationContext(), "clipchange", "clipchange", "0");
//		    }
//		}
//		//外部应用打开APP传递参数
//		Intent intent = getIntent();
//		String scheme = intent.getScheme();
//		Uri uri = intent.getData();
//		if (uri!= null){
//			SchemeIntentUtil.intent(uri,this);
//		}

	}
	 private void checkExsistProduct(String text) {
		 Map<String, String> paramsMap = new HashMap<String, String>();
		 paramsMap.put("url", text);
		 RetrofitClient.getInstance(this).createBaseApi().checkExsistProduct(
				 paramsMap, new BaseObserver<String>(this) {
					 @Override
					 public void onNext(String s) {
						 try {
							 JSONObject jsonObject = new JSONObject(s);
							 if (jsonObject.optString("status").equals("1")) {
								 String content = jsonObject.optString("content");
								 JSONObject json = new JSONObject(content);
//						Log.i("checkprice：",jsonObject+"------------------------");
//								if (!json.optString("rowkey").isEmpty()) {
								 SharedPreferencesUtil.putSharedData(getApplicationContext(), "clipchange", "clipchange", "1");
								 SharedPreferencesUtil.putSharedData(getApplicationContext(), "clipchange", "object", content);
//								}
//								 Logg.json(content);
							     checkBean = JSON.parseObject(content,CheckBean.class);
								 if (checkBean.getHasCps() != null) {
									 if (checkBean.getHasCps().equals("1")) {
										 mHandler.postDelayed(new Runnable() {
											 @Override
											 public void run() {
												 Intent intent = new Intent(BaseActivity.this, IntentActivity.class);
												 if (checkBean.getUrl() != null && !checkBean.getUrl().equals("")) {
													 intent.putExtra("url", checkBean.getUrl());
												 }
												 if (checkBean.getDomain() != null && !checkBean.getDomain().equals("")) {
													 intent.putExtra("domain", checkBean.getDomain());
												 }
												 if (checkBean.getRowkey()!= null && !checkBean.getRowkey().equals("")) {
													 intent.putExtra("groupRowKey", checkBean.getRowkey());
												 }
												 if (checkBean.getPrice() != null && !checkBean.getPrice().equals("")) {
													 intent.putExtra("bprice", checkBean.getPrice());
												 }
												 DialogCheckYouhuiUtil.dismiss(2000);
												 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
												 startActivity(intent);
											 }
										 }, 2000);
									 }
								 }else{
									 DialogCheckYouhuiUtil.dismiss(2000);
									 mHandler.postDelayed(new Runnable() {
										 @Override
										 public void run() {
											 showMessageDialog(BaseActivity.this,checkBean.getUrl());;//耗时操作
										 }
									 }, 2000);
								 }
							 }
						 } catch (JSONException e) {
							 e.printStackTrace();
						 }
					 }
					 @Override
					 protected void hideDialog() {
//						 DialogCheckYouhuiUtil.dismiss(2000);
						 clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
					 }

					 @Override
					 protected void showDialog() {
						 if (BaseActivity.this != null){
							 DialogCheckYouhuiUtil.show(BaseActivity.this);
						 }
					 }

					 @Override
					 public void onError(ExceptionHandle.ResponeThrowable e) {
//						StringUtil.showToast(FloatingWindowService.this, e.message);
						 DialogCheckYouhuiUtil.dismiss(0);
					 }
				 });

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
			StringUtil.showToast(this, "没有网络连接,请设置！");
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
	 /**
	  *
	  * @param context
	  */
	 public void showMessageDialog(final Context context, final String url) {
		 if(updataDialog == null || !updataDialog.isShowing()) {
			 //初始化弹窗 布局 点击事件的id
			 updataDialog = new UpdataDialog(context, R.layout.check_nomessage_dialog_layout,
					 new int[]{R.id.tv_update_gengxin});
			 updataDialog.show();
			 updataDialog.setCanceledOnTouchOutside(true);
			 TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
			 tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View v) {
					 updataDialog.dismiss();
					 jumpThirdApp(url);
				 }
			 });
			 LinearLayout ll_close = updataDialog.findViewById(R.id.ll_close);
			 ll_close.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View v) {
					 updataDialog.dismiss();
				 }
			 });
		 }
	 }



	 public  void jumpThirdApp(String url){
//			DialogSingleUtil.show(context);
		 alibcShowParams = new AlibcShowParams(OpenType.Native, false);
		 alibcShowParams.setClientType("taobao_scheme");
		 exParams = new HashMap<>();
		 exParams.put("isv_code", "appisvcode");
		 exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
//			if (domain != null) {
		 if (url.contains("tmall") || url.contains("taobao")) {
			 showUrl(url);
		 } else if (url.contains("jd")) {
			 KeplerApiManager.getWebViewService().openAppWebViewPage(BaseActivity.this,
					 url,
					 mKeplerAttachParameter,
					 mOpenAppAction);
//					DialogSingleUtil.dismiss(100);
		 } else {
			 Intent intent = new Intent(BaseActivity.this, WebViewActivity.class);
			 if (url != null) {
				 intent.putExtra("url", url);
			 }
//					if (rowkey != null) {
//						intent.putExtra("rowkey", rowkey);
//					}
			 startActivity(intent);
//					DialogSingleUtil.dismiss(50);
		 }
//			}
	 }

	 /**
	  * 打开指定链接
	  */
	 public  void showUrl(String url) {
		 String text = url;
		 if (TextUtils.isEmpty(text)) {
			 StringUtil.showToast(BaseActivity.this, "URL为空");
			 return;
		 }
		 AlibcTrade.show(BaseActivity.this, new AlibcPage(text), alibcShowParams, null, exParams, new DemoTradeCallback());
//		DialogSingleUtil.dismiss(100);
	 }

	 private static KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();

	 OpenAppAction mOpenAppAction = new OpenAppAction() {
		 @Override
		 public void onStatus(final int status, final String url) {
			 Intent intent;
			 if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
//				DialogSingleUtil.show(context);
			 } else {
//				DialogSingleUtil.dismiss(0);
			 }
			 if (status == OpenAppAction.OpenAppAction_result_NoJDAPP) {
				 StringUtil.showToast(BaseActivity.this, "未安装京东");
				 intent = new Intent(BaseActivity.this, WebViewActivity.class);
				 if (url != null) {
					 intent.putExtra("url", url);
				 }
//				if (rowkey != null) {
//					intent.putExtra("rowkey", rowkey);
//				}
				 startActivity(intent);
				 //未安装京东
			 } else if (status == OpenAppAction.OpenAppAction_result_BlackUrl) {
				 StringUtil.showToast(BaseActivity.this, "不在白名单");
				 //不在白名单
			 } else if (status == OpenAppAction.OpenAppAction_result_ErrorScheme) {
				 StringUtil.showToast(BaseActivity.this, "协议错误");
				 //协议错误
			 } else if (status == OpenAppAction.OpenAppAction_result_APP) {
				 //呼京东成功
			 } else if (status == OpenAppAction.OpenAppAction_result_NetError) {
				 StringUtil.showToast(BaseActivity.this, "网络异常");
				 //网络异常
			 }
		 }
	 };


	 @Override
	 protected void onStart() {
		 super.onStart();
		 NewConstants.showdialogFlg = "0";
		 clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		 if ( clipboardManager.getText() != null){

		 String text = clipboardManager.getText().toString();
		 if (text != null && !text.equals("") && !text.equals("null")) {
			 if (text.contains("bbj")) {
				 NewConstants.copyText = text;
			 }
			 // //获得当前activity的名字
			 if (!text.contains("标题:")) {
				 SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "clipchange", "cm", text);
				 Logg.e("======>>>", text);
				 if (text.contains("http") && text.contains("jd") || text.contains("https") && text.contains("jd") || text.contains("http") && text.contains("taobao") || text.contains("http") && text.contains("tmall") ||
						 text.contains("http") && text.contains("zmnxbc") || text.contains("http") && text.contains("点击链接") || text.contains("http") && text.contains("喵口令") || text.contains("https") && text.contains("taobao")
						 || text.contains("https") && text.contains("tmall") || text.contains("https") && text.contains("zmnxbc") || text.contains("https") && text.contains("点击链接") || text.contains("https") && text.contains("喵口令")) {
					 checkExsistProduct(text);
				 }
			 }
		 }
		 }
	 }

	 @Override
	 protected void onDestroy() {
		 super.onDestroy();
		 DialogCheckYouhuiUtil.dismiss(0);
	 }

 }
